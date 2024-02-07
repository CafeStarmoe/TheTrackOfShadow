package top.ycmt.thetrackofshadow.game.state

// 玩家禁止状态
enum class CancelState {
    CANCEL_MOVE, // 禁止移动
    CANCEL_MOVE_ANGLE, // 禁止移动视角
    CANCEL_PVP, // 禁止PVP
    CANCEL_DAMAGE, // 禁止受伤
    CANCEL_OPEN_CHEST, // 禁止开箱
    CANCEL_RESPAWN, // 禁止重生
}