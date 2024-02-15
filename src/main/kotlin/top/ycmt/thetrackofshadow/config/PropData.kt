package top.ycmt.thetrackofshadow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 道具配置表
@Serializable
data class PropData(
    @SerialName("物品ID") val itemId: Int,
    @SerialName("道具类型") val propType: Int,
    @SerialName("数值用类型") val propStrType: String,
    @SerialName("数值用名称") val nameStrType: String,
    @SerialName("[使用]1操作") val use1Action: String?,
    @SerialName("[使用]1参数") val use1Param: Int?,
)
