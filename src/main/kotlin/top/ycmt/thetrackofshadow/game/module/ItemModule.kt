package top.ycmt.thetrackofshadow.game.module

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.module.chat.impl.DefaultComponent
import taboolib.platform.util.buildItem
import taboolib.platform.util.modifyLore
import taboolib.platform.util.modifyMeta
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
            // 装备
            ItemType.EQUIP.ordinal -> modifyEquipItem(itemId, item)
            // 道具
            ItemType.PROP.ordinal -> modifyPropItem(itemId, item)
            // 药水
            ItemType.POTION.ordinal -> modifyPotionItem(itemId, item)
            // 宝石
            ItemType.GEM.ordinal -> modifyGemItem(itemId, item)
            // 食物
            ItemType.FOOD.ordinal -> modifyFoodItem(itemId, item)
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
            add("§6武器")
            add("§f攻击力: ${weaponData.attack}")
            add("§f耐久: ${weaponData.durability}")
        }
        return item
    }

    // 修改装备物品
    private fun modifyEquipItem(itemId: Int, item: ItemStack): ItemStack {
        // 获取装备配置表
        val equipData = Config.getEquipDataByItemId(itemId)
        if (equipData == null) {
            Logger.error("装备配置表为空, itemId: $itemId")
            return item
        }
        // 设置lore
        item.modifyLore {
            add("§6装备")
            add("§f防御力: ${equipData.defense}")
            add("§f耐久: ${equipData.durability}")
        }
        return item
    }

    // 修改道具物品
    private fun modifyPropItem(itemId: Int, item: ItemStack): ItemStack {
        // 获取道具配置表
        val propData = Config.getPropDataByItemId(itemId)
        if (propData == null) {
            Logger.error("道具配置表为空, itemId: $itemId")
            return item
        }
        // 设置lore
        item.modifyLore {
            add("§6道具")
        }
        return item
    }

    // 修改药水物品
    private fun modifyPotionItem(itemId: Int, item: ItemStack): ItemStack {
        // 获取药水配置表
        val potionData = Config.getPotionDataByItemId(itemId)
        if (potionData == null) {
            Logger.error("药水配置表为空, itemId: $itemId")
            return item
        }
        item.modifyMeta<PotionMeta> {
            // 确保药水效果配置不为空
            if (potionData.potionEffect1Type == null ||
                potionData.potionEffect1Duration == null ||
                potionData.potionEffect1Amplifier == null
            ) {
                return@modifyMeta
            }
            // 获取药水效果类型
            val potionEffectType = PotionEffectType.getByName(potionData.potionEffect1Type ?: "")
            // 确保药水效果不为空
            if (potionEffectType == null) {
                return@modifyMeta
            }
            addCustomEffect(
                PotionEffect(
                    potionEffectType,
                    potionData.potionEffect1Duration,
                    potionData.potionEffect1Amplifier
                ), true
            )
            color = potionEffectType.color
        }
        // 设置lore
        item.modifyLore {
            add("§6药水")
        }
        return item
    }

    // 修改宝石物品
    private fun modifyGemItem(itemId: Int, item: ItemStack): ItemStack {
        // 获取宝石配置表
        val gemData = Config.getGemDataByItemId(itemId)
        if (gemData == null) {
            Logger.error("宝石配置表为空, itemId: $itemId")
            return item
        }
        // 设置lore
        item.modifyLore {
            add("§6宝石")
        }
        return item
    }

    // 修改食物物品
    private fun modifyFoodItem(itemId: Int, item: ItemStack): ItemStack {
        // 设置lore
        item.modifyLore {
            add("§6食物")
        }
        return item
    }

}