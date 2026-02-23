package io.chxxchi.cAlertManager.module;

import eu.okaeri.configs.OkaeriConfig;
import io.chxxchi.cAlertManager.enums.AlertType;

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

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
}