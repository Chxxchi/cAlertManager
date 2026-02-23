package io.chxxchi.cAlertManager.command;

import io.chxxchi.cAlertManager.CAlertManager;
import io.chxxchi.cAlertManager.enums.AlertType;
import io.chxxchi.cAlertManager.service.AlertManagerService;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

import static io.chxxchi.cAlertManager.lib.CLibrary.*;

public class AdminCommand implements CommandExecutor, TabCompleter {
    private final AlertManagerService alertManagerService;
    private final List<String> helpMessages;
    private final Map<String, BiConsumer<Player, String[]>> executeMap;

    private final String prefix = CAlertManager.PREFIX;
    private final String accentColor = CAlertManager.ACCENT_COLOR;
    private final String baseColor = CAlertManager.BASE_COLOR;

    public AdminCommand(AlertManagerService alertManagerService) {
        this.alertManagerService = alertManagerService;

        helpMessages = getHelpMessages();
        executeMap = getExecuteMap();
    }

    private Map<String, BiConsumer<Player, String[]>> getExecuteMap() {
        Map<String, BiConsumer<Player, String[]>> executeMap = new HashMap<>();

        executeMap.put("열람", (player, args) -> {
            if (args.length == 1) {
                info(player, "대상의 이름을 입력하여주세요.");
                return;
            }

            String targetName = args[1];
            OfflinePlayer op = Bukkit.getOfflinePlayer(targetName);
            if (!op.hasPlayedBefore()) {
                info(player, "서버에 접속한 기록이 없는 유저입니다.");
                return;
            }

            UUID targetUUID = op.getUniqueId();
            alertManagerService.open(player, targetUUID);
        });

        executeMap.put("경고", (player, args) -> {
            if (args.length == 1) {
                info(player, "대상의 이름을 입력하여주세요.");
                return;
            }
            if (args.length == 2) {
                info(player, "값을 입력하여주세요.");
                return;
            }
            if (args.length == 3) {
                info(player, "사유를 입력하여주세요.");
                return;
            }

            OfflinePlayer of = Bukkit.getOfflinePlayer(args[1]);
            if (!of.hasPlayedBefore()) {
                info(player, "서버에 접속한 기록이 없는 유저입니다.");
                return;
            }
            UUID targetUUID = of.getUniqueId();

            String value = args[2];
            String koreanType = "";
            AlertType alertType;
            int count;

            if (value.startsWith("+")) {
                alertType = AlertType.ADD;
                koreanType = "추가";
                value = value.replace("+", "");

            } else if (value.startsWith("-")) {
                alertType = AlertType.REMOVE;
                koreanType = "삭제";
                value = value.replace("-", "");

            } else {
                alertType = AlertType.SET;
                koreanType = "설정";
            }

            count = Integer.parseInt(value);
            StringBuilder reason = new StringBuilder();

            for (int index = 0; index<args.length; index++) {
                if (index < 3) continue;
                reason.append(args[index]);
                reason.append(" ");
            }

            alertManagerService.addAlert(targetUUID, alertType, count, reason.toString(), player.getName());
            int totalCount = alertManagerService.getTotalCount(targetUUID);

            broadcast("");
            broadcast(String.format(" %s %s%s님이 %s님의 경고를 %s하였습니다.",
                    prefix, baseColor, player.getName(), args[1], koreanType));
            broadcast(String.format("   %s└ &f횟수: %d회", accentColor, count));
            broadcast(String.format("   %s└ &f사유: %s", accentColor, reason));
            broadcast(String.format("   %s└ &f누적: %d회", accentColor, totalCount));
            broadcast("");
        });
        return executeMap;
    }

    private List<String> getHelpMessages() {
        return new ArrayList<>(List.of(
                String.format(""),
                String.format(" %s%s/경고관리", prefix, baseColor),
                String.format("   %s└ %s열람 <유저> &7- 대상의 경고기록을 확인합니다.", accentColor, baseColor),
                String.format("   %s└ %s경고 <유저> <값> <사유> &7- 대상의 경고값을 변경합니다.", accentColor, baseColor),
                String.format("   &f ( +1 입력 시 1회 지급, -1 입력 시 1회 차감, 1 입력 시 1로 설정)"),
                String.format("")
        ));
    }

    private void showHelp(Audience audience) {
        for (String message : helpMessages) {
            send(audience, message);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            info(sender, "콘솔에서는 명령어 사용이 불가능합니다.");
            return false;
        }

        if (!player.isOp()) {
            info(sender, "권한이 없어 명령어 사용이 불가능합니다.");
            return false;
        }

        if (args.length == 0) {
            showHelp(player);
            return false;
        }

        String arg = args[0];
        if (executeMap.containsKey(arg)) executeMap.get(arg).accept(player, args);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();

        if (args.length == 1) {
            return List.of("열람", "경고");
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "열람","경고": {
                    return onlinePlayers.stream()
                            .filter(name -> name.startsWith(args[1]))
                            .toList();
                }
            }
        }

        return List.of();
    }
}
