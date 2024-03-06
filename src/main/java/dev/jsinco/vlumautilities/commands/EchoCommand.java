package dev.jsinco.vlumautilities.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jsinco.vlumautilities.Util;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

public final class EchoCommand implements SimpleCommand {

    private final ProxyServer proxy;

    public EchoCommand(final ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final Invocation invocation) {
        final Component text = Util.color(Arrays.toString(invocation.arguments()).substring(1, Arrays.toString(invocation.arguments()).length() - 1));
        proxy.getAllPlayers().forEach(player -> player.sendMessage(text));
        proxy.getConsoleCommandSource().sendMessage(text);
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("vlumautilities.command.echo");
    }


}
