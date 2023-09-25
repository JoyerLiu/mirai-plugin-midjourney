package top.joyer.Midjourney;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import top.joyer.Utils.HttpUtils;

import java.io.IOException;

public class MidjourneyHttp {
    String api_key ="";
    String api_url="";

    public MidjourneyHttp(String api_url, String api_key ) {
        setApi_url(api_url);
        setApi_key(api_key);
    }

    public String getApi_url() {
        return api_url;
    }

    public void setApi_url(String api_url) {
        if(api_url.charAt(api_url.length()-1)=='/')
            api_url=api_url.substring(0,api_url.length()-1); //去除可能的‘/’符号
        this.api_url = api_url;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }



    public Response newTask(String[] base64Array, String prompt, String state) throws IOException {
        JSONObject request=new JSONObject();
        request.put("base64Array",base64Array);
        request.put("notifyHook","");
        request.put("prompt",prompt);
        request.put("state",state);
        String respondString = HttpUtils.sendPostRequest(getApi_url()+"/mj/submit/imagine", request.toJSONString(), api_key);//获得响应
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
        String respondString = HttpUtils.sendPostRequest(getApi_url()+"/mj/submit/change", request.toJSONString(), api_key);//获得响应
        if (respondString != null) { //响应有效时
            Response response = JSON.parseObject(respondString, Response.class);
            return response;
        }
        return null;
    }

    public ImgResponse getTaskResult(String taskID) throws IOException {
        String respondString = HttpUtils.sendGetRequest(getApi_url()+"/mj/task/"+taskID+"/fetch", api_key);//获得响应)
        if (respondString != null) { //响应有效时
            return JSON.parseObject(respondString, ImgResponse.class);
        }
        return null;
    }
}
