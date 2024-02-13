package top.ycmt.thetrackofshadow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 物品配置表
@Serializable
data class ItemData(
    @SerialName("物品ID") val itemId: Int,
    @SerialName("物品类型") val itemType: Int,
    @SerialName("数值用类型") val itemStrType: String,
    @SerialName("品质类型") val qualityType: Int,
    @SerialName("名称") val name: String,
    @SerialName("数值用名称") val nameStr: String,
    @SerialName("材质") val material: String,
    @SerialName("模型ID") val modelId: Int,
    @SerialName("介绍") val introduction: String,
    @SerialName("可出售") val sellable: Boolean,
    @SerialName("宝石槽数") val gemSlots: Boolean,
)
