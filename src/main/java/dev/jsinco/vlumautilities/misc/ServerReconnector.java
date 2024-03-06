package dev.jsinco.vlumautilities.misc;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import dev.jsinco.vlumautilities.Util;
import dev.jsinco.vlumautilities.VLumaUtilities;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerReconnector {

    private final static Logger logger = VLumaUtilities.getLogger();
    private final static ProxyServer proxy = VLumaUtilities.getProxy();
    private final static Map<RegisteredServer, ReconnectingServer> sendCache = new HashMap<>();


    private void setForReconnect(RegisteredServer server, UUID player) {
        ReconnectingServer reconnectingServer = sendCache.get(server);
        reconnectingServer.players.add(player);
    }

    @Subscribe
    public void onPlayerDisconnect(KickedFromServerEvent event) {
        RegisteredServer server = event.getServer();

        proxy.getScheduler().buildTask(VLumaUtilities.getPlugin(), () -> {
            server.ping().whenComplete((status, throwable) -> {
                if (throwable != null) {
                    if (!sendCache.containsKey(server)) {
                        logger.info("Server " + server.getServerInfo().getName() + " is offline, starting pinging");
                        startPingingServer(server);
                    }
                    logger.info("Player " + event.getPlayer().getUsername() + " has been disconnected from offline server " + server.getServerInfo().getName() + ", adding to reconnect list");
                    setForReconnect(server, event.getPlayer().getUniqueId());
                }
            });
        }).delay(10, TimeUnit.SECONDS).schedule();
    }

    private void startPingingServer(RegisteredServer server) {
        ScheduledTask task = proxy.getScheduler().buildTask(VLumaUtilities.getPlugin(), () -> {
            server.ping().whenComplete((status, throwable) -> {
                logger.info("Pinging server: " + server.getServerInfo().getName());
                ReconnectingServer reconnectingServer = sendCache.get(server);
                if (status != null) {
                    logger.info("Server " + server.getServerInfo().getName() + " is online, reconnecting players");
                    reconnectingServer.reconnect(server);
                    reconnectingServer.task.cancel();
                    sendCache.remove(server);
                    return;
                }

                if (reconnectingServer.attempts > 6) {
                    logger.warn("Server " + server.getServerInfo().getName() + " is still offline after 6 attempts, cancelling");
                    reconnectingServer.cancel();
                    sendCache.remove(server);
                    return;
                }
                logger.info("Server " + server.getServerInfo().getName() + " is offline, retrying in 1 minute");
                reconnectingServer.attempts++;
            });
        }).repeat(1, TimeUnit.MINUTES).schedule();

        sendCache.put(server, new ReconnectingServer(new ArrayList<>(), task));


    }

    private static class ReconnectingServer {
        private final ScheduledTask task;
        private int attempts = 0;
        private final List<UUID> players;

        public ReconnectingServer(List<UUID> players, ScheduledTask task) {
            this.task = task;
            this.players = players;
        }

        public void cancel() {
            task.cancel();
        }

        public void reconnect(RegisteredServer server) {
            server.getPlayersConnected().forEach(player -> {
                player.sendMessage(Util.getPrefix().append(Util.color("Sending <#f498f6>" + players.size() + " <#E2E2E2>to <#f498f6> " + server.getServerInfo().getName())));
            });
            for (UUID uuid : players) {
                Player player = proxy.getPlayer(uuid).orElse(null);
                if (player == null) {
                    continue;
                }
                player.createConnectionRequest(server).fireAndForget();
                player.sendMessage(Util.getPrefix().append(Util.color("Sending you to <#f498f6>" + server.getServerInfo().getName() + "<#E2E2E2>!")));
            }
        }
    }
}
