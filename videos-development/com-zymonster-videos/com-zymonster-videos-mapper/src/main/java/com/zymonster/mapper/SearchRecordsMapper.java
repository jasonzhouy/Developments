package com.zymonster.mapper;

import com.zymonster.pojo.SearchRecords;
import com.zymonster.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> getHotwords();
}