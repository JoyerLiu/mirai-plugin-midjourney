package top.joyer.Config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    val bot_qq: Long by value(0L);
    val api_url: String by value("")
    val api_key: String by value("");
    val command_help: Boolean by value(true); // help指令的开关
    val retry_count:Int by value(5);//轮询重试次数
    val connect_timeout:Int by value(60000);
    val read_timeout:Int by value(600000);
}