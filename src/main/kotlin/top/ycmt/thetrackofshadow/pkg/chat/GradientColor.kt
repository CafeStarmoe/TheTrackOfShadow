package top.ycmt.thetrackofshadow.pkg.chat

import taboolib.module.chat.toGradientColor

// 渐变色
object GradientColor {

    // 解析字符串渐变色模版
    fun String.toGradientColor(): String {
        // 存储最终结果的字符串构建器
        val result = StringBuilder()
        // 上一次匹配的结束位置
        var lastIndex = 0
        // 渐变色模板的正则表达式
        val regex = Regex("<#(\\w+(?:,\\w+)*)>(.*?)</#>")
        // 遍历所有匹配结果
        regex.findAll(this).forEach { matchResult ->
            // 当前匹配结果的起始位置
            val startIndex = matchResult.range.first
            // 如果起始位置在上一次匹配结束位置之后
            if (startIndex > lastIndex) {
                // 添加未匹配部分到结果中
                result.append(this.substring(lastIndex, startIndex))
            }
            // 获取渐变色的颜色列表
            val colorList = matchResult.groupValues[1].trim().split(",").map { it.toInt(16) }
            // 获取渐变色文本内容
            val text = matchResult.groupValues[2]
            // 将渐变色应用到文本内容上，并添加到结果中
            result.append(text.toGradientColor(colorList))
            // 更新上一次匹配结束位置
            lastIndex = matchResult.range.last + 1
        }
        // 如果最后一个匹配之后还有未处理的部分
        if (lastIndex < this.length) {
            // 添加剩余部分到结果中
            result.append(this.substring(lastIndex))
        }
        // 返回最终结果
        return result.toString()
    }


}
