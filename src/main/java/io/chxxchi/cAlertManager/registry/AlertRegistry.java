package io.chxxchi.cAlertManager.registry;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import io.chxxchi.cAlertManager.module.AlertLog;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class AlertRegistry extends OkaeriConfig {
    @Setter
    @Getter
    private int maxAlertCount = 10;

    @Setter
    @Getter
    @Comment("유저별 경고 로그")
    private Map<UUID, List<AlertLog>> alertMap = new HashMap<>();

    @Comment("유저별 경고 누적 횟수")
    private Map<UUID, Integer> alertCountMap = new HashMap<>();

    public int getAlertCount(UUID uuid) {
        return alertCountMap.computeIfAbsent(uuid, key -> 0);
    }

    public void setAlertCount(UUID uuid, int count) {
        alertCountMap.put(uuid, count);
    }

    public List<AlertLog> getAlertLogs(UUID uuid) {
        return alertMap.computeIfAbsent(uuid, key -> new ArrayList<>());
    }

    public AlertLog getAlertLog(UUID uuid, int index) {
        return getAlertLogs(uuid).get(index);
    }

    public void addAlertLog(UUID uuid, AlertLog alertLog) {
        getAlertLogs(uuid).add(alertLog);
    }

}
