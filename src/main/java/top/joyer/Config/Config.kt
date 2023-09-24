package top.joyer.Config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    val bot_qq: Long by value(0L);
    val api_key: String by value("");
    val command_help: Boolean by value(true); // help指令的开关
}