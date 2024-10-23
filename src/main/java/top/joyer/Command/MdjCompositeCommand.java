package top.joyer.Command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.permission.Permission;
import org.jetbrains.annotations.NotNull;
import top.joyer.MidjourneySupport;

public class MdjCompositeCommand extends JCompositeCommand {
    public MdjCompositeCommand(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String[] secondaryNames, @NotNull Permission parentPermission) {
        super(owner, primaryName, secondaryNames, parentPermission);
    }
    public MdjCompositeCommand(){
        super(MidjourneySupport.INSTANCE,"mdj");
        setDescription("AI绘画插件的一些命令行指令");
    }
    @SubCommand("reload")
    public void reload(CommandContext context){
        //重新加载插件
        MidjourneySupport.INSTANCE.getLogger().info("开始插件重置");
        MidjourneySupport.INSTANCE.onEnable();
        MidjourneySupport.INSTANCE.getLogger().info("插件重置完成");
    }
}
