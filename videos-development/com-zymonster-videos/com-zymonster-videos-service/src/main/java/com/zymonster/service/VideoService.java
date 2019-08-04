package com.zymonster.service;

import com.zymonster.pojo.Comments;
import com.zymonster.pojo.VideoHistory;
import com.zymonster.pojo.Videos;
import com.zymonster.pojo.vo.VideosVO;
import com.zymonster.utils.PagedResult;

import java.util.List;

public interface VideoService {
	
	/**
	 * @Description: 保存视频
	 */
	public String saveVideo(Videos video);
	
	/**
	 * @Description: 修改视频的封面
	 */
	public void updateVideo(String videoId, String coverPath);
	
	/**
	 * @Description: 分页查询视频列表
	 */
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord,
                                    Integer page, Integer pageSize);
	
	/**
	 * @Description: 查询我喜欢的视频列表
	 */
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);
	
	/**
	 * @Description: 查询我关注的人的视频列表
	 */
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);
	
	/**
	 * @Description: 获取热搜词列表
	 */
	public List<String> getHotwords();
	
	/**
	 * @Description: 用户喜欢/点赞视频
	 */
	public void userLikeVideo(String userId, String videoId, String videoCreaterId);
	
	/**
	 * @Description: 用户不喜欢/取消点赞视频
	 */
	public void userUnLikeVideo(String userId, String videoId, String videoCreaterId);
	
	/**
	 * @Description: 用户留言
	 */
	public void saveComment(Comments comment);
	
	/**
	 * @Description: 留言分页
	 */
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);

	/**
	 * @Description: 添加用户观看记录
	 */

	public void addVideoHistory(String videoId,String userId);


	/**
	 * @Description: 获得视频观看记录
	 */

	public List<VideoHistory> getVideoHistTory(String videoId);

	/**
	 * @Description: 获得单个视频的所有信息
	 */

	public VideosVO getSingleVideoInfo(String videoId);

	/**
	 * @Description: 获得视频列表
	 */

	public List<String> getVideoList(String videoId);

	/**
	 * @Description: 分页查询视频列表
	 */
	public PagedResult getAllRecommandVideos(String userId,
									Integer page, Integer pageSize);


	/**
	 * @Description: 给用户删除视频
	 */

	public void deleteVideoInfo(String videoId);
}


