package top.ycmt.thetrackofshadow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 药水配置表
@Serializable
data class PotionData(
    @SerialName("物品ID") val itemId: Int,
    @SerialName("药水类型") val potionType: Int,
    @SerialName("数值用类型") val potionStrType: String,
    @SerialName("数值用名称") val nameStrType: String,
    @SerialName("[药水效果]1类型") val potionEffect1Type: String?,
    @SerialName("[药水效果]1持续时间") val potionEffect1Duration: Int?,
    @SerialName("[药水效果]1强度") val potionEffect1Amplifier: Int?,
)
