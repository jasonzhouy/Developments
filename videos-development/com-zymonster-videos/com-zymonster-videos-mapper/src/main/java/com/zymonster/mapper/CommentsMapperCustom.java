package com.zymonster.mapper;

import com.zymonster.pojo.Comments;
import com.zymonster.pojo.vo.CommentsVO;
import com.zymonster.utils.MyMapper;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}