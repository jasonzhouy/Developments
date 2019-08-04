package com.zymonster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zymonster.mapper.*;
import com.zymonster.pojo.*;
import com.zymonster.pojo.vo.CommentsVO;
import com.zymonster.pojo.vo.VideoHistoryVO;
import com.zymonster.pojo.vo.VideosVO;
import com.zymonster.service.VideoService;
import com.zymonster.utils.PagedResult;
import com.zymonster.utils.TimeAgoUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideosMapper videosMapper;
	
	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	private VideosMapperCustom videosMapperCustom;
	
	@Autowired
	private SearchRecordsMapper searchRecordsMapper; 
	
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper; 
	
	@Autowired
	private CommentsMapper commentMapper; 
	
	@Autowired
	private CommentsMapperCustom commentMapperCustom;

	@Autowired
	private VideoHistoryMapper videoHistoryMapper;
	
	@Autowired
	private Sid sid;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveVideo(Videos video) {
		
		String id = sid.nextShort();
		video.setId(id);
		videosMapper.insertSelective(video);
		
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateVideo(String videoId, String coverPath) {
		
		Videos video = new Videos();
		video.setId(videoId);
		video.setCoverPath(coverPath);
		videosMapper.updateByPrimaryKeySelective(video);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord,
			 Integer page, Integer pageSize) {
		
		// 保存热搜词
		String desc = video.getVideoDesc();
		String userId = video.getUserId();
		if (isSaveRecord != null && isSaveRecord == 1) {
			SearchRecords record = new SearchRecords();
			String recordId = sid.nextShort();
			record.setId(recordId);
			record.setContent(desc);
			searchRecordsMapper.insert(record);
		}


		PageHelper.startPage(page, pageSize);

		List<VideosVO> list = videosMapperCustom.queryAllVideos(desc, userId);


		//修改后台的视频热度
		for (VideosVO videosVO: list
			 ) {
			if(null == videosVO.getVisitCounts())
			{
				videosVO.setVisitCounts(new Integer(1));

			}
			else
			{
				int data = videosVO.getVisitCounts();
				int result = (int)(data * 3) + 1;
				if(result >= 1000){
					result = 999;
				}
				videosVO.setVisitCounts(new Integer(result));
			}

		}
		PageInfo<VideosVO> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyLikeVideos(userId);
				
		PageInfo<VideosVO> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyFollowVideos(userId);
				
		PageInfo<VideosVO> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotwords() {
		return searchRecordsMapper.getHotwords();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
		// 1. 保存用户和视频的喜欢点赞关联关系表
		String likeId = sid.nextShort();
		UsersLikeVideos ulv = new UsersLikeVideos();
		ulv.setId(likeId);
		ulv.setUserId(userId);
		ulv.setVideoId(videoId);
		usersLikeVideosMapper.insert(ulv);
		
		// 2. 视频喜欢数量累加
		videosMapperCustom.addVideoLikeCount(videoId);
		
		// 3. 用户受喜欢数量的累加
		usersMapper.addReceiveLikeCount(videoCreaterId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
		// 1. 删除用户和视频的喜欢点赞关联关系表
		
		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("videoId", videoId);
		
		usersLikeVideosMapper.deleteByExample(example);
		
		// 2. 视频喜欢数量累减
		videosMapperCustom.reduceVideoLikeCount(videoId);
		
		// 3. 用户受喜欢数量的累减
		usersMapper.reduceReceiveLikeCount(videoCreaterId);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveComment(Comments comment) {
		String id = sid.nextShort();
		comment.setId(id);
		comment.setCreateTime(new Date());
		commentMapper.insert(comment);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
		
		PageHelper.startPage(page, pageSize);
		
		List<CommentsVO> list = commentMapperCustom.queryComments(videoId);
		
			for (CommentsVO c : list) {
				String timeAgo = TimeAgoUtils.format(c.getCreateTime());
				c.setTimeAgoStr(timeAgo);
			}
		
		PageInfo<CommentsVO> pageList = new PageInfo<>(list);
		
		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());
		
		return grid;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addVideoHistory(String videoId, String userId) {
		VideoHistory videoHistory = new VideoHistory();
		String id = sid.nextShort();
		videoHistory.setId(id);
		videoHistory.setVideoId(videoId);
		videoHistory.setUserId(userId);
		videoHistory.setVisitTime(new Date());
		videoHistoryMapper.insert(videoHistory);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<VideoHistory> getVideoHistTory(String videoId) {
		List<VideoHistory> result = videoHistoryMapper.getVideoAllHistory(videoId);
		return result;
	}



	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public VideosVO getSingleVideoInfo(String videoId) {
		VideosVO result = videosMapperCustom.queryOneVideoInfo(videoId);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<String> getVideoList(String videoId) {
		List<String> list = videosMapperCustom.queryVideoList(videoId);
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllRecommandVideos(String userId,
									Integer page, Integer pageSize) {


		List<VideoHistoryVO> temp = videoHistoryMapper.getUserAllVideoHistory(userId);
		System.out.println(temp.size());

		if(temp == null || temp.size() == 0){

			PagedResult pagedResult = new PagedResult();
			return pagedResult;
		}


		String tagsId = temp.get(0).getTagsId();
		System.out.println(tagsId);

		PageHelper.startPage(page, pageSize);

		List<VideosVO> list = videosMapperCustom.queryAllRecommandVideos(tagsId);

		System.out.println(temp.get(0).getTagsId());


		//修改后台的视频热度
		for (VideosVO videosVO: list
		) {
			if(null == videosVO.getVisitCounts())
			{
				videosVO.setVisitCounts(new Integer(1));

			}
			else
			{
				int data = videosVO.getVisitCounts();
				int result = (int)(data * 3) + 1;
				if(result >= 1000){
					result = 999;
				}
				videosVO.setVisitCounts(new Integer(result));
			}

		}
		PageInfo<VideosVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}


	//删除视频，用户接口
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteVideoInfo(String videoId) {

		videosMapperCustom.deleteVideoInfo(videoId);


	}


}
