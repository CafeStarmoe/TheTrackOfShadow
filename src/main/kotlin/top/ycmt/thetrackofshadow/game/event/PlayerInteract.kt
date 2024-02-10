package top.ycmt.thetrackofshadow.game.event

import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendFailMsg

// 玩家交互事件
object PlayerInteract {
    @SubscribeEvent
    fun onInteract(e: PlayerInteractEvent) {
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
        // 事件流程
        playerOpenChest(e, game, player) // 玩家打开宝箱
    }

    // 玩家打开宝箱
    private fun playerOpenChest(e: PlayerInteractEvent, game: Game, player: Player) {
        // 事件已取消则跳出
        if (e.useInteractedBlock() == Event.Result.DENY) {
            return
        }
        // 确保玩家交互对象是方块 并且是 右键交互
        if (!(e.hasBlock() && e.action == Action.RIGHT_CLICK_BLOCK)) {
            return
        }
        val chestBlock = e.clickedBlock ?: return
        // 判断交互的方块是否为箱子
        if (chestBlock.type != Material.CHEST) {
            return
        }
        // 判断该宝箱是否被找到
        val chestFoundUUID = game.chestModule.getChestFoundUUID(chestBlock.location)
        if (chestFoundUUID == null) {
            // 确保宝箱还未被找完
            if (game.chestModule.getRemainChestCount() <= 0) {
                player.sendFailMsg("无法打开, 宝箱已被探索完!")
                e.isCancelled = true
                return
            }
            // 确保玩家不存在取消打开新宝箱状态
            if (game.cancelModule.containsCancelState(player, CancelState.CANCEL_FIND_CHEST)) {
                player.sendFailMsg("你现在不能打开新宝箱!")
                e.isCancelled = true
                return
            }
            // 玩家找到宝箱
            game.chestModule.playerFindChest(player, chestBlock.state as Chest)
        } else if (chestFoundUUID != player.uniqueId) {
            // 如果宝箱不是同个玩家打开的那就提示
            player.sendFailMsg("你来晚啦, 此宝箱已被其他玩家打开!")
            e.isCancelled = true
            return
        }
    }

}