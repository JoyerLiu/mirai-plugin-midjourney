package top.joyer.Midjourney;


import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;
import top.joyer.Utils.BotUtils;
import top.joyer.MidjourneySupport;

import java.io.IOException;
import java.util.Objects;

public class MjToMirai {
    /**
     * 该方法仅限于Mirai使用，并且只能在触发指令时于已知的为聊天环境下的回复使用
     * 这是一个无可奈何为了方便维护的写法
     * @param response 响应
     * @param midjourney 当前的midjourney实例
     * @param context Command上下文
     */
    public static void replyImg(Response response, Midjourney midjourney, CommandContext context){
        long sendQQ= Objects.requireNonNull(context.getSender().getUser()).getId();
        //获取结果阶段
        if(response!=null&&(response.getCode()==1||response.getCode()==22)){ //判断请求是否成功,成功时轮询
            ImgResponse imgResponse=null;
            imgResponse=midjourney.pollImgResult(15000, response.getTaskID());
            //处理结果阶段
            MessageChain new_img_messages = BotUtils.creatReplyMessageChine(context);
            if(imgResponse!=null){
                try {
                    byte[] bytes=midjourney.downloadImg(15000,imgResponse);
                    Image img= Objects.requireNonNull(context.getSender().getSubject()).uploadImage(ExternalResource.create(bytes).toAutoCloseable());
                    new_img_messages=new_img_messages.plus("绘画完成，ID："+imgResponse.getId()).plus(img);
                    if(!imgResponse.getAction().equals("UPSCALE")){
                        midjourney.putTaskID(sendQQ,imgResponse.getId()); //最后更新用户的最后生成的绘图ID
                    }
                }catch (IOException e) {
                    MidjourneySupport.INSTANCE.getLogger().error("TaskID:"+response.getTaskID()+" QQ:"+sendQQ +" 图片下载错误："+e.getMessage());
                    new_img_messages=new_img_messages.plus("绘画失败：内部错误");
                } catch (Exception e) {
                    MidjourneySupport.INSTANCE.getLogger().error("TaskID:"+response.getTaskID()+" QQ:"+sendQQ +" 任务失败错误："+e.getMessage());
                    new_img_messages=new_img_messages.plus("绘画失败："+e.getMessage());
                }
            }else{
                new_img_messages=new_img_messages.plus("绘画失败：内部错误");
            }
            context.getSender().sendMessage(new_img_messages); //发送图片消息
        }
    }
}
