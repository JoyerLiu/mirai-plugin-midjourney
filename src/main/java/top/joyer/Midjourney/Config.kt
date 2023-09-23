package top.joyer.Midjourney

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

public object Config : AutoSavePluginConfig("config") {
    val bot_qq: Long by value(0L);
    val api_key: String by value("");
    val console_help: Boolean by value(true);
}