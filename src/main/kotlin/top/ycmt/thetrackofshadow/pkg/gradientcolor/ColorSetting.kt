package top.ycmt.thetrackofshadow.pkg.gradientcolor

// 文本颜色设置数据
data class ColorSetting(
    // 文本
    val text: String,
    // 颜色
    val colors: List<String> = listOf(),
    // 是否加粗
    val isBold: Boolean = false,
    // 是否倾斜
    val isItalic: Boolean = false,
    // 是否添加下划线
    val isUnderLine: Boolean = false,
    // 是否添加删除线
    val isStrikethrough: Boolean = false,
)