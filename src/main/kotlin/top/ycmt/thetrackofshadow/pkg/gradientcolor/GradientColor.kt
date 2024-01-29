package top.ycmt.thetrackofshadow.pkg.gradientcolor

import java.awt.Color

// 渐变色
class GradientColor {
    // 文本颜色设置
    private var colorSettings: MutableList<ColorSetting> = mutableListOf()

    constructor()
    constructor(vararg colorSettings: ColorSetting) {
        this.colorSettings = colorSettings.toMutableList()
    }

    // 追加文本颜色设置
    fun append(vararg colorSettings: ColorSetting) {
        this.colorSettings.addAll(colorSettings.toMutableList())
    }

    // 生成所有渐变色字符串并拼接
    fun generate(): String {
        val result = StringBuilder()

        for (textColorSetting in colorSettings) {
            result.append(parseColorText(textColorSetting))
        }

        return result.toString()
    }

    // 根据文本颜色设置生成渐变色字符串
    private fun parseColorText(colorSetting: ColorSetting): String {
        // 如果没有指定颜色那就直接返回文本
        if (colorSetting.colors.isEmpty()) {
            return colorSetting.text
        }

        // 加粗处理
        val boldColor = if (colorSetting.isBold) "§l" else ""
        // 倾斜处理
        val italicColor = if (colorSetting.isItalic) "§o" else ""
        // 下划线处理
        val underLineColor = if (colorSetting.isUnderLine) "§n" else ""
        // 删除线处理
        val strikethroughColor = if (colorSetting.isStrikethrough) "§m" else ""

        // 用于构建最终渐变色字符串
        val colorStringBuilder = StringBuilder()

        // 遍历输入的文本
        for (i in colorSetting.text.indices) {
            // 获取文本中的字符
            val char = colorSetting.text[i]
            // 计算当前字符在文本中的百分比位置
            val percentage = i.toFloat() / (colorSetting.text.length - 1)
            // 插值计算当前字符对应的颜色
            val currentColor = interpolateColors(colorSetting.colors, percentage)

            // 将带有颜色标记的字符追加到 StringBuilder 中
            colorStringBuilder.append("§$currentColor$boldColor$italicColor$underLineColor$strikethroughColor$char")
        }

        return colorStringBuilder.toString()
    }

    // 解析颜色
    private fun parseColor(colorString: String): Color {
        val hexColor = colorString.removePrefix("#")
        val rgb = hexColor.toLong(16).toInt()
        return Color(rgb)
    }

    // 插值计算颜色
    private fun interpolateColors(colors: List<String>, percentage: Float): String {
        require(colors.size >= 2) { "At least two colors are required." }

        // 计算颜色段的数量
        val colorCount = colors.size - 1
        val segmentPercentage = 1.0f / colorCount

        // 确定当前颜色段的索引
        val segmentIndex = (percentage / segmentPercentage).toInt().coerceIn(0, colorCount - 1)
        val segmentPercentageAdjusted = (percentage - segmentIndex * segmentPercentage) / segmentPercentage

        // 获取插值所需的相邻颜色
        val startColor = parseColor(colors[segmentIndex])
        val endColor = parseColor(colors[segmentIndex + 1])

        // 使用插值计算当前颜色
        val interpolatedColor = Color(
            (startColor.red + segmentPercentageAdjusted * (endColor.red - startColor.red)).toInt(),
            (startColor.green + segmentPercentageAdjusted * (endColor.green - startColor.green)).toInt(),
            (startColor.blue + segmentPercentageAdjusted * (endColor.blue - startColor.blue)).toInt()
        )

        // 将计算出的颜色转换为十六进制字符串
        return String.format("#%06X", 0xFFFFFF and interpolatedColor.rgb)
    }
}