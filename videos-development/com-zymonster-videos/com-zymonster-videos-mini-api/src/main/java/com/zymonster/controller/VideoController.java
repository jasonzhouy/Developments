package com.zymonster.controller;

import com.zymonster.enums.VideoStatusEnum;
import com.zymonster.mapper.VideosMapperCustom;
import com.zymonster.pojo.Bgm;
import com.zymonster.pojo.Comments;
import com.zymonster.pojo.Videos;
import com.zymonster.pojo.vo.VideosVO;
import com.zymonster.service.BgmService;
import com.zymonster.service.VideoService;
import com.zymonster.utils.ResultJSON;
import com.zymonster.utils.MergeVideoMp3;
import com.zymonster.utils.PagedResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@RestController
@Api(value = "视频相关业务的接口", tags = {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;


    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "tagsId", value = "标签描述", required = false,
                    dataType = "String", paramType = "form")
    })


    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public ResultJSON upload(String userId,
                             String bgmId, double videoSeconds,
                             int videoWidth, int videoHeight,
                             String desc, String tagsId,
                             @ApiParam(value = "短视频", required = true)
                                     MultipartFile file) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return ResultJSON.errorMsg("用户id不能为空...");
        }

        // 文件保存的命名空间
//		String fileSpace = "C:/imooc_videos_dev";
        // 保存到数据库中的相对路径
        String uploadPathDB = "/com_zymonster_videos/" + userId + "/video";
        String coverPathDB = "/com_zymonster_videos/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalVideoPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                // abc.mp4
                String arrayFilenameItem[] = fileName.split("\\.");
                String fileNamePrefix = "";
                for (int i = 0; i < arrayFilenameItem.length - 1; i++) {
                    fileNamePrefix += arrayFilenameItem[i];
                }
                // fix bug: 解决小程序端OK，PC端不OK的bug，原因：PC端和小程序端对临时视频的命名不同
//				String fileNamePrefix = fileName.split("\\.")[0];

                if (StringUtils.isNotBlank(fileName)) {

                    finalVideoPath = "" + uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);
                    coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";

                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

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

        // 判断bgmId是否为空，如果不为空，
        // 那就查询bgm的信息，并且合并视频，生产新的视频
        if (StringUtils.isNotBlank(bgmId)) {
            Bgm bgm = bgmService.queryBgmById(bgmId);
            String mp3InputPath = FILE_SPACE + bgm.getPath();

            String videoInputPath = finalVideoPath;

            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            uploadPathDB = "/" + userId + "/video" + "/" + videoOutputName;
            finalVideoPath = FILE_SPACE + uploadPathDB;
            uploadPathDB = finalVideoPath;

            Process ps = null;
            ps = Runtime.getRuntime().exec("sh " + FFMPEG_APPEND_EXE  + " " + videoInputPath + " " + mp3InputPath + " " + videoSeconds + " " + uploadPathDB);
            ps.waitFor();
            ps.exitValue();
        }
        System.out.println("uploadPathDB=" + uploadPathDB);
        System.out.println("finalVideoPath=" + finalVideoPath);

        // 对视频进行截图

        int minVideoSeconds = 2;
        int data = 0;
        if ((int) videoSeconds < minVideoSeconds || videoSeconds == minVideoSeconds) {
            data = 1;
        } else {
            data = minVideoSeconds;
        }
        if(videoWidth == 0 || videoWidth < 350){
            videoWidth = 350;
        }
        videoWidth = videoWidth - 25;
        String size = 540 + "*960";
        Process ps = null;
        ps = Runtime.getRuntime().exec("sh " + FFMPEG_EXE + " " + finalVideoPath + " " + data + " " + size + " " + coverPathDB);
        ps.waitFor();
        ps.exitValue();

        // 保存视频信息到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float) videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDB);
        video.setCoverPath(coverPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());
        video.setTagsId(tagsId);

        String videoId = videoService.saveVideo(video);

        return ResultJSON.ok(videoId);
    }

    @ApiOperation(value = "上传封面", notes = "上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频主键id", required = true,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
    public ResultJSON uploadCover(String userId,
                                  String videoId,
                                  @ApiParam(value = "视频封面", required = true)
                                          MultipartFile file) throws Exception {

        if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
            return ResultJSON.errorMsg("视频主键id和用户id不能为空...");
        }

        // 文件保存的命名空间
//		String fileSpace = "C:/imooc_videos_dev";
        // 保存到数据库中的相对路径
        String uploadPathDB = "/com_zymonster_videos/" + userId + "/video";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 文件上传的最终保存路径
        String finalCoverPath = "";
        try {
            if (file != null) {

                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {

                    finalCoverPath = uploadPathDB + "/" + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);

                    File outFile = new File(finalCoverPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

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

        videoService.updateVideo(videoId, uploadPathDB);

        return ResultJSON.ok();
    }

    /**
     * @Description: 分页和搜索查询视频列表
     * isSaveRecord：1 - 需要保存
     * 0 - 不需要保存 ，或者为空的时候
     */
    @PostMapping(value = "/showAll")
    public ResultJSON showAll(@RequestBody Videos video, Integer isSaveRecord,
                              Integer page, Integer pageSize) throws Exception {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedResult result = videoService.getAllVideos(video, isSaveRecord, page, pageSize);
        return ResultJSON.ok(result);
    }

    /**
     * @Description: 分页和搜索查询视频列表
     * isSaveRecord：1 - 需要保存
     * 0 - 不需要保存 ，或者为空的时候
     */
    @PostMapping(value = "/showAllRecommandVideo")
    public ResultJSON showAllRecommandVideo(String userId,
                                            Integer page, Integer pageSize) throws Exception {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedResult result = videoService.getAllRecommandVideos(userId,page,pageSize);
        return ResultJSON.ok(result);
    }


    /**
     * @Description: 我关注的人发的视频
     */
    @PostMapping("/showMyFollow")
    public ResultJSON showMyFollow(String userId, Integer page) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return ResultJSON.ok();
        }

        if (page == null) {
            page = 1;
        }

        int pageSize = 6;

        PagedResult videosList = videoService.queryMyFollowVideos(userId, page, pageSize);

        return ResultJSON.ok(videosList);
    }

    /**
     * @Description: 我收藏(点赞)过的视频列表
     */
    @PostMapping("/showMyLike")
    public ResultJSON showMyLike(String userId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return ResultJSON.ok();
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }

        PagedResult videosList = videoService.queryMyLikeVideos(userId, page, pageSize);

        return ResultJSON.ok(videosList);
    }

    @PostMapping(value = "/hot")
    public ResultJSON hot() throws Exception {
        return ResultJSON.ok(videoService.getHotwords());
    }

    @PostMapping(value = "/userLike")
    public ResultJSON userLike(String userId, String videoId, String videoCreaterId)
            throws Exception {
        videoService.userLikeVideo(userId, videoId, videoCreaterId);
        return ResultJSON.ok();
    }

    @PostMapping(value = "/userUnLike")
    public ResultJSON userUnLike(String userId, String videoId, String videoCreaterId) throws Exception {
        videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
        return ResultJSON.ok();
    }

    @PostMapping("/saveComment")
    public ResultJSON saveComment(@RequestBody Comments comment,
                                  String fatherCommentId, String toUserId) throws Exception {

        comment.setFatherCommentId(fatherCommentId);
        comment.setToUserId(toUserId);

        videoService.saveComment(comment);
        return ResultJSON.ok();
    }

    @PostMapping("/getVideoComments")
    public ResultJSON getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(videoId)) {
            return ResultJSON.ok();
        }

        // 分页查询视频列表，时间顺序倒序排序
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PagedResult list = videoService.getAllComments(videoId, page, pageSize);

        return ResultJSON.ok(list);
    }

    @PostMapping("/addVideoHistory")
    public ResultJSON addVideoHistory(String videoId, String userId) throws Exception {
        if (StringUtils.isBlank(videoId)) {
            return ResultJSON.errorMsg("视频信息为空");
        }
        videoService.addVideoHistory(videoId, userId);
        return ResultJSON.ok("视频保存成功");
    }


    @PostMapping("/getRandomOneVideo")
    @ApiOperation(value = "从视频库中随机获取视频", notes = "单个视频获取接口")
    public ResultJSON getRandomOneVideoExceptNow(String videoId) throws Exception {
        if (StringUtils.isBlank(videoId)) {
            return ResultJSON.errorMsg("视频Id为空");
        }
        VideosVO resultVideoVo = new VideosVO();
        List<String> result = videoService.getVideoList(videoId);
        if (result != null || result.size() != 0) {
            int index = new Random().nextInt(result.size() - 1);

            //即将播放的视频Id
            String currentVideoId = result.get(index);

            resultVideoVo = videoService.getSingleVideoInfo(currentVideoId);
            return ResultJSON.ok(resultVideoVo);
        }

        return ResultJSON.ok(resultVideoVo);

    }

    @PostMapping("/deleteVideo")
    @ApiOperation(value = "删除选中视频",notes = "删除视频接口")
    public ResultJSON deleteVideo(String videoId) throws Exception{
        videoService.deleteVideoInfo(videoId);

        return ResultJSON.ok("删除成功");
    }


}
