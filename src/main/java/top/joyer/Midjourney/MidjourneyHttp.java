package top.joyer.Midjourney;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import top.joyer.HttpUtils;
import top.joyer.MidjourneySupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MidjourneyHttp {
    String request_key="";

    public MidjourneyHttp(String request_key){
        this.request_key=request_key;
    }

    public String getRequest_key() {
        return request_key;
    }

    public void setRequest_key(String request_key) {
        this.request_key = request_key;
    }



    public Response newTask(String[] base64Array, String prompt, String state) throws IOException {
        JSONObject request=new JSONObject();
        request.put("base64Array",base64Array);
        request.put("notifyHook","");
        request.put("prompt",prompt);
        request.put("state",state);
        String respondString = HttpUtils.sendPostRequest("https://api.mctools.online/mj/submit/imagine", request.toJSONString(),request_key);//获得响应
        if (respondString != null) { //响应有效时
            Response response = JSON.parseObject(respondString, Response.class);
            return response;
        }
        return null;
    }
    public Response secondTask(String action, int index, String notifyHook, String state, String taskId) throws IOException {
        JSONObject request=new JSONObject();
        request.put("action",action);
        request.put("index",index);
        request.put("notifyHook",notifyHook);
        request.put("state",state);
        request.put("taskId",taskId);
        String respondString = HttpUtils.sendPostRequest("https://api.mctools.online/mj/submit/change", request.toJSONString(),request_key);//获得响应
        if (respondString != null) { //响应有效时
            Response response = JSON.parseObject(respondString, Response.class);
            return response;
        }
        return null;
    }

    public ImgResponse getTaskResult(String taskID) throws IOException {
        String respondString = HttpUtils.sendGetRequest("https://api.mctools.online/mj/task/"+taskID+"/fetch",request_key);//获得响应)
        if (respondString != null) { //响应有效时
            return JSON.parseObject(respondString, ImgResponse.class);
        }
        return null;
    }
}
