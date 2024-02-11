package top.ycmt.thetrackofshadow.config

import kotlinx.serialization.Serializable

// 物品配置表
@Serializable
data class ItemData(
    val firstName: String,
    val lastName: String,
)