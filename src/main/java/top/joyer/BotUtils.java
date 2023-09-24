package top.joyer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.message.data.*;

public class BotUtils {
    private BotUtils(){}
    public static Config config=Config.INSTANCE;
    public static boolean needReply(CommandContext context){
        //判断是否为聊天环境发送的指令
        if(context.getSender().getUser()!=null) {
            //判断接受指令的是否为配置的机器人
            if (context.getSender().getBot().getId() == Config.INSTANCE.getBot_qq()) {
                //打开了At机器人时,在群中没有At机器人都不响应
//                if (config.getAt_command()
//                        && getMessageChainnKind(context.getOriginalMessage())==MessageKind.GROUP/*context.getOriginalMessage().get(MessageSource.Key).getKind()==MessageSourceKind.GROUP 2.15 版本可用*/) {
//                    At at = (At) context.getOriginalMessage().get(At.Key);
//                    if (at == null) {
//                        return false;
//                    }
//                    if (at.getTarget() != config.getBot_qq()) {
//                        return false;
//                    }
//                }
                return true;
            }
        }
        return false;
    }
    public static MessageChain creatReplyMessageChine(CommandContext context){
        MessageChain new_messages = MessageUtils.newChain();
        long sendQQ=context.getSender().getUser().getId();
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

