package com.zymonster.controller;

import com.zymonster.pojo.Users;
import com.zymonster.pojo.vo.UsersVO;
import com.zymonster.service.UserService;
import com.zymonster.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
public class RegistLoginController extends BasicController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private JmsComponent jmsComponent;

	@Autowired
	private MailServer mailServer;


	@ApiOperation(value="用户注册", notes="用户注册的接口")
	@PostMapping("/regist")
	public ResultJSON regist(@RequestBody Users user) throws Exception {


		if(StringUtils.isBlank(user.getMail())) {
			return ResultJSON.errorMsg("邮箱地址不能为空");
		}

		// 1. 判断用户名和密码必须不为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return ResultJSON.errorMsg("用户名和密码不能为空");
		}
		
		// 2. 判断用户名是否存在
		boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

		//3.判断邮箱是否存在
		boolean mailIsExist = userService.queryMailIsExist(user.getMail());
		
		// 3. 保存用户，注册信息
		if (!mailIsExist && !usernameIsExist) {
			user.setNickname(user.getUsername());
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			user.setFansCounts(0);
			user.setReceiveLikeCounts(0);
			user.setFollowCounts(0);
			user.setMail(user.getMail());
			userService.saveUser(user);

			//消息队列，异步发送邮件

			System.out.println("执行了吗？？？");

			Message message = new Message();
			message.setUsername(user.getUsername());
			message.setMailAddress(user.getMail());
			jmsComponent.send(message);

			System.out.println("执行了！");



		} else if (usernameIsExist){
			return ResultJSON.errorMsg("用户名已经存在，请换一个再试");
		} else{
			return ResultJSON.errorMsg("邮箱已经存在，请找回密码");
		}
		
		user.setPassword("");
		
//		String uniqueToken = UUID.randomUUID().toString();
//		redis.set(USER_REDIS_SESSION + ":" + user.getId(), uniqueToken, 1000 * 60 * 30);
//		
//		UsersVO userVO = new UsersVO();
//		BeanUtils.copyProperties(user, userVO);
//		userVO.setUserToken(uniqueToken);
		
		UsersVO userVO = setUserRedisSessionToken(user);
		
		return ResultJSON.ok(userVO);
	}
	
	public UsersVO setUserRedisSessionToken(Users userModel) {
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);
		
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userModel, userVO);
		userVO.setUserToken(uniqueToken);
		return userVO;
	}
	
	@ApiOperation(value="用户登录", notes="用户登录的接口")
	@PostMapping("/login")
	public ResultJSON login(@RequestBody Users user) throws Exception {
		String username = user.getUsername();
		String password = user.getPassword();
		
//		Thread.sleep(3000);
		
		// 1. 判断用户名和密码必须不为空
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return ResultJSON.ok("用户名或密码不能为空...");
		}
		
		// 2. 判断用户是否存在
		Users userResult = userService.queryUserForLogin(username, 
				MD5Utils.getMD5Str(user.getPassword()));
		
		// 3. 返回
		if (userResult != null) {
			userResult.setPassword("");
			UsersVO userVO = setUserRedisSessionToken(userResult);
			return ResultJSON.ok(userVO);
		} else {
			return ResultJSON.errorMsg("用户名或密码不正确, 请重试...");
		}
	}
	
	@ApiOperation(value="用户注销", notes="用户注销的接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true, 
						dataType="String", paramType="query")
	@PostMapping("/logout")
	public ResultJSON logout(String userId) throws Exception {
		redis.del(USER_REDIS_SESSION + ":" + userId);
		return ResultJSON.ok();
	}


	@PostMapping("/forgetPassword")
	@ApiOperation(value = "找回密码发送邮件",notes = "用户找回密码的发送邮件接口")
	@ApiImplicitParam(name = "userMail",value = "用户邮箱",required = true,
	dataType = "String",paramType = "query")
	public ResultJSON forgetPassword(String userMail) throws Exception{

		boolean userMailIsExist = userService.queryMailIsExist(userMail);

		if(!userMailIsExist) {
			return ResultJSON.errorMsg("邮箱不存在,请确认");
		}
		else{
			String username = userService.queryUserName(userMail);
			if (username == ""){
				return ResultJSON.errorMsg("未找到用户名！");
			}
			String date = new Date().toString();
			String enUsername = CryptoUtil.encode(username);
			String enDate = CryptoUtil.encode(date);
			Map<String,Object> valueMap = new HashMap<>();
			valueMap.put("to",new String[]{userMail});
			valueMap.put("username",enUsername);
			valueMap.put("date",enDate);
			valueMap.put("title","这是您在This Moment上的找回链接");
			mailServer.sendPasswordMail(valueMap);
			return ResultJSON.ok("邮件已发送");
		}

	}
	
}
