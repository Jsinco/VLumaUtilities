package dev.jsinco.vlumautilities.commands.subcommands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.List;

public interface Subcommand {

    void executeThis(ProxyServer proxy,  CommandSource source, String[] args, String alias);

    String requiredPermission();

    List<String> tabCompletion(ProxyServer proxy, CommandSource source, String[] args, String alias);

    //CompletableFuture<List<String>> tabCompletionAsync(SimpleCommand.Invocation invocation);
}
