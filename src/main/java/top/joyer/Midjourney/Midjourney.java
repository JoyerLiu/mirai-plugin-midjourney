package top.joyer.Midjourney;

import top.joyer.Utils.HttpUtils;
import top.joyer.MidjourneySupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Midjourney {
    MidjourneyHttp midjourneyHttp;
    Map<Long,String> userIdtoTaskId=new HashMap<>();


    public Midjourney(String api_url,String api_key){
        midjourneyHttp=new MidjourneyHttp(api_url,api_key);
    }
    public String getApi_url() {
        return midjourneyHttp.getApi_url();
    }

    public void setApi_url(String api_url) {
        midjourneyHttp.setApi_url(api_url);
    }

    public String getApi_key() {
        return midjourneyHttp.getApi_key();
    }

    public void setApi_key(String api_key) {
        midjourneyHttp.setApi_key(api_key);
    }

    /**
     * 新建绘图任务
     * @param base64Array 垫图
     * @param prompt 关键词
     * @param state 额外参数
     * @return 响应
     * @throws IOException 网络错误
     */
    public Response newImage(String[] base64Array, String prompt, String state) throws IOException {
        return midjourneyHttp.newTask(base64Array,prompt,state);
    }

    /**
     * 新建绘图任务
     * @param prompt 关键词
     * @return 响应
     * @throws IOException 网络错误
     */
    public Response newImage(String prompt) throws IOException {
        return newImage(new String[]{},prompt,"");
    }

    /**
     * 二次绘图任务
     * @param action 动作，可选动作(UPSCALE(放大); VARIATION(变换); REROLL(重新生成))
     * @param index 上一张图的序号，action为 UPSCALE,VARIATION 时必须
     * @param notifyHook 回调地址, 为空时使用全局notifyHook
     * @param state 自定义参数
     * @param taskId 上一张图的任务ID
     * @return 响应
     */
    public Response secondImage(String action, int index, String notifyHook, String state, String taskId) throws IOException {
        return midjourneyHttp.secondTask(action, index, notifyHook, state, taskId);
    }

    /**
     * 二次绘图任务
     * @param action 动作，可选动作(UPSCALE(放大); VARIATION(变换); REROLL(重新生成))
     * @param index 上一张图的序号，action为 UPSCALE,VARIATION 时必须
     * @param taskId 上一张图的任务ID
     * @return 响应
     */
    public Response secondImage(String action, int index,String taskId) throws IOException {
        return midjourneyHttp.secondTask(action, index, "", "", taskId);
    }

    /**
     * 二次绘图任务
     * @param action 动作，可选动作(UPSCALE(放大); VARIATION(变换); REROLL(重新生成))
     * @param index 上一张图的序号，action为 UPSCALE,VARIATION 时必须
     * @param user_id 用户QQ
     * @return 响应
     */
    public Response secondImage(String action, int index,long user_id) throws IOException,NullPointerException {
        if(userIdtoTaskId!=null && userIdtoTaskId.containsKey(user_id)){
            return secondImage(action, index, "", "", userIdtoTaskId.get(user_id));
        }
        throw new NullPointerException("找不到用户对应的taskId");
    }

    /**
     * 从服务器获取当次任务的图片
     * @param taskID 当次任务的ID
     * @return 图片响应
     */
    public ImgResponse getImage(String taskID) throws IOException {
        return midjourneyHttp.getTaskResult(taskID);
    }

    /**
     * 轮询绘画结果
     * @param sleep_time 单次轮询时间
     * @param taskID 任务ID
     * @return 有效的ImgResponse
     * @throws InterruptedException 线程休眠可能产生的线程错误
     * @throws IOException API访问可能导致的网络错误
     * @throws NullPointerException Response为空的错误
     */
    public ImgResponse pollImgResult(long sleep_time,String taskID) throws InterruptedException, IOException,NullPointerException {
        ImgResponse imgResponse;
        //轮询结果
        while(true){
            Thread.sleep(sleep_time);
            imgResponse = getImage(taskID);//查询结果
            if(imgResponse !=null){
                MidjourneySupport.INSTANCE.getLogger().info("Midjourney Support在"+taskID+"轮询得到回应:\n"+ imgResponse); //测试，可能删除
                if (imgResponse.getStatus().equals("FAILURE") || imgResponse.getStatus().equals("SUCCESS")){
                    break;
                }
            }else{
                throw new NullPointerException("respond为null");
            }
        }
        return imgResponse;
    }

    /**
     * 轮询绘画结果
     * @param sleep_time 单次轮询时间
     * @param userId 用户QQ
     * @return 有效的ImgResponse
     * @throws InterruptedException 线程休眠可能产生的线程错误
     * @throws IOException API访问可能导致的网络错误
     * @throws NullPointerException Response为空的错误
     */
    public ImgResponse pollImgResult(long sleep_time,long userId) throws InterruptedException, IOException,NullPointerException {
        if(userIdtoTaskId.containsKey(userId)){
            return pollImgResult(sleep_time,userIdtoTaskId.get(userId));
        }else{
            throw new NullPointerException("未找到用户"+userId+"对应的TaskId");
        }
    }

    /**
     * 下载结果图像
     * @param imgResponse API返回的绘图结果
     * @return bytes缓存流形式的图片
     * @throws IOException 下载图片可能产生的网络错误
     * @throws Exception 任务失败导致的错误
     */
    public byte[] downloadImg(ImgResponse imgResponse) throws IOException,Exception{
        if(imgResponse.getStatus().equals("SUCCESS")){
            //成功
            return HttpUtils.getImgToURL(imgResponse.getImageUrl());
        }else{
            //失败
            throw new Exception(imgResponse.getStatus()+":"+imgResponse.getFailReason());
        }
    }

    public String getTaskID(long userId){
        return userIdtoTaskId.get(userId);
    }
    public void putTaskID(long userId,String taskId){
        userIdtoTaskId.put(userId,taskId);
    }
}
