package com.zymonster.domain;

import java.util.Date;

/**
 * @author zhouyang
 * @date 2019/4/25
 */
public class Report {

    private String id;

    private String videoId;

    private String reportFrom;

    private String reportTo;

    private String reportVideoCoverPath;

    private String reportTitle;

    private String reportReason;

    private String reportVideoPath;

    private Date reportTime;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportFrom() {
        return reportFrom;
    }

    public void setReportFrom(String reportFrom) {
        this.reportFrom = reportFrom;
    }

    public String getReportTo() {
        return reportTo;
    }

    public void setReportTo(String reportTo) {
        this.reportTo = reportTo;
    }

    public String getReportVideoCoverPath() {
        return reportVideoCoverPath;
    }

    public void setReportVideoCoverPath(String reportVideoCoverPath) {
        this.reportVideoCoverPath = reportVideoCoverPath;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportVideoPath() {
        return reportVideoPath;
    }

    public void setReportVideoPath(String reportVideoPath) {
        this.reportVideoPath = reportVideoPath;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }
}
