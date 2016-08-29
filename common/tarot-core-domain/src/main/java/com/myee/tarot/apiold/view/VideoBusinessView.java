package com.myee.tarot.apiold.view;

import com.myee.tarot.apiold.bean.BaseBean;
import com.myee.tarot.apiold.domain.VideoBusiness;
import com.myee.tarot.apiold.domain.VideoPublish;

import java.util.Date;


public class VideoBusinessView extends BaseBean {

	private Long publishId;

	private Long videoBusinessId;

	private Long videoId;

	private Integer kind;

	private String previewPath;

	private String videoPath;

	private String qiniuPath;

	private Long videoSize;

	private Date timeStart;

	private Date timeEnd;

	private Integer type;

	private String description;

	private Integer enable;

	public VideoBusinessView(){

	}

	public VideoBusinessView(VideoBusiness v){
		this.videoBusinessId = v.getId();
		this.videoId = v.getVideo().getId();
		this.kind = v.getKind();
		this.previewPath = v.getVideo().getPreviewPath();
		this.videoPath = v.getVideo().getVideoPath();
		this.qiniuPath = v.getVideo().getQiniuPath();
		this.videoSize = v.getVideo().getVideoSize();
		this.timeStart = v.getTimeStart();
		this.timeEnd = v.getTimeEnd();
		this.type = v.getType();
		this.description = v.getDescription();
	}

	public VideoBusinessView(VideoPublish videoPublish) {
		this.videoBusinessId = videoPublish.getVideoBusiness().getId();
		this.videoId = videoPublish.getVideoBusiness().getVideo().getId();
		this.kind = videoPublish.getVideoBusiness().getKind();
		this.previewPath = videoPublish.getVideoBusiness().getVideo().getPreviewPath();
		this.videoPath = videoPublish.getVideoBusiness().getVideo().getVideoPath();
		this.qiniuPath = videoPublish.getVideoBusiness().getVideo().getQiniuPath();
		this.videoSize = videoPublish.getVideoBusiness().getVideo().getVideoSize();
		this.timeStart = videoPublish.getVideoBusiness().getTimeStart();
		this.timeEnd = videoPublish.getVideoBusiness().getTimeEnd();
		this.type = videoPublish.getVideoBusiness().getType();
		this.description = videoPublish.getVideoBusiness().getDescription();
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPreviewPath() {
		return previewPath;
	}

	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Long getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(Long videoSize) {
		this.videoSize = videoSize;
	}

	public String getQiniuPath() {
		return qiniuPath;
	}

	public void setQiniuPath(String qiniuPath) {
		this.qiniuPath = qiniuPath;
	}

	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}
}