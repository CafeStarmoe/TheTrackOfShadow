package top.ycmt.thetrackofshadow.game.phase.impl

import org.bukkit.Sound
import org.bukkit.entity.Player
import taboolib.module.chat.toGradientColor
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.phase.PhaseAbstract

// 大厅等待阶段
class LobbyPhase(override val game: Game) : PhaseAbstract() {
    // 剩余等待的时间
    private var waitTick: Int = game.gameSetting.lobbyWaitTick

    override fun onTick() {
        val onlinePlayerList = game.playerModule.getOnlinePlayerList()

        // 校验玩家人数是否符合开始游戏所需最小玩家数
        if (onlinePlayerList.size < game.gameSetting.minPlayerCount) {
            // 重置剩余等待时间并提示玩家
            if (waitTick != game.gameSetting.lobbyWaitTick) {
                waitTick = game.gameSetting.lobbyWaitTick
                // 提示玩家
                notifyWaitMorePlayer(onlinePlayerList)
            }
            return
        }

        // 根据剩余时间执行
        when (waitTick) {
            in 1..5, 10 -> notifyCountdown(onlinePlayerList)
            0 -> {
                // 剩余等待时间结束 此阶段完成
                this.Done()
                return
            }

            else -> {
                // 不是指定的时间每10秒提示一次
                if (waitTick % 10 == 0) {
                    notifyCountdown(onlinePlayerList)
                }
            }
        }
        waitTick--
    }

    // 提示玩家等待更多玩家
    private fun notifyWaitMorePlayer(onlinePlayerList: List<Player>) {
        game.playSound(onlinePlayerList, Sound.BLOCK_NOTE_BLOCK_HAT)
        game.sendTitle(
            onlinePlayerList,
            "等待更多玩家加入...".toGradientColor(listOf(0xffcbcb, 0xff7093)),
            "",
            5,
            30,
            5
        )
        game.sendMessage(
            onlinePlayerList,
            "玩家数量不足, 等待更多玩家...".toGradientColor(
                listOf(0xffcbcb, 0xff7093)
            )
        )
    }

    // 提示倒计时
    private fun notifyCountdown(onlinePlayerList: List<Player>) {
        // 倒计时颜色
        val countdownColor = when (waitTick) {
            10 -> listOf(0xf7c79c, 0xef987d)
            in 1..5 -> listOf(0xff9c9c, 0xde4949)
            else -> listOf(0xc1c1ff, 0x7373ff)
        }

        game.playSound(onlinePlayerList, Sound.BLOCK_NOTE_BLOCK_HAT)
        game.sendTitle(
            onlinePlayerList, "$waitTick".toGradientColor(countdownColor), "", 5, 20, 5
        )
        game.sendMessage(
            onlinePlayerList,
            "游戏将在".toGradientColor(
                listOf(
                    0xf5ead0,
                    0xeee6a1
                )
            ) + "${waitTick}秒".toGradientColor(countdownColor) + "后开始哦~".toGradientColor(
                listOf(0xf5ead0, 0xeee6a1)
            )
        )
    }

}