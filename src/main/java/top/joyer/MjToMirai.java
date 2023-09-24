package top.joyer;


import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;
import top.joyer.BotUtils;
import top.joyer.Midjourney.ImgResponse;
import top.joyer.Midjourney.Midjourney;
import top.joyer.Midjourney.Response;
import top.joyer.MidjourneySupport;

import java.io.IOException;

public class MjToMirai {
    /**
     * 该方法仅限于Mirai使用，并且只能在触发指令时于已知的为聊天环境下的回复使用
     * 这是一个无可奈何为了方便维护的写法
     * @param response 响应
     * @param midjourney 当前的midjourney实例
     * @param context Command上下文
     */
    public static void replyImg(Response response, Midjourney midjourney, CommandContext context){
        long sendQQ=context.getSender().getUser().getId();
        //获取结果阶段
        if(response!=null&&(response.getCode()==1||response.getCode()==22)){ //判断请求是否成功,成功时轮询
            ImgResponse imgResponse=null;
            try {
                imgResponse=midjourney.pollImgResult(15000, response.getTaskID());
            } catch (InterruptedException e) {
                MidjourneySupport.INSTANCE.getLogger().error("TaskID:"+response.getTaskID()+" QQ:"+sendQQ +" 轮询线程错误："+e.getMessage());
            }catch (NullPointerException e){
                MidjourneySupport.INSTANCE.getLogger().error("TaskID:"+response.getTaskID()+" QQ:"+sendQQ +" 轮询结果错误："+e.getMessage());
            } catch (IOException e) {
                MidjourneySupport.INSTANCE.getLogger().error("TaskID:"+response.getTaskID()+" QQ:"+sendQQ +" 轮询网络错误："+e.getMessage());
            } catch (Exception e) {
                MidjourneySupport.INSTANCE.getLogger().error("TaskID:"+response.getTaskID()+" QQ:"+sendQQ +" 结果获取错误："+e.getMessage());
            }
            //处理结果阶段
            MessageChain new_img_messages = BotUtils.creatReplyMessageChine(context);
            if(imgResponse!=null){
                try {
                    byte[] bytes=midjourney.downloadImg(imgResponse);
                    Image img=context.getSender().getSubject().uploadImage(ExternalResource.create(bytes).toAutoCloseable());
                    new_img_messages=new_img_messages.plus("绘画完成，ID："+imgResponse.getId()).plus(img);
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
