package io.chxxchi.cAlertManager;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import io.chxxchi.cAlertManager.command.AdminCommand;
import io.chxxchi.cAlertManager.command.UserCommand;
import io.chxxchi.cAlertManager.registry.AlertRegistry;
import io.chxxchi.cAlertManager.service.AlertManagerService;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CAlertManager extends JavaPlugin {
    public static final String PREFIX = "<#66a1ff>[경고관리] &f";
    public static final String ACCENT_COLOR = "<#66a1ff>";
    public static final String BASE_COLOR = "<#d6e6ff>";

    private AlertRegistry alertRegistry;

    @Override
    public void onEnable() {
        alertRegistry = ConfigManager.create(AlertRegistry.class, (it) -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(this.getDataFolder(), "registry.yml"));
            it.saveDefaults();
            it.load(true);
        });

        saveDefaultConfig();
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        int maxAlertCount = getConfig().getInt("alertMaxCount");
        alertRegistry.setMaxAlertCount(maxAlertCount);

        AlertManagerService alertManagerService = new AlertManagerService(alertRegistry);

        getCommand("경고관리").setExecutor(new AdminCommand(alertManagerService));
        getCommand("경고").setExecutor(new UserCommand(alertManagerService));

        getLogger().info("경고관리 플러그인이 활성화 되었습니다. - 제작: 참치");

    }

    @Override
    public void onDisable() {
        alertRegistry.save();
        getLogger().info("경고관리 플러그인이 비활성화 되었습니다. - 제작: 참치");
    }
}
