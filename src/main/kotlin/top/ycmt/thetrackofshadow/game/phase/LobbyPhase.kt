package top.ycmt.thetrackofshadow.game.phase

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.CN_LOGO_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg
import top.ycmt.thetrackofshadow.pkg.scoreboard.ScoreBoard
import java.text.SimpleDateFormat
import java.util.*

// 大厅等待阶段
class LobbyPhase(private val game: Game) : PhaseAbstract() {
    // 剩余等待的时间
    private var waitTick = game.setting.lobbyWaitTick

    override fun onTick() {
        // 刷新记分板
        refreshBoard()

        // 校验玩家人数是否符合开始游戏所需最小玩家数
        if (game.playerModule.getOnlineUsers().size < game.setting.minPlayerCount) {
            // 重置剩余等待时间并提示玩家
            if (waitTick != game.setting.lobbyWaitTick) {
                waitTick = game.setting.lobbyWaitTick
                // 提示玩家
                notifyWaitMorePlayer()
            }
            return
        }

        // 根据剩余时间执行
        when (waitTick) {
            in 1..5, 10L -> notifyCountdown()

            0L -> {
                // 剩余等待时间结束 此阶段完成
                this.done()
                return
            }

            else -> {
                // 不是指定的时间每10秒提示一次
                if (waitTick % 10 == 0L) {
                    notifyCountdown()
                }
            }
        }
        waitTick--
    }

    // 提示玩家等待更多玩家
    private fun notifyWaitMorePlayer() {
        game.playerModule.getOnlineUsers().forEach {
            it.sendMsg("<#ffcbcb,ff7093>玩家数量不足, 等待更多玩家...</#>".toGradientColor())
            it.sendTitle("", "<#ffcbcb,ff7093>等待更多玩家加入...</#>".toGradientColor(), 5, 30, 5)
            it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
        }
    }

    // 提示倒计时
    private fun notifyCountdown() {
        // 倒计时颜色
        val countdownColor = when (waitTick) {
            10L -> "f7c79c,ef987d"
            in 1..5 -> "ff9c9c,de4949"
            else -> "c1c1ff,7373ff"
        }

        game.playerModule.getOnlineUsers().forEach {
            it.sendMsg("<#f5ead0,eee6a1>游戏将在</#><#$countdownColor>${waitTick}秒</#><#f5ead0,eee6a1>后开始哦~</#>".toGradientColor())
            it.sendTitle("<#$countdownColor>$waitTick</#>".toGradientColor(), "", 5, 20, 5)
            it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
        }
    }

    // 刷新或创建计分板
    private fun refreshBoard() {
        // 设置日期格式
        val df = SimpleDateFormat("MM/dd/yy")

        for (p in game.playerModule.getOnlineUsers()) {
            val board: ScoreBoard =
                if (ScoreBoard.hasScore(p)) ScoreBoard.getByPlayer(p)!! else ScoreBoard.createScore(
                    p,
                    CN_LOGO_LEGACY_TEXT
                )
            board.setSlot(11, "§7${df.format(Date())}  §8${game.setting.gameName}")
            board.setSlot(10, "")
            board.setSlot(9, "§f地图: <#dcffcc,9adbb1>${game.setting.mapName}</#>".toGradientColor())
            board.setSlot(
                8,
                "§f玩家: <#dcffcc,9adbb1>${game.playerModule.getOnlineUsers().size}</#>§f/<#dcffcc,9adbb1>${game.setting.maxPlayerCount}</#>".toGradientColor()
            )
            board.setSlot(7, "")
            board.setSlot(
                6,
                if (game.playerModule.getOnlineUsers().size >= game.setting.minPlayerCount) "§f即将开始: " + "<#dcffcc,9adbb1>${waitTick}秒</#>".toGradientColor() else "§f等待中..."
            )
            board.setSlot(5, "")
            board.setSlot(4, "§f模式: <#dcffcc,9adbb1>单挑</#>".toGradientColor())
            board.setSlot(3, "§f版本: <#ffd6d6,e14d4d>1.4 Beta</#>".toGradientColor())
            board.setSlot(2, "")
            board.setSlot(1, "<#fff4ba,f4f687>mc.ycmt.top</#>".toGradientColor())
        }
    }

}