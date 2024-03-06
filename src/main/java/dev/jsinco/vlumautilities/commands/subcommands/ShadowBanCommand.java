package dev.jsinco.vlumautilities.commands.subcommands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.jsinco.vlumautilities.files.Saves;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShadowBanCommand implements Subcommand {

    private static final List<String> shadowBans = new ArrayList<>(Saves.getFile().getStringList("shadowBanned"));
    private static final List<String> bannedIps = new ArrayList<>();
    private static final List<String> bannedUUIDs = new ArrayList<>();

    public ShadowBanCommand() {
        reloadBans();
    }

    @Override
    public void executeThis(ProxyServer proxy, CommandSource source, String[] args, String alias) {
        if (args[1].equalsIgnoreCase("list")) {
            source.sendMessage(Component.text("Shadowbanned players: " + shadowBans));
            return;
        }

        Player player = proxy.getPlayer(args[2]).orElse(null);
        if (player == null) {
            source.sendMessage(Component.text("Player not found"));
            return;
        }

        String shadowBanID = player.getRemoteAddress().getAddress().getHostAddress() + "/" + player.getUniqueId().toString();

        switch (args[1]) {
            case "add" -> {
                if (shadowBans.contains(shadowBanID)) {
                    source.sendMessage(Component.text("Player already shadowbanned"));
                    return;
                }
                shadowBans.add(shadowBanID);
                if (!Arrays.stream(args).toList().contains("-s")) {
                    player.disconnect(Component.text(""));
                }
                source.sendMessage(Component.text("Player shadowbanned"));
            }
            case "remove" -> {
                if (!shadowBans.contains(shadowBanID)) {
                    source.sendMessage(Component.text("Player not shadowbanned"));
                    return;
                }
                shadowBans.remove(shadowBanID);
                source.sendMessage(Component.text("Player unshadowbanned"));
            }
        }

        Saves.getFile().set("shadowBanned", shadowBans);
        Saves.save();

        reloadBans();
    }

    private void reloadBans() {
        bannedIps.clear();
        bannedUUIDs.clear();
        for (String shadowBan : shadowBans) {
            bannedIps.add(shadowBan.split("/")[0]);
            bannedUUIDs.add(shadowBan.split("/")[1]);
        }
    }

    @Override
    public String requiredPermission() {
        return "vlumautilities.command.shadowban";
    }

    @Override
    public List<String> tabCompletion(ProxyServer proxy, CommandSource source, String[] args, String alias) {
        return List.of("add", "remove", "list");
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {


        if (bannedIps.contains(event.getConnection().getRemoteAddress().getAddress().getHostAddress())) {
            event.setPing(event.getPing().asBuilder()
                            .maximumPlayers(0)
                            .onlinePlayers(0)
                            .clearSamplePlayers()
                            .description(Component.empty())
                            .version(new ServerPing.Version(0, "Unknown"))
                            .clearFavicon()
                            .build()
            );
        }
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        if (bannedIps.contains(event.getPlayer().getRemoteAddress().getAddress().getHostAddress()) ||
                bannedUUIDs.contains(String.valueOf(event.getPlayer().getUniqueId()))) {
            event.setResult(ResultedEvent.ComponentResult.denied(Component.empty()));
        }
    }
}
