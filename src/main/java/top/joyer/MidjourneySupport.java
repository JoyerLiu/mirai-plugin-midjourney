package top.joyer;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import org.jetbrains.annotations.NotNull;
import top.joyer.Command.HelpCommand;
import top.joyer.Command.ImageCommand;
import top.joyer.Command.ImageCompositeCommand;
import top.joyer.Command.MdjCompositeCommand;
import top.joyer.Config.Config;
import top.joyer.Config.HelpConfig;
import top.joyer.Config.WhitelistConfig;
import top.joyer.Midjourney.*;


public final class MidjourneySupport extends JavaPlugin {
    public static final MidjourneySupport INSTANCE = new MidjourneySupport();

    Midjourney midjourney;
    Config config; //配置文件
    HelpConfig helpConfig;

    WhitelistConfig whitelistConfig;

    private MidjourneySupport() {
        super(new JvmPluginDescriptionBuilder("top.joyer.mirai-plugin-midjourney", "0.1.6")
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
        //加载配置文件
        if(configInit()){
            //加载实例
            midjourney=new Midjourney(config.getApi_url(),config.getApi_key());
            midjourney.setRetry_count(config.getRetry_count());
        }

        //注册指令
        commandInit();

        getLogger().info("Midjourney Support 插件加载完成!");
    }
    /**
     * 指令初始化
     */
    private void commandInit(){
        //TODO:注册指令
        if(config.getCommand_help())
            CommandManager.INSTANCE.registerCommand(new HelpCommand(),true);
        CommandManager.INSTANCE.registerCommand(new ImageCommand(midjourney),true);
        CommandManager.INSTANCE.registerCommand(new ImageCompositeCommand(midjourney),true);
        CommandManager.INSTANCE.registerCommand(new MdjCompositeCommand(),true);
    }

    /**
     * 配置文件初始化
     */
    private Boolean configInit(){
        boolean success=true;
        config=Config.INSTANCE;
        helpConfig=HelpConfig.INSTANCE;
        whitelistConfig=WhitelistConfig.INSTANCE;
        reloadPluginConfig(helpConfig);
        reloadPluginConfig(config);
        reloadPluginConfig(whitelistConfig);
        if(config.getApi_url().isEmpty()){
            getLogger().error("未填写API-KEY，请在配置文件中填写");
            success=false;
        }
        if(config.getApi_key().isEmpty()){
            getLogger().error("未填写API-KEY，请在配置文件中填写");
            success=false;
        }
        if(config.getBot_qq()==0L){
            getLogger().error("未填写Bot QQ，请在配置文件中填写");
            success=false;
        }
        return success;
    }
//    Listener listener=GlobalEventChannel.INSTANCE.parentScope(this).subscribeAlways(GroupMessageEvent.class, event->{
//
//    });
}