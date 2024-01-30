package top.ycmt.thetrackofshadow.cmd

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.module.chat.impl.DefaultComponent
import taboolib.module.chat.toGradientColor

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
            .append("-----//".toGradientColor(listOf(0x727272, 0xe1e1e1))).bold()
            .append(" ")
            .append("TheTrackOfShadow".toGradientColor(listOf(0xffd89d, 0x76d3c3, 0x8a95f5))).bold()
            .append(" §f§l- ")
            .append("HELP".toGradientColor(listOf(0xc4ffff, 0x7493ff))).bold()
            .append(" ")
            .append("//-----".toGradientColor(listOf(0xe1e1e1, 0x727272))).bold()
    ).sendTo(sender)
    DefaultComponent().sendTo(sender)
    DefaultComponent().append(
        DefaultComponent()
            .append(">> 所有重要的子命令及注释:".toGradientColor(listOf(0xededed, 0x797979))).bold()
    ).sendTo(sender)
    DefaultComponent().sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("->".toGradientColor(listOf(0xa0a0a0, 0xdedede)))
                .append(" ")
                .append("/${cmd.name}".toGradientColor(listOf(0x808080, 0xe6e6e6)))
                .append(" ")
                .append("join".toGradientColor(listOf(0xffc998, 0xfff0d4)))
                .append(" ")
                .append("§7<")
                .append("游戏名".toGradientColor(listOf(0xffc998, 0xfff0d4)))
                .append("§7>")
                .append(" ")
                .append("§8- §f加入游戏")
        )
            .hoverText("添加命令到聊天框".toGradientColor(listOf(0xffa7a7, 0xfce0e0)))
            .clickSuggestCommand("/${cmd.name} join ")
    ).sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("->".toGradientColor(listOf(0xa0a0a0, 0xdedede)))
                .append(" ")
                .append("/${cmd.name}".toGradientColor(listOf(0x808080, 0xe6e6e6)))
                .append(" ")
                .append("watch".toGradientColor(listOf(0xffc998, 0xfff0d4)))
                .append(" ")
                .append("§7<")
                .append("游戏名".toGradientColor(listOf(0xffc998, 0xfff0d4)))
                .append("§7>")
                .append(" ")
                .append("§8- §f观看游戏")
        )
            .hoverText("添加命令到聊天框".toGradientColor(listOf(0xffa7a7, 0xfce0e0)))
            .clickSuggestCommand("/${cmd.name} watch ")
    ).sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("->".toGradientColor(listOf(0xa0a0a0, 0xdedede)))
                .append(" ")
                .append("/${cmd.name}".toGradientColor(listOf(0x808080, 0xe6e6e6)))
                .append(" ")
                .append("quit".toGradientColor(listOf(0xffc998, 0xfff0d4)))
                .append(" ")
                .append("§8- §f离开加入的游戏")
        )
            .hoverText("添加命令到聊天框".toGradientColor(listOf(0xffa7a7, 0xfce0e0)))
            .clickSuggestCommand("/${cmd.name} quit")
    ).sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("->".toGradientColor(listOf(0xa0a0a0, 0xdedede)))
                .append(" ")
                .append("/${cmd.name}".toGradientColor(listOf(0x808080, 0xe6e6e6)))
                .append(" ")
                .append("list".toGradientColor(listOf(0xffc998, 0xfff0d4)))
                .append(" ")
                .append("§8- §f查询所有游戏房间")
        )
            .hoverText("添加命令到聊天框".toGradientColor(listOf(0xffa7a7, 0xfce0e0)))
            .clickSuggestCommand("/${cmd.name} list")
    ).sendTo(sender)
    DefaultComponent().sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("->".toGradientColor(listOf(0xa0a0a0, 0xdedede)))
                .append(" ")
                .append("/${cmd.name}".toGradientColor(listOf(0x808080, 0xe6e6e6)))
                .append(" ")
                .append("admin".toGradientColor(listOf(0xffc998, 0xfff0d4)))
                .append(" ")
                .append("§8- §f显示管理员帮助信息")
        )
            .hoverText("添加命令到聊天框".toGradientColor(listOf(0xffa7a7, 0xfce0e0)))
            .clickSuggestCommand("/${cmd.name} admin")
    ).sendTo(sender)
    DefaultComponent().append(
        DefaultComponent().append(
            DefaultComponent()
                .append("->".toGradientColor(listOf(0xa0a0a0, 0xdedede)))
                .append(" ")
                .append("/${cmd.name}".toGradientColor(listOf(0x808080, 0xe6e6e6)))
                .append(" ")
                .append("help".toGradientColor(listOf(0xffc998, 0xfff0d4)))
                .append(" ")
                .append("§8- §f显示插件帮助信息")
        )
            .hoverText("添加命令到聊天框".toGradientColor(listOf(0xffa7a7, 0xfce0e0)))
            .clickSuggestCommand("/${cmd.name} help")
    ).sendTo(sender)
    DefaultComponent().sendTo(sender)
}