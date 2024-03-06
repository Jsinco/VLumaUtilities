package dev.jsinco.vlumautilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Util {

    private final static VLumaUtilities plugin = VLumaUtilities.getPlugin();

    public static Component getPrefix() {
        return color(plugin.getConfig().getString("prefix"));
    }

    public static Component color(final String colorMe) {
        return MiniMessage.miniMessage().deserialize(colorMe);
    }
}
