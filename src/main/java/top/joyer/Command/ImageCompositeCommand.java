package top.joyer.Command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import top.joyer.BotUtils;
import top.joyer.Config.Config;
import top.joyer.Midjourney.Midjourney;
import top.joyer.Midjourney.Response;
import top.joyer.MidjourneySupport;
import top.joyer.MjToMirai;

import java.io.IOException;
import java.util.Objects;

public class ImageCompositeCommand extends JCompositeCommand {

    Midjourney midjourney;

    public ImageCompositeCommand( Midjourney midjourney) {
        super(MidjourneySupport.INSTANCE,"change");
        this.midjourney=midjourney;
        setDescription("对已有的绘画进行修改\n" +
                "/image u <数字> <可选：任务ID> # 放大四幅图中的某一幅图\n" +
                "/image v <数字> <可选：任务ID> # 以四幅图中的某一幅图为底再次进行绘图\n" +
                "/image r <可选：任务ID> # 放大四幅图中的某一幅图\n" +
                "例1：/image u 3 # 在上一幅生成的绘画中放大第3张图\n" +
                "例2：/image V 3 123456 # 以id为123456的绘图中选择第3张图为底再次进行绘图");
    }

    public ImageCompositeCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String[] secondaryNames, @NotNull Permission parentPermission) {
        super(owner, primaryName, secondaryNames, parentPermission);
    }

    @SubCommand("v")
    public void variation(CommandContext context, int index,String taskID) {
        //变换
        //判断是否为聊天环境发送的指令
        if(BotUtils.needReply(context)){
            //回应
            long sendQQ = Objects.requireNonNull(context.getSender().getUser()).getId();
            MidjourneySupport.INSTANCE.getLogger().info("接收到来自 "+sendQQ+" 的变换绘画请求:"+context.getOriginalMessage().contentToString());
            Response response;
            try {
                    response = midjourney.secondImage("VARIATION",index,taskID);
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
    @SubCommand("v")
    public void variation(CommandContext context, int index){
        variation(context,index, midjourney.getTaskID(Objects.requireNonNull(context.getSender().getUser()).getId()));
    }
    @SubCommand("u")
    public void upscale(CommandContext context, int index, String taskID) {
        //放大
        //判断是否为聊天环境发送的指令
        if(BotUtils.needReply(context)){
            //回应
            long sendQQ = Objects.requireNonNull(context.getSender().getUser()).getId();
            MidjourneySupport.INSTANCE.getLogger().info("接收到来自 "+sendQQ+" 的变换绘画请求:"+context.getOriginalMessage().contentToString());
            Response response;
            try {
                response = midjourney.secondImage("UPSCALE",index,taskID);
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
    @SubCommand("u")
    public void upscale(CommandContext context, int index){
        upscale(context,index, midjourney.getTaskID(Objects.requireNonNull(context.getSender().getUser()).getId()));
    }
    @SubCommand("r")
    public void reRoll(CommandContext context,String taskID) {
        //重新生成
        //判断是否为聊天环境发送的指令
        if(BotUtils.needReply(context)){
            //回应
            long sendQQ = Objects.requireNonNull(context.getSender().getUser()).getId();
            MidjourneySupport.INSTANCE.getLogger().info("接收到来自 "+sendQQ+" 的变换绘画请求:"+context.getOriginalMessage().contentToString());
            Response response;
            try {
                response = midjourney.secondImage("REROLL",0,taskID);
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
    @SubCommand("r")
    public void reRoll(CommandContext context){
        reRoll(context, midjourney.getTaskID(Objects.requireNonNull(context.getSender().getUser()).getId()));
    }

}
