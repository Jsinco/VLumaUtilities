package dev.jsinco.vlumautilities;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.jsinco.vlumautilities.commands.EchoCommand;
import dev.jsinco.vlumautilities.files.JsonSavingSchema;
import dev.jsinco.vlumautilities.files.SnakeYamlConfig;
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

        //SnakeYamlConfig file = new SnakeYamlConfig("config.yaml");
        JsonSavingSchema file = new JsonSavingSchema("saves.json");
        file.set("tester.testingtwo", 3);
        file.save();
        SnakeYamlConfig yamlConfig = new SnakeYamlConfig("testing.yml");
        yamlConfig.set("test", "test");
        yamlConfig.save();
        /*file.getConfigurationSection("test").getKeys().forEach(key -> {
            logger.info(key + ": " + file.getObject("test." + key).getClass().getName());
        });*/
        //file.setString("testr", "testt");
        //file.save();

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
