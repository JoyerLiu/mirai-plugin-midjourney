package top.joyer.Command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import top.joyer.BotUtils;
import top.joyer.Config;
import top.joyer.HelpConfig;
import top.joyer.MidjourneySupport;

public class HelpCommand extends JSimpleCommand {

    Config config=Config.INSTANCE;
    HelpConfig helpConfig=HelpConfig.INSTANCE;
    public HelpCommand(){
        super(MidjourneySupport.INSTANCE,"?","？");
    }
    public HelpCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String[] secondaryNames, @NotNull Permission basePermission) {
        super(owner, primaryName, secondaryNames, basePermission);
    }
    @Handler
    public void foo(CommandContext context) {
        //判断是否为聊天环境发送的指令
        if(BotUtils.needReply(context)){
            //消息回复
            long sendQQ=context.getSender().getUser().getId();
            MessageChain new_messages= BotUtils.creatReplyMessageChine(context);
            new_messages=new_messages.plus(new PlainText(helpConfig.getHelp_text()));//TODO：修改为可配置的help
            context.getSender().sendMessage(new_messages);
        }
    }
}
