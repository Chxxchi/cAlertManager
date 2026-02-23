package io.chxxchi.cAlertManager.lib;

import io.chxxchi.cAlertManager.CAlertManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public class CLibrary {
    private static final MiniMessage MM = MiniMessage.builder().build();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    public static Component colorize(String content) {
        Component component = MM.deserialize(content);
        component = LEGACY.deserialize(LEGACY.serialize(component));
        return component;
    }

    public static void send(Audience audience, String content) {
        audience.sendMessage(colorize(content));
    }

    public static void info(Audience audience, String content) {
        String prefix = CAlertManager.PREFIX;
        send(audience, prefix + content);
    }

    public static void broadcast(String content) {
        Bukkit.broadcast(colorize(content));
    }
}
