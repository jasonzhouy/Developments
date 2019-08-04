package com.zymonster.enums;

public enum VideoStatusEnum {
	
	SUCCESS(1),		// 发布成功
	FORBID(2),		// 禁止播放，管理员操作
	DELETE(3);		//用户删除的视频
	
	public final int value;
	
	VideoStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
