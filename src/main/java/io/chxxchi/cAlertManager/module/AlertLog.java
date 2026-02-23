package io.chxxchi.cAlertManager.module;

import eu.okaeri.configs.OkaeriConfig;
import io.chxxchi.cAlertManager.enums.AlertType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertLog extends OkaeriConfig {
    private AlertType alertType;
    private int count;
    private String reason;
    private String time;
    private String handler;

    public AlertLog() {
    }

    public AlertLog(AlertType alertType, int count, String reason, String time, String handler) {
        this.alertType = alertType;
        this.count = count;
        this.reason = reason;
        this.time = time;
        this.handler = handler;
    }
}