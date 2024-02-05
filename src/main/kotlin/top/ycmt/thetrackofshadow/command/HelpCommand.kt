package top.ycmt.thetrackofshadow.command

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.module.chat.impl.DefaultComponent
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.CN_LOGO_LEGACY_TEXT
import top.ycmt.thetrackofshadow.pkg.chat.toGradientColor

// 显示帮助命令
val helpCommand = subCommand {
    execute<ProxyCommandSender> { sender, cmd, _ ->
        sendHelpMsg(sender, cmd) // 发送帮助信息
    }
}

// 发送帮助消息
fun sendHelpMsg(sender: ProxyCommandSender, cmd: CommandContext<ProxyCommandSender>) {
    DefaultComponent().sendTo(sender)
    DefaultComponent().append(
        DefaultComponent()
            .append("<#727272,e1e1e1>-----//</#> $CN_LOGO_LEGACY_TEXT  §f§l- <#c4ffff,7493ff>HELP</#> <#e1e1e1,727272>//-----</#>".toGradientColor())
            .bold()
    ).sendTo(sender)
    DefaultComponent().sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("<#a0a0a0,dedede>-></#> <#808080,e6e6e6>/${cmd.name}</#> <#ffc998,fff0d4>join</#> §7<<#ffc998,fff0d4>游戏名</#>§7> §8- §f加入游戏".toGradientColor())
        )
            .hoverText("<#ffa7a7,fce0e0>添加命令到聊天框</#>".toGradientColor())
            .clickSuggestCommand("/${cmd.name} join ")
    ).sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("<#a0a0a0,dedede>-></#> <#808080,e6e6e6>/${cmd.name}</#> <#ffc998,fff0d4>watch</#> §7<<#ffc998,fff0d4>游戏名</#>§7> §8- §f观看游戏".toGradientColor())
        )
            .hoverText("<#ffa7a7,fce0e0>添加命令到聊天框</#>".toGradientColor())
            .clickSuggestCommand("/${cmd.name} watch ")
    ).sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("<#a0a0a0,dedede>-></#> <#808080,e6e6e6>/${cmd.name}</#> <#ffc998,fff0d4>quit</#> §8- §f离开加入的游戏".toGradientColor())
        )
            .hoverText("<#ffa7a7,fce0e0>添加命令到聊天框</#>".toGradientColor())
            .clickSuggestCommand("/${cmd.name} quit")
    ).sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("<#a0a0a0,dedede>-></#> <#808080,e6e6e6>/${cmd.name}</#> <#ffc998,fff0d4>list</#> §8- §f查询所有游戏房间".toGradientColor())
        )
            .hoverText("<#ffa7a7,fce0e0>添加命令到聊天框</#>".toGradientColor())
            .clickSuggestCommand("/${cmd.name} list")
    ).sendTo(sender)
    DefaultComponent().sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("<#a0a0a0,dedede>-></#> <#808080,e6e6e6>/${cmd.name}</#> <#ffc998,fff0d4>admin</#> §8- §f显示管理员帮助信息".toGradientColor())
        )
            .hoverText("<#ffa7a7,fce0e0>添加命令到聊天框</#>".toGradientColor())
            .clickSuggestCommand("/${cmd.name} admin")
    ).sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("<#a0a0a0,dedede>-></#> <#808080,e6e6e6>/${cmd.name}</#> <#ffc998,fff0d4>help</#> §8- §f显示插件帮助信息".toGradientColor())
        )
            .hoverText("<#ffa7a7,fce0e0>添加命令到聊天框</#>".toGradientColor())
            .clickSuggestCommand("/${cmd.name} help")
    ).sendTo(sender)
    DefaultComponent().sendTo(sender)
}