package top.joyer.Midjourney;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import top.joyer.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MidjourneyHttp {
    Map<Long,String> userIdtoTaskId=new HashMap<>();

    public String getRequest_key() {
        return request_key;
    }

    public void setRequest_key(String request_key) {
        this.request_key = request_key;
    }

    String request_key="";
    public Response newTask(String prompt, String state, Long userId) throws IOException{
        return newTask(new String[]{},prompt,state,userId);
    }

    public Response newTask(String[] base64Array, String prompt, String state, Long userId) throws IOException {
        JSONObject request=new JSONObject();
        request.put("base64Array",base64Array);
        request.put("notifyHook","");
        request.put("prompt",prompt);
        request.put("state",state);
        String respondString = HttpUtils.sendPostRequest("https://api.mctools.online/mj/submit/imagine", request.toJSONString(),request_key);//获得响应
        if (respondString != null) { //响应有效时
            Response response = JSON.parseObject(respondString, Response.class);
            userIdtoTaskId.put(userId, response.getTaskID());
            return response;
        }
        return null;
    }
    public Response secondTask(String action, int index, String notifyHook, String state, String taskId, Long userId) throws IOException {
        JSONObject request=new JSONObject();
        request.put("action",action);
        request.put("index",index);
        request.put("notifyHook",notifyHook);
        request.put("state",state);
        request.put("taskId",taskId);
        String respondString = HttpUtils.sendPostRequest("https://api.mctools.online/mj/submit/change", request.toJSONString(),request_key);//获得响应
        if (respondString != null) { //响应有效时
            Response response = JSON.parseObject(respondString, Response.class);
            userIdtoTaskId.put(userId, response.getTaskID());
            return response;
        }
        return null;
    }
    public Response secondTask(String action, int index, String notifyHook, String state, Long userId) throws IOException {
        if(userIdtoTaskId.get(userId)!=null)
            return secondTask(action,index,notifyHook,state,userIdtoTaskId.get(userId),userId);
        return null;
    }

    public ImgResponse getTaskResult(Long userId) throws IOException {
        if(userIdtoTaskId.get(userId)!=null){
            String respondString = HttpUtils.sendGetRequest("https://api.mctools.online/mj/task/"+userIdtoTaskId.get(userId)+"/fetch",request_key);//获得响应)
            if (respondString != null) { //响应有效时
                return JSON.parseObject(respondString, ImgResponse.class);
            }
        }
        return null;
    }
}
