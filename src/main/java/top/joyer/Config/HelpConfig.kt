package top.joyer.Config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

public object HelpConfig : AutoSavePluginConfig("config") {
    var help_text:String by value(
        """
            Bot指令列表：
            
            AI绘画：
            ◆ /image <关键词>  # 新的AI绘图
            ◆ /change v <数字1-4> <可选：绘图ID> # 从四宫格绘图中的第{数字}幅图为底生成新的绘图
            ◆ /change u <数字1-4> <可选：绘图ID> # 放大四宫格绘图中的第{数字}幅图
            ◆ /change r <可选：任务ID> # 重新绘制四宫格绘图
            
            在 image 指令中添加 --niji <数字1-5> 参数，数字越大二次元浓度越高
            在 change 指令中如果不填写绘图 ID ，默认为该用户上一次生成的四宫格绘图
            """.trimIndent()
    )
}