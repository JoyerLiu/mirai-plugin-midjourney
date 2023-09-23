package top.joyer.Midjourney;

import com.alibaba.fastjson2.annotation.JSONField;

public class ImgResponse {
    @JSONField(name = "action")
    private String action;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "failReason")
    private String failReason;

    @JSONField(name = "finishTime")
    private long finishTime;

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "imageUrl")
    private String imageUrl;

    @JSONField(name = "progress")
    private String progress;

    @JSONField(name = "prompt")
    private String prompt;

    @JSONField(name = "promptEn")
    private String promptEn;

    @JSONField(name = "startTime")
    private long startTime;

    @JSONField(name = "state")
    private String state;

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "submitTime")
    private long submitTime;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPromptEn() {
        return promptEn;
    }

    public void setPromptEn(String promptEn) {
        this.promptEn = promptEn;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    @Override
    public String toString() {
        return "ImgResponse{" +
                "action='" + action + '\'' +
                ", description='" + description + '\'' +
                ", failReason='" + failReason + '\'' +
                ", finishTime=" + finishTime +
                ", id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", progress='" + progress + '\'' +
                ", prompt='" + prompt + '\'' +
                ", promptEn='" + promptEn + '\'' +
                ", startTime=" + startTime +
                ", state='" + state + '\'' +
                ", status='" + status + '\'' +
                ", submitTime=" + submitTime +
                '}';
    }
}
