package top.joyer.Config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object WhitelistConfig : AutoSavePluginConfig("whitelist") {
    val group_whitelist: Boolean by value(false);
    val groups: List<Long> by value(listOf(12345678L,87654321L));
}