package io.chxxchi.cAlertManager.module;

import eu.okaeri.configs.OkaeriConfig;
import io.chxxchi.cAlertManager.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AlertLog extends OkaeriConfig {
    private AlertType alertType;
    private int count;
    private String reason;
    private String time;
    private String handler;

}