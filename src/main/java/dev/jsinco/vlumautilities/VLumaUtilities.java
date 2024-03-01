package dev.jsinco.vlumautilities;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.jsinco.vlumautilities.commands.EchoCommand;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "vlumautilities",
        name = "VLumaUtilities",
        version = "1.0.0"
)
public class VLumaUtilities {

    private static ProxyServer proxy;
    private static Logger logger;
    private static Path dataDirectory;

    @Inject
    public VLumaUtilities(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        VLumaUtilities.proxy = proxy;
        VLumaUtilities.logger = logger;
        VLumaUtilities.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("VLumaUtilities has been initialized!");


        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("echo").plugin(this).build();


        commandManager.register(commandMeta, new EchoCommand(proxy));



        RegisteredServer server = proxy.getServer("pufferfish").orElse(null);

        // ping server and check if it's online
        if (server != null) {
            server.ping().whenComplete((status, throwable) -> {
                if (throwable != null) {
                    logger.error("Error pinging server: " + throwable.getMessage());
                } else {
                    logger.info("Server is online!");
                }
            });
        }
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
}

//         // send command on proxy
//        proxy.getCommandManager().executeAsync(proxy.getConsoleCommandSource(), "alert VLumaUtilities has been initialized!");
