package io.chxxchi.cAlertManager.command;

import io.chxxchi.cAlertManager.service.AlertManagerService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import static io.chxxchi.cAlertManager.lib.CLibrary.*;

public class UserCommand implements CommandExecutor {
    private final AlertManagerService alertManagerService;

    public UserCommand(AlertManagerService alertManagerService) {
        this.alertManagerService = alertManagerService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            info(sender, "콘솔에서는 사용 불가능합니다.");
            return false;
        }

        alertManagerService.open(player, player.getUniqueId());
        return true;
    }
}
