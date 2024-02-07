package top.ycmt.thetrackofshadow.pkg.chat

import taboolib.module.chat.toGradientColor

// 渐变色
object GradientColor {

    // 解析字符串渐变色模版
    fun String.toGradientColor(): String {
        // 最终结果
        val result = StringBuilder()
        // 正则表达式分割
        val splitRegex = Regex("<#.*?</#>|[^<#]+")
        val parts = splitRegex.findAll(this).map { it.value }.toList()
        for (part in parts) {
            // 正则表达式
            val regex = Regex("<#(\\w+(?:,\\w+)*)>(.*)</#>")
            // 匹配正则表达式
            val matchResult = regex.find(part)
            // 无法匹配到直接添加
            if (matchResult == null) {
                result.append(part)
                continue
            }
            // 颜色字符串列表
            val group1 = matchResult.groups[1]?.value ?: continue
            // 消息文本
            val group2 = matchResult.groups[2]?.value ?: continue

            // 颜色字符串列表转换
            val colorList = group1.trim().split(",").map { it.toInt(16) }

            // 消息文本应用渐变色
            result.append(group2.toGradientColor(colorList))
        }
        return result.toString()
    }

}
