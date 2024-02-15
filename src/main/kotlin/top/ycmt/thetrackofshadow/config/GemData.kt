package top.ycmt.thetrackofshadow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 宝石配置表
@Serializable
data class GemData(
    @SerialName("物品ID") val itemId: Int,
    @SerialName("宝石类型") val gemType: Int,
    @SerialName("数值用类型") val gemStrType: String,
    @SerialName("数值用名称") val nameStrType: String,
    @SerialName("属性参数") val attrParam: Double,
    @SerialName("[目标物品]1类型") val targetItem1Type: Int?,
    @SerialName("[目标物品]1参数") val targetItem1Param: Int?,
    @SerialName("[目标物品]2类型") val targetItem2Type: Int?,
    @SerialName("[目标物品]2参数") val targetItem2Param: Int?,
)
