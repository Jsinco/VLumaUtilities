package dev.jsinco.vlumautilities.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jsinco.vlumautilities.commands.subcommands.ShadowBanCommand;
import dev.jsinco.vlumautilities.commands.subcommands.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LumaProxyCommandManager implements SimpleCommand {

    private final static Map<String, Subcommand> subcommands = new HashMap<>();
    private final ProxyServer proxy;

    public LumaProxyCommandManager(final ProxyServer proxy) {
        this.proxy = proxy;

        // Register subcommands
        subcommands.put("shadowban", new ShadowBanCommand());
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {
            source.sendMessage(Component.text("Usage: /luma <subcommand> [args]", NamedTextColor.RED));
            return;
        } else if (!subcommands.containsKey(args[0])) {
            source.sendMessage(Component.text("Unknown subcommand", NamedTextColor.RED));
            return;
        }
        Subcommand subcommandObj = subcommands.get(args[0]);
        String permission = subcommandObj.requiredPermission();

        if ((permission == null || source.hasPermission(permission))) {
            subcommandObj.executeThis(proxy, source, args, args[0]);
        } else {
            source.sendMessage(Component.text("You do not have permission to execute this command", NamedTextColor.RED));
        }
    }

    // This method allows you to control who can execute the command.
    // If the executor does not have the required permission,
    // the execution of the command and the control of its autocompletion
    // will be sent directly to the server on which the sender is located
    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("vlumautilities.command");
    }

    // With this method you can control the suggestions to send
    // to the CommandSource according to the arguments
    // it has already written or other requirements you need
    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    // Here you can offer argument suggestions in the same way as the previous method,
    // but asynchronously. It is recommended to use this method instead of the previous one
    // especially in cases where you make a more extensive logic to provide the suggestions
    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length <= 1 && source.hasPermission("vlumautilities.command")) {
            return CompletableFuture.completedFuture(subcommands.keySet().stream().toList());
        }
        if ((args.length == 2 && subcommands.containsKey(args[0])) && (subcommands.get(args[0]).requiredPermission() == null || source.hasPermission(subcommands.get(args[0]).requiredPermission()))) {
            return CompletableFuture.completedFuture(subcommands.get(args[0]).tabCompletion(proxy, source, args, args[0]));
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

}
