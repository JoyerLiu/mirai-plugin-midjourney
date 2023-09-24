package top.joyer

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

public object HelpConfig : AutoSavePluginConfig("config") {
    var help_text:String by value(
        """
            Bot指令列表：
            AI绘画：
            /image <关键词>  # 新的AI绘画
            /change v <数字> <可选：任务ID> # 从第{数字}幅画为底生成
            /change u <数字> <可选：任务ID> # 放大第{数字}幅画
            /change r <可选：任务ID> - 重新绘图
            """.trimIndent()
    )
}