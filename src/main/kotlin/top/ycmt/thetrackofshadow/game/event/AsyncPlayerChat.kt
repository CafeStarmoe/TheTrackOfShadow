package top.ycmt.thetrackofshadow.game.event

import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor

// 异步玩家聊天事件
object AsyncPlayerChat {
    @SubscribeEvent
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player
        // 获取玩家所在的游戏
        val playerGamePair = GameManager.getPlayerGame(player) ?: return
        val game = playerGamePair.first
        val playerType = playerGamePair.second
        // 事件流程
        lobbyChatFormat(e, game, playerType, player) // 大厅聊天格式
        settleChatFormat(e, game, playerType, player) // 结算阶段聊天格式
        playerChatFormat(e, game, playerType, player) // 玩家聊天格式
        watcherChatFormat(e, playerType, player) // 观察者聊天格式
    }

    // 大厅聊天格式
    private fun lobbyChatFormat(
        e: AsyncPlayerChatEvent,
        game: Game,
        playerType: GameManager.PlayerType,
        player: Player
    ) {
        // 确保游戏处于大厅等待阶段
        if (game.phaseState != PhaseState.LOBBY_PHASE) {
            return
        }
        // 如果不是玩家则跳出
        if (playerType != GameManager.PlayerType.PLAYER) {
            return
        }
        // 游戏等待中的格式
        e.format = "§7${player.name}: ${e.message}"
    }

    // 结算阶段聊天格式
    private fun settleChatFormat(
        e: AsyncPlayerChatEvent,
        game: Game,
        playerType: GameManager.PlayerType,
        player: Player
    ) {
        // 确保游戏处于结算阶段
        if (game.phaseState != PhaseState.SETTLE_PHASE) {
            return
        }
        // 如果不是玩家则跳出
        if (playerType != GameManager.PlayerType.PLAYER) {
            return
        }
        // 游戏等待中的格式
        e.format = "§7${player.name}: ${e.message}"
    }

    // 玩家聊天格式
    private fun playerChatFormat(
        e: AsyncPlayerChatEvent,
        game: Game,
        playerType: GameManager.PlayerType,
        player: Player
    ) {
        // 确保游戏处于运行阶段
        if (game.phaseState != PhaseState.RUNNING_PHASE) {
            return
        }
        // 如果不是玩家则跳出
        if (playerType != GameManager.PlayerType.PLAYER) {
            return
        }
        // 获取积分排行
        val scoreRank = game.scoreModule.getScoreRank()
        var playerRank = 0 // 玩家名次
        var playerScore = 0 // 玩家积分
        // 获取玩家是第几名
        for ((index, pair) in scoreRank.withIndex()) {
            // 判断这个是否是该玩家的排名
            if (pair.first != player.uniqueId) {
                continue
            }
            playerRank = index + 1
            playerScore = pair.second
        }
        // 玩家排名颜色
        val playerRankColor = when (playerRank) {
            1 -> "f7c79c,ef987d"
            2 -> "d3faff,aaf2fd"
            3 -> "deffd2,bee8ff"
            else -> "afafaf,a0a0a0"
        }
        // 根据排名修改聊天格式
        e.format = "§f[${playerScore}✫] <#${playerRankColor}>${player.name}</#>§f: ${e.message}".toGradientColor()
    }

    // 观察者聊天格式
    private fun watcherChatFormat(
        e: AsyncPlayerChatEvent,
        playerType: GameManager.PlayerType,
        player: Player
    ) {
        // 如果不是观察者则跳出
        if (playerType != GameManager.PlayerType.WATCHER) {
            return
        }
        // 观察者的格式
        e.format = "§7[观察者] ${player.name}: ${e.message}"
    }

}