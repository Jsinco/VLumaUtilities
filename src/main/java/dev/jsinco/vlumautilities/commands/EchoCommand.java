package dev.jsinco.vlumautilities.commands;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jsinco.vlumautilities.Util;
import net.kyori.adventure.text.Component;

public final class EchoCommand implements RawCommand {

    private final ProxyServer proxy;

    public EchoCommand(final ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final Invocation invocation) {
        final Component text = Util.color(invocation.arguments());
        proxy.getAllPlayers().forEach(player -> player.sendMessage(text));
        proxy.getConsoleCommandSource().sendMessage(text);
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("vlumautilities.command.echo");
    }
}
