package top.joyer.Utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.message.data.*;
import top.joyer.Config.Config;
import top.joyer.Config.WhitelistConfig;
import top.joyer.MidjourneySupport;

import java.util.Objects;

public class BotUtils {
    private BotUtils(){}
    private static WhitelistConfig whitelistConfig=WhitelistConfig.INSTANCE;
    //public static Config config=Config.INSTANCE;
    public static boolean needReply(CommandContext context){
        //判断是否为聊天环境发送的指令
        if(context.getSender().getUser()!=null) {
            //判断接受指令的是否为配置的机器人
            if (Objects.requireNonNull(context.getSender().getBot()).getId() == Config.INSTANCE.getBot_qq()) {
                if(whitelistConfig.getGroup_whitelist()){//当开启白名单时
                    if(getMessageChainnKind(context.getOriginalMessage())==MessageKind.GROUP){//群组白名单判断
                        for(Long i:whitelistConfig.getGroups()){
                            if(context.getSender().getSubject().getId()==i){
                                return true;
                            }
                        }
                        MidjourneySupport.INSTANCE.getLogger().info("群组 "+context.getSender().getSubject().getId()+"判断为非白名单的群组,不予回复");
                        return false;
                    }
                }
                return true;
            }else{
                MidjourneySupport.INSTANCE.getLogger().info("群组 "+context.getSender().getSubject().getId()+"判断为非机器人"+Config.INSTANCE.getBot_qq()+"收到的指令,不予回复");
                return false;
            }
        }else{
            MidjourneySupport.INSTANCE.getLogger().info("群组 "+context.getSender().getSubject().getId()+"判断为非聊天环境触发的指令,不予回复");
            return false;
        }
    }
    public static MessageChain creatReplyMessageChine(CommandContext context){
        MessageChain new_messages = MessageUtils.newChain();
        long sendQQ= Objects.requireNonNull(context.getSender().getUser()).getId();
        //判断为群消息时添加At
        if (getMessageChainnKind(context.getOriginalMessage())==MessageKind.GROUP) {
            return new_messages.plus(new At(sendQQ)).plus(new PlainText(" "));//添加At
        }
        return new_messages;
    }
    /**
     * Mirai 2.14版本
     * 判断消息为群消息还是私聊消息
     * @param messages 要判断的MessageChain
     * @return 当MessageChain可判断时返回正常的 MessageKind 标记符
     */
    public static MessageKind getMessageChainnKind(MessageChain messages){

        String jsonString=MessageChain.serializeToJsonString(messages);
        JSONArray jsonArray=JSON.parseArray(jsonString);
        JSONObject json = jsonArray.getJSONObject(0); //获得MassageChain中的Source

        if(json.getString("kind").equals("FRIEND")){
            return MessageKind.FRIEND;
        }else if(json.getString("kind").equals("GROUP")){
            return MessageKind.GROUP;
        }else if(json.getString("kind").equals("TEMP")){
            return MessageKind.TEMP;
        }else if(json.getString("kind").equals("STRANGER")){
            return MessageKind.STRANGER;
        }
        return MessageKind.UNKNOW;
    }

    public enum MessageKind{
        GROUP,
        FRIEND,
        TEMP,
        STRANGER,
        UNKNOW
    }
}

