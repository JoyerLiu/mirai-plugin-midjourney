package top.joyer.Midjourney;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;

public class Response {
    @JSONField(name = "code")
    private int code;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "properties")
    private JSONObject properties;
    @JSONField(name = "result")
    private String taskID;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public JSONObject getProperties() {
        return properties;
    }

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", properties=" + properties +
                ", taskID='" + taskID + '\'' +
                '}';
    }
}
