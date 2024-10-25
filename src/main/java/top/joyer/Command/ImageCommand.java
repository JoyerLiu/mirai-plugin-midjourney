package top.joyer.Command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import top.joyer.Utils.BotUtils;
import top.joyer.Midjourney.*;
import top.joyer.MidjourneySupport;
import top.joyer.Midjourney.MjToMirai;

import java.io.IOException;
import java.util.Objects;

public class ImageCommand extends JRawCommand {
    private Midjourney midjourney;
    public ImageCommand(Midjourney midjourney){
        super(MidjourneySupport.INSTANCE,"image");
        this.midjourney=midjourney;
        setUsage("/image <关键词>");
        setDescription("新的绘画请求");
    }
    public ImageCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String[] secondaryNames, @NotNull Permission basePermission) {
        super(owner, primaryName, secondaryNames, basePermission);
    }

    @Override
    public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
        //判断是否需要回复
        if(BotUtils.needReply(context)){
            //回应
            long sendQQ = Objects.requireNonNull(context.getSender().getUser()).getId();
            MidjourneySupport.INSTANCE.getLogger().info("接收到来自 "+sendQQ+" 的新绘画请求:"+context.getOriginalMessage().contentToString());
            Response response=null;
            try {
                response = midjourney.newImage(messageHandle(args));
                if(response!=null){
                    MessageChain new_messages = BotUtils.creatReplyMessageChine(context);
                    if(response.getCode()==1||response.getCode()==22){
                        new_messages=new_messages.plus("绘画请求成功，ID："+response.getTaskID()+"，请耐心等待");
                    }else{
                        new_messages=new_messages.plus("绘画请求失败，原因："+response.getDescription());
                    }
                    context.getSender().sendMessage(new_messages);
                }else{
                    MidjourneySupport.INSTANCE.getLogger().error("在"+sendQQ+" 的新绘画请求中Response为Null");
                    context.getSender().sendMessage(BotUtils.creatReplyMessageChine(context).plus("请求发送失败"));
                }
            } catch (IOException e) {
                MidjourneySupport.INSTANCE.getLogger().error(" "+sendQQ+" 的绘画请求失败:"+e.getMessage());
                context.getSender().sendMessage(BotUtils.creatReplyMessageChine(context).plus("请求发送失败"));
            }
            //获取到绘画请求，回复消息
            MjToMirai.replyImg(response,midjourney,context);
        }

    }

    /**
     * 消息处理，将普通文本剥离出来
     * @param mc 发送的消息
     * @return 处理好的字符串
     */
    private String messageHandle(MessageChain mc){
        StringBuilder text= new StringBuilder(); //普通字符串
        //处理用户发送给机器人的信息
        for(SingleMessage element : mc){
            if(element instanceof PlainText){
                PlainText plainText= (PlainText) element;
                text.append(plainText.contentToString()).append(" "); //普通字符串加至此
            }
        }
        return text.substring(0,text.length()-1);
    }
}
