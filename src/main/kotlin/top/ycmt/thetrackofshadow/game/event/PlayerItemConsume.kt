package top.ycmt.thetrackofshadow.game.event

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.platform.util.takeItem
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState

// 玩家消耗物品事件
object PlayerItemConsume {
    @SubscribeEvent
    fun onItemConsume(e: PlayerItemConsumeEvent) {
        val player = e.player
        // 获取玩家所在的游戏
        val playerGamePair = GameManager.getPlayerGame(player) ?: return
        val game = playerGamePair.first
        val playerType = playerGamePair.second
        // 如果不是玩家则跳出
        if (playerType != GameManager.PlayerType.PLAYER) {
            return
        }
        // 确保游戏处于运行阶段
        if (game.phaseState != PhaseState.RUNNING_PHASE) {
            return
        }
        // 被消耗的物品
        val item = e.item
        // 事件流程
        cleanBottle(e, player, item) // 清除使用完的药水瓶子
    }

    // 清除使用完的药水瓶子
    private fun cleanBottle(e: PlayerItemConsumeEvent, player: Player, item: ItemStack) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 判断使用的物品是否为药水
        if (item.type != Material.POTION) {
            return
        }
        submit(delay = 1L) {
            // 删除药水瓶子
            player.inventory.takeItem(1) {
                it.type == Material.GLASS_BOTTLE
            }
        }
    }

}