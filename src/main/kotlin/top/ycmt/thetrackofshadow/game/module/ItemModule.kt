package top.ycmt.thetrackofshadow.game.module

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.module.chat.impl.DefaultComponent
import taboolib.platform.util.buildItem
import taboolib.platform.util.modifyLore
import top.ycmt.thetrackofshadow.config.Config
import top.ycmt.thetrackofshadow.constant.ItemType
import top.ycmt.thetrackofshadow.constant.QualityType
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.logger.Logger

// 游戏物品管理模块
class ItemModule(private val game: Game) {

    // 创建物品
    fun createItem(itemId: Int): ItemStack? {
        // 获取物品配置表
        val itemData = Config.getItemDataByItemId(itemId)
        if (itemData == null) {
            Logger.error("物品配置表为空, itemId: $itemId")
            return null
        }
        // 获取物品材质
        val material = Material.getMaterial(itemData.material)
        if (material == null) {
            Logger.error("物品材质为空, material: ${itemData.material}")
            return null
        }
        // 通过品质获取名称颜色
        val qualityColor = when (itemData.qualityType) {
            // 传说
            QualityType.LEGENDARY.ordinal -> "ffd580,f7b32a"
            // 史诗
            QualityType.EPIC.ordinal -> "ff87ff,ff4dff"
            // 稀有
            QualityType.RATE.ordinal -> "aeaeff,8787ff"
            // 普通
            else -> "dbdbdb,cbcbcb"
        }
        val nameText = DefaultComponent()
            .append("<#${qualityColor}>${itemData.name}</#>".toGradientColor()).bold()
            .toLegacyText()
        // 构造物品
        var item = buildItem(material) {
            // 物品名称
            name = nameText
            // 自定义模型
            customModelData = itemData.modelId
        }
        // 根据物品类型的不同分开构造
        item = when (itemData.itemType) {
            // 武器
            ItemType.WEAPON.ordinal -> modifyWeaponItem(itemId, item)
            else -> item
        }
        // 设置物品介绍
        item.modifyLore {
            add("")
            add("<#888888,676767>-------------------</#>".toGradientColor())
            add("<#acabad>${itemData.introduction}</#>".toGradientColor())
            add("<#888888,676767>-------------------</#>".toGradientColor())
        }
        return item
    }

    // 修改武器物品
    private fun modifyWeaponItem(itemId: Int, item: ItemStack): ItemStack {
        // 获取武器配置表
        val weaponData = Config.getWeaponDataByItemId(itemId)
        if (weaponData == null) {
            Logger.error("武器配置表为空, itemId: $itemId")
            return item
        }
        // 设置lore
        item.modifyLore {
            add("§f攻击力: ${weaponData.attack}")
            add("§f耐久: ${weaponData.durability}")
        }
        return item
    }

}