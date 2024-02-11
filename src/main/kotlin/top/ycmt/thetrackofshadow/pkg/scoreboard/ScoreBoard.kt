package top.ycmt.thetrackofshadow.pkg.scoreboard

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import java.util.*

// 记分板
class ScoreBoard(player: Player, name: String, showHealth: Boolean) {
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager()!!.newScoreboard
    private val sidebar: Objective = scoreboard.registerNewObjective("sidebar", Criteria.DUMMY, name)

    init {
        sidebar.displaySlot = DisplaySlot.SIDEBAR
        // 血量显示
        if (showHealth) {
            scoreboard.registerNewObjective("health", "health", "§c§l❤").displaySlot = DisplaySlot.BELOW_NAME
        }
        // 创建队伍
        for (i in 1..15) {
            val team = scoreboard.registerNewTeam("SLOT_$i")
            team.addEntry(genEntry(i))
        }
        player.scoreboard = scoreboard
        playerScoreBoards[player.uniqueId] = this
    }

    fun setTitle(title: String) {
        sidebar.displayName = title
    }

    fun setSlot(slot: Int, text: String) {
        val team = scoreboard.getTeam("SLOT_$slot")
        val entry = genEntry(slot)
        if (!scoreboard.entries.contains(entry)) {
            sidebar.getScore(entry).score = slot
        }
        team?.prefix = text
        team?.suffix = ""
    }

    fun removeSlot(slot: Int) {
        val entry = genEntry(slot)
        if (scoreboard.entries.contains(entry)) {
            scoreboard.resetScores(entry)
        }
    }

    private fun genEntry(slot: Int): String {
        return ChatColor.values()[slot].toString()
    }

    companion object {
        private val playerScoreBoards = mutableMapOf<UUID, ScoreBoard>()
        fun hasScore(player: Player): Boolean {
            return playerScoreBoards.containsKey(player.uniqueId)
        }

        fun createScore(player: Player, name: String, showHealth: Boolean = false): ScoreBoard {
            return ScoreBoard(player, name, showHealth)
        }

        fun getByPlayer(player: Player): ScoreBoard? {
            return playerScoreBoards[player.uniqueId]
        }

        fun removeScore(player: Player) {
            playerScoreBoards[player.uniqueId]?.sidebar?.unregister()
            playerScoreBoards.remove(player.uniqueId)
        }
    }
}