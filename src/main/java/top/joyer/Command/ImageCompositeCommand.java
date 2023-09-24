package top.joyer.Command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import top.joyer.BotUtils;
import top.joyer.Config;
import top.joyer.Midjourney.Midjourney;
import top.joyer.Midjourney.Response;
import top.joyer.MidjourneySupport;
import top.joyer.MjToMirai;

import java.io.IOException;

public class ImageCompositeCommand extends JCompositeCommand {

    Midjourney midjourney;
    Config config=Config.INSTANCE;

    public ImageCompositeCommand( Midjourney midjourney) {
        super(MidjourneySupport.INSTANCE,"change");
        this.midjourney=midjourney;
    }

    public ImageCompositeCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String[] secondaryNames, @NotNull Permission parentPermission) {
        super(owner, primaryName, secondaryNames, parentPermission);
    }

    @SubCommand("v")
    public void variation(CommandContext context, int index, String taskID) {
        //变换
        //判断是否为聊天环境发送的指令
        if(BotUtils.needReply(context)){
            //回应
            long sendQQ = context.getSender().getUser().getId();
            MidjourneySupport.INSTANCE.getLogger().info("接收到来自 "+sendQQ+" 的变换绘画请求:"+context.getOriginalMessage().contentToString());
            Response response=null;
            try {
                //判断是否有指定task
                if(taskID.isEmpty())
                    response = midjourney.secondImage("VARIATION",index,taskID,sendQQ);
                else
                    response = midjourney.secondImage("VARIATION",index,sendQQ);

                if(response!=null){
                    MessageChain new_messages = BotUtils.creatReplyMessageChine(context);
                    if(response.getCode()==1||response.getCode()==22){
                        new_messages=new_messages.plus("成功发送变换绘画请求，ID："+response.getTaskID()+"，请耐心等待");
                    }else{
                        new_messages=new_messages.plus("请求发送失败，原因："+response.getDescription());
                    }
                    context.getSender().sendMessage(new_messages);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //获取到绘画请求，回复消息
            MjToMirai.replyImg(response,midjourney,context);
        }
    }

    @SubCommand("u")
    public void upscale(CommandContext context, int index, String taskID) {
        //放大
        //判断是否为聊天环境发送的指令
        if(BotUtils.needReply(context)){
            //回应
            long sendQQ = context.getSender().getUser().getId();
            MidjourneySupport.INSTANCE.getLogger().info("接收到来自 "+sendQQ+" 的变换绘画请求:"+context.getOriginalMessage().contentToString());
            Response response=null;
            try {
                //判断是否有指定task
                if(taskID.isEmpty())
                    response = midjourney.secondImage("UPSCALE",index,taskID,sendQQ);
                else
                    response = midjourney.secondImage("UPSCALE",index,sendQQ);

                if(response!=null){
                    MessageChain new_messages = BotUtils.creatReplyMessageChine(context);
                    if(response.getCode()==1||response.getCode()==22){
                        new_messages=new_messages.plus("成功发送放大绘画请求，ID："+response.getTaskID()+"，请耐心等待");
                    }else{
                        new_messages=new_messages.plus("请求发送失败，原因："+response.getDescription());
                    }
                    context.getSender().sendMessage(new_messages);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //获取到绘画请求，回复消息
            MjToMirai.replyImg(response,midjourney,context);
        }
    }


    @SubCommand("r")
    public void upscale(CommandContext context,String taskID) {
        //重新生成
        //判断是否为聊天环境发送的指令
        if(BotUtils.needReply(context)){
            //回应
            long sendQQ = context.getSender().getUser().getId();
            MidjourneySupport.INSTANCE.getLogger().info("接收到来自 "+sendQQ+" 的变换绘画请求:"+context.getOriginalMessage().contentToString());
            Response response=null;
            try {
                //判断是否有指定task
                if(taskID.isEmpty())
                    response = midjourney.secondImage("UPSCALE",0,taskID,sendQQ);
                else
                    response = midjourney.secondImage("UPSCALE",0,sendQQ);

                if(response!=null){
                    MessageChain new_messages = BotUtils.creatReplyMessageChine(context);
                    if(response.getCode()==1||response.getCode()==22){
                        new_messages=new_messages.plus("成功发送重新生成绘画请求，ID："+response.getTaskID()+"，请耐心等待");
                    }else{
                        new_messages=new_messages.plus("请求发送失败，原因："+response.getDescription());
                    }
                    context.getSender().sendMessage(new_messages);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //获取到绘画请求，回复消息
            MjToMirai.replyImg(response,midjourney,context);
        }
    }


}
