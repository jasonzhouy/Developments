package com.zymonster.controller.controller;


import com.zymonster.common.utils.PagedResult;
import com.zymonster.common.utils.ResultJSON;
import com.zymonster.domain.Bgm;
import com.zymonster.domain.FileUpload;
import com.zymonster.service.BgmService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author zhouyang
 * @date 2019/4/8
 */

@RestController
@RequestMapping("/bgm")
public class BgmController {

    @Autowired
    private BgmService bgmServiceImpl;

    @GetMapping("/getBgmList")
    public ResultJSON getBgmList(Integer page) throws Exception{

        if (page == null){
            page = 1;
        }

        int pageSize = 10;

        PagedResult pagedResult =  bgmServiceImpl.getAllUserInfo(page,pageSize);

        return ResultJSON.ok(pagedResult);

    }


    @GetMapping("/remove")
    public ResultJSON deleteBgm(String id) throws Exception{

        System.out.println(id);
        bgmServiceImpl.deleteBgm(id);

        return ResultJSON.ok();
    }

    @PostMapping(value = "/addBgm",headers = "content-type=multipart/form-data")
    public ResultJSON addBgm(String name,String author,MultipartFile file) throws Exception{



        if (StringUtils.isBlank(name) || StringUtils.isBlank(author)) {
            return ResultJSON.errorMsg("歌曲信息不能为空");
        }

        String uploadPathDB = File.separator + "bgm";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (file != null ) {

                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    String finalPath = "/com_zymonster_videos" + uploadPathDB + File.separator + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += (File.separator + fileName);

                    File outFile = new File(finalPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    System.out.println(finalPath);
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return ResultJSON.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJSON.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        Bgm bgm = new Bgm();
        bgm.setAuthor(author);
        bgm.setName(name);
        bgm.setPath(uploadPathDB);

        bgmServiceImpl.addBgm(bgm);

        return ResultJSON.ok("success~");

    }
}
