package io.chxxchi.cAlertManager.service;

import io.chxxchi.cAlertManager.CAlertManager;
import io.chxxchi.cAlertManager.enums.AlertType;
import io.chxxchi.cAlertManager.module.AlertLog;
import io.chxxchi.cAlertManager.registry.AlertRegistry;
import io.chxxchi.cAlertManager.view.AlertView;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import static io.chxxchi.cAlertManager.lib.CLibrary.*;

public class AlertManagerService {
    private final AlertRegistry alertRegistry;

    public AlertManagerService(AlertRegistry alertRegistry) {
        this.alertRegistry = alertRegistry;
    }

    public void open(Player player, UUID targetUUID) {
        new AlertView(
                targetUUID,
                alertRegistry.getAlertLogs(targetUUID),
                alertRegistry.getAlertCount(targetUUID)
        ).open(player);
    }

    public int getMaxCount() {
        return alertRegistry.getMaxAlertCount();
    }

    public void setMaxCount(int count) {
        alertRegistry.setMaxAlertCount(count);
    }

    public int getTotalCount(UUID uuid) {
        return alertRegistry.getAlertCount(uuid);
    }

    public void addAlert(UUID uuid, AlertType type, int count, String reason, String handler) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy. MM. dd HH:mm");
        String time = LocalDateTime.now().format(formatter);
        AlertLog alertLog = new AlertLog(type, count, reason, time, handler);
        alertRegistry.addAlertLog(uuid, alertLog);

        int currentCount = alertRegistry.getAlertCount(uuid);

        switch (type) {
            case ADD -> currentCount += count;
            case SET -> currentCount = count;
            case REMOVE -> currentCount -= count;
        }
        if (currentCount < 0) currentCount = 0;

        alertRegistry.setAlertCount(uuid, currentCount);
        banCheck(uuid, currentCount);
    }

    private void banCheck(UUID uuid, int count) {
        OfflinePlayer of = Bukkit.getOfflinePlayer(uuid);
        if (count < alertRegistry.getMaxAlertCount()) return;

        String reason = "경고 누적으로 인한 서버 차단";
        of.ban(reason, (Date) null, "AlertManager");

        if (of.isOnline()) {
            of.getPlayer().kick(colorize(reason));
        }

        String name = (of.getName() != null) ? of.getName() : uuid.toString();
        broadcast(String.format("%s &f%s님이 경고 누적으로 인해 서버에서 차단되셨습니다.", CAlertManager.PREFIX, name));
    }
}
