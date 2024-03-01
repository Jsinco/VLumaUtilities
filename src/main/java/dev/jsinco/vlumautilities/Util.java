package dev.jsinco.vlumautilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Util {

    public static Component color(final String colorMe) {
        return MiniMessage.miniMessage().deserialize(colorMe);
    }
}
