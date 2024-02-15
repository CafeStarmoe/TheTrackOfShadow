package top.ycmt.thetrackofshadow.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 装备配置表
@Serializable
data class EquipData(
    @SerialName("物品ID") val itemId: Int,
    @SerialName("装备类型") val equipType: Int,
    @SerialName("数值用类型") val equipStrType: String,
    @SerialName("数值用名称") val nameStrType: String,
    @SerialName("防御力") val defense: Int,
    @SerialName("耐久") val durability: Int,
    @SerialName("[触发器]1操作") val trigger1Action: String?,
    @SerialName("[触发器]1参数") val trigger1Param: Int?,
    @SerialName("[触发器]2操作") val trigger2Action: String?,
    @SerialName("[触发器]2参数") val trigger2Param: Int?,
    @SerialName("[触发器]3操作") val trigger3Action: String?,
    @SerialName("[触发器]3参数") val trigger3Param: Int?,
    @SerialName("[触发器]4操作") val trigger4Action: String?,
    @SerialName("[触发器]4参数") val trigger4Param: Int?,
    @SerialName("[触发器]5操作") val trigger5Action: String?,
    @SerialName("[触发器]5参数") val trigger5Param: Int?,
)
