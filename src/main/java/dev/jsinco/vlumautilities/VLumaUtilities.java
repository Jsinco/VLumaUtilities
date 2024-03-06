package dev.jsinco.vlumautilities;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jsinco.vlumautilities.commands.EchoCommand;
import dev.jsinco.vlumautilities.commands.LumaProxyCommandManager;
import dev.jsinco.vlumautilities.commands.subcommands.ShadowBanCommand;
import dev.jsinco.vlumautilities.filemanagers.SnakeYamlConfig;
import dev.jsinco.vlumautilities.misc.ServerReconnector;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "vlumautilities",
        name = "VLumaUtilities",
        version = "1.0.0",
        authors = {"jsinco"}
)
public class VLumaUtilities {

    private static ProxyServer proxy;
    private static Logger logger;
    private static Path dataDirectory;
    private static VLumaUtilities plugin;
    private SnakeYamlConfig config;

    @Inject
    public VLumaUtilities(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        VLumaUtilities.proxy = proxy;
        VLumaUtilities.logger = logger;
        VLumaUtilities.dataDirectory = dataDirectory;
        plugin = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        config = new SnakeYamlConfig("config.yml");

        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("echo").plugin(this).build();


        commandManager.register(commandMeta, new EchoCommand(proxy));
        commandManager.register(commandManager.metaBuilder("lumaproxy").plugin(this).build(), new LumaProxyCommandManager(proxy));

        proxy.getEventManager().register(this, new ShadowBanCommand());
        proxy.getEventManager().register(this, new ServerReconnector());

    }


    public static ProxyServer getProxy() {
        return proxy;
    }
    public static Logger getLogger() {
        return logger;
    }
    public static Path getDataFolder() {
        return dataDirectory;
    }

    public static VLumaUtilities getPlugin() {
        return plugin;
    }

    public SnakeYamlConfig getConfig() {
        return config;
    }
}

//         // send command on proxy
//        proxy.getCommandManager().executeAsync(proxy.getConsoleCommandSource(), "alert VLumaUtilities has been initialized!");
