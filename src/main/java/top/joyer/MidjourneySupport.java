package top.joyer;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;
import top.joyer.Midjourney.Config;
import top.joyer.Midjourney.ImgResponse;
import top.joyer.Midjourney.MidjourneyHttp;
import top.joyer.Midjourney.Response;

import java.io.IOException;


public final class MidjourneySupport extends JavaPlugin {
    public static final MidjourneySupport INSTANCE = new MidjourneySupport();
    private long bot_account;
    private MidjourneyHttp midjourneyHttp;
    Listener<GroupMessageEvent> grouplistener;
    Listener<FriendMessageEvent> friendlistener;
    Config config; //配置文件

    private MidjourneySupport() {
        super(new JvmPluginDescriptionBuilder("top.joyer.mirai-plugin-midjourney", "0.1.0")
                .name("mirai-plugin-midjourney")
                .author("Joyer")
                .build());
    }
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        super.onLoad($this$onLoad);

    }

    @Override
    public void onEnable() {
        //加载实例
        midjourneyHttp=new MidjourneyHttp();
        config=Config.INSTANCE;
        //加载配置文件
        reloadPluginConfig(config);
        bot_account =config.getBot_qq();
        midjourneyHttp.setRequest_key(config.getApi_key());
        if(config.getApi_key().isEmpty()){
            getLogger().error("未填写API-KEY，请在配置文件中填写");
        }
        if(config.getBot_qq()==0L){
            getLogger().error("未填写Bot QQ，请在配置文件中填写");
        }
        //加载事件监听
        grouplistener= GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event ->{
            //开启单个用户的线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取群消息
                    MessageChain messages=event.getMessage();

                    //Bot被呼叫时的处理
                    if(isCalled(messages)){
                        botReply(event.getGroup().getId(),event.getSender().getId(),messageHandling(messages),MessageFrom.GROUP);
                    }
                }
            }).start();
        });
        friendlistener= GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, event ->{
            //开启单个用户的线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取私聊消息
                    MessageChain messages=event.getMessage();
                    //获取呼叫机器人的账号
                    long call_account=event.getFriend().getId();
                    //私聊不需要判定是否被At
                    botReply(call_account,call_account,messageHandling(messages),MessageFrom.FRIEND);
                }
            }).start();
        });

        getLogger().info("Midjourney Support 插件加载完成!");
    }

    /**
     * 消息处理，将普通文本剥离出来
     * @param mc 发送的消息
     * @return 处理好的字符串
     */
    private String messageHandling(MessageChain mc){
        StringBuilder text= new StringBuilder(); //普通字符串
        //处理用户发送给机器人的信息
        for(SingleMessage element : mc){
            if(element instanceof PlainText){
                PlainText plainText= (PlainText) element;
                text.append(plainText.contentToString()); //普通字符串加至此
            }
        }
        return text.toString();
    }

    /**
     * 判定机器人是否被呼叫，即是否被At
     * @param mc
     * @return
     */
    private boolean isCalled(MessageChain mc){
        for(SingleMessage element : mc){
            if(element instanceof At){
                At at= (At) element;
                if(new Long(at.getTarget()).equals(bot_account)){
                    return true; //当Bot被At时标记为被呼叫
                }
            }
        }
        return false;
    }

    /**
     * 机器人回复
     * @param reply_account 机器人要回复的账号，可以是群号或是qq号
     * @param call_account 呼叫机器人的账号
     * @param text 机器人被呼叫时附带的普通文本
     * @param messageFrom 机器人回应是在群中还是在私聊中
     */
    private void botReply(long reply_account, long call_account, String text, MessageFrom messageFrom){
        //指令处理
        int head_index=-1;
        getLogger().info("Midjourney Support 收到来自"+call_account+"的信息:"+text);

        if(config.getConsole_help()) {
            head_index = text.indexOf("/help");
            if (head_index != -1) {
                //发送help
                MessageChain messages = MessageUtils.newChain(new At(call_account), new PlainText(
                        "Bot指令列表：\n" +
                                "AI绘画：\n" +
                                "/image {关键词}  - 新的AI绘画\n" +
                                "/image -v {数字} - 从第{数字}幅画为底生成\n" +
                                "/image -u {数字} - 放大第{数字}幅画\n" +
                                "/image -r - 重新绘图"));
                sendMessage(messageFrom, reply_account, messages); //发送帮助文档

                return;
            }
        }
        head_index=text.indexOf("/image");
        //指令识别
        if(head_index!=-1){
            //处理指令块
            String[] words=text.substring(head_index+7).split(" ");
            //判断指令块
            Response response=null;
            try{
                getLogger().info("Midjourney Support 接收来自"+call_account+"的新绘图指令："+text.substring(head_index));
                if (words[0].equals("-r")){
                    //重新生成指令
                    response=midjourneyHttp.secondTask("REROLL",0,"","",call_account);
                }else if(words[0].equals("-u")) {
                    //放大指令
                    int img_index = Integer.parseInt(words[1]);//获得index
                    if (img_index > 0 && img_index < 5) {
                        response=midjourneyHttp.secondTask("UPSCALE", img_index, "", "", call_account);
                    }else{
                        throw new NumberFormatException("输入的指令中的数字错误");
                    }
                }else if(words[0].equals("-v")){
                    //变换指令
                    int img_index = Integer.parseInt(words[1]);//获得index
                    if (img_index > 0 && img_index < 5) {
                        response=midjourneyHttp.secondTask("VARIATION", img_index, "", "", call_account);
                    }else{
                        throw new NumberFormatException("输入的指令中的数字错误");
                    }
                }else{
                    //新绘图指令
                    String prompt=text.substring(head_index+7); //TODO：测试
                    response =midjourneyHttp.newTask(prompt,"",call_account);
                }
                //判断请求的response是否成功
                if(response!=null){
                    getLogger().info("Midjourney Support 在"+call_account+"的请求得到回应:\n"+ response.toString()); //TODO:测试
                    if(response.getCode()==1|| response.getCode()==22){
                        sendMessage(messageFrom,reply_account,MessageUtils.newChain(new At(call_account),new PlainText(" 绘图请求已经发送成功，请耐心等候"))); //向群中发送等候
                        //接收轮询
                        ImgResponse imgResponse =null;
                        while(true){ //轮询结果
                            try {
                                Thread.sleep(15000);
                                imgResponse = midjourneyHttp.getTaskResult(call_account);
                                if(imgResponse !=null){
                                    getLogger().info("Midjourney Support在"+call_account+"轮询得到回应:\n"+ imgResponse.toString()); //TODO:测试
                                    if (imgResponse.getStatus().equals("FAILURE") || imgResponse.getStatus().equals("SUCCESS")){
                                        break;
                                    }
                                }else{
                                    throw new NullPointerException("respond为null");
                                }
                            } catch (Exception e) {
                                getLogger().error("Midjourney Support在轮询结果时发生错误：\n"+e.getMessage());
                                break;
                            }
                        }
                        //处理结果
                        if(imgResponse.getStatus().equals("SUCCESS")){
                            //成功

                            try {
                                Image image=null;
                                if(messageFrom==MessageFrom.GROUP){
                                    image = Bot.getInstance(bot_account).getGroup(reply_account)
                                            .uploadImage(ExternalResource.create(HttpUtils.getImgToURL(imgResponse.getImageUrl())).toAutoCloseable()); //发送图片后关闭
                                }else if(messageFrom==MessageFrom.FRIEND){
                                    image = Bot.getInstance(bot_account).getFriend(reply_account)
                                            .uploadImage(ExternalResource.create(HttpUtils.getImgToURL(imgResponse.getImageUrl())).toAutoCloseable()); //发送图片后关闭
                                }
                                sendMessage(messageFrom,reply_account,MessageUtils.newChain(new At(call_account),new PlainText(" 图片生成成功"),image));
                            } catch (IOException e) {
                                sendMessage(messageFrom,reply_account,MessageUtils.newChain(new At(call_account),new PlainText(" 你的请求生成失败，失败原因：").plus("内部错误"))); //向群中发送失败原因
                                throw new RuntimeException(e);
                            }

                        }else{
                            //失败
                            sendMessage(messageFrom,reply_account,MessageUtils.newChain(new At(call_account),new PlainText(" 你的请求生成失败，失败原因：").plus(imgResponse.getDescription()))); //向群中发送失败原因
                        }
                    }
                }else{
                    throw new NullPointerException("response is Null");
                }
            }catch (NumberFormatException e){
                getLogger().error("Midjourney Support 在请求"+call_account+"的任务时发生错误：\n"+e.getClass().getName()+":"+e.getMessage());
                sendMessage(messageFrom,reply_account,MessageUtils.newChain(new At(call_account),new PlainText("指令解析错误，请检查你的指令"))); //向群中发送等候
            }
            catch (Exception e){
                getLogger().error("Midjourney Support 在请求"+call_account+"的任务时发生错误：\n"+e.getClass().getName()+":"+e.getMessage());
                sendMessage(messageFrom,reply_account,MessageUtils.newChain(new At(call_account),new PlainText(" 请求发生错误，请稍后再试"))); //向群中发送等候
            }

        }
    }

    private void sendMessage(MessageFrom messageFrom,long sendId,MessageChain messageChain){
        if(messageFrom==MessageFrom.GROUP){
            Bot.getInstance(bot_account).getGroup(sendId).sendMessage(messageChain);
        }else if(messageFrom==MessageFrom.FRIEND){
            Bot.getInstance(bot_account).getFriend(sendId).sendMessage(messageChain);
        }
    }

    public enum MessageFrom{
        GROUP,
        FRIEND
    }
}