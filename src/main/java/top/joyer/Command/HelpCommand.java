package top.joyer.Command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import top.joyer.Utils.BotUtils;
import top.joyer.Config.HelpConfig;
import top.joyer.MidjourneySupport;

public class HelpCommand extends JSimpleCommand {
    HelpConfig helpConfig=HelpConfig.INSTANCE;
    public HelpCommand(){
        super(MidjourneySupport.INSTANCE,"?","？");
        setDescription("AI绘图的帮助指令");
    }
    public HelpCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String[] secondaryNames, @NotNull Permission basePermission) {
        super(owner, primaryName, secondaryNames, basePermission);
    }
    @Handler
    public void foo(CommandContext context) {
        //判断是否为聊天环境发送的指令
        if(BotUtils.needReply(context)){
            //消息回复
            MessageChain new_messages= BotUtils.creatReplyMessageChine(context);
            StringBuilder text= new StringBuilder();
            for(String i:helpConfig.getHelp_text()){ //格式化文档
                text.append(i).append("\n");
            }
            new_messages=new_messages.plus(new PlainText(text));//TODO：修改为可配置的help
            context.getSender().sendMessage(new_messages);
        }
    }
}
