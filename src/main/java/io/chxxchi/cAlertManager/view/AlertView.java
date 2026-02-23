package io.chxxchi.cAlertManager.view;

import io.chxxchi.cAlertManager.module.AlertLog;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.chxxchi.cAlertManager.lib.CLibrary.*;

public class AlertView {
    private final UUID ownerUUID;
    private final List<AlertLog> logs;
    private final int totalCount;
    private ItemStack book;

    public AlertView(UUID ownerUUID, List<AlertLog> logs, int totalCount) {
        this.ownerUUID = ownerUUID;
        this.logs = logs;
        this.totalCount = totalCount;

        init();
    }

    public void open(Player player) {
        player.openBook(book);
    }

    private void init() {
        String userName = Bukkit.getOfflinePlayer(ownerUUID).getName();
        book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle(userName + "님의 경고기록");
        meta.setAuthor("참치");

        List<Component> pages = new ArrayList<>();
        StringBuilder firstBuilder = new StringBuilder();
        List<AlertLog> reversed = logs.reversed();
        int index = 0;

        firstBuilder
                .append(String.format("%s님의 경고기록\n", userName))
                .append(String.format("└ 누적 횟수: %d회\n\n", totalCount));

        for (int i = 0; i<2 && index < reversed.size(); i++, index++) {
            firstBuilder.append(formatLog(reversed.get(index)));
        }
        pages.add(colorize(firstBuilder.toString()));

        while (index < reversed.size()) {
            StringBuilder nextBuilder = new StringBuilder();

            for (int i = 0; i<3 && index < reversed.size(); i++, index++) {
                nextBuilder.append(formatLog(reversed.get(index)));
            }

            pages.add(colorize(nextBuilder.toString()));
        }

        meta.pages(pages);
        book.setItemMeta(meta);
    }

    private String formatLog(AlertLog alertLog) {
        String type = "";
        switch (alertLog.getAlertType()) {
            case ADD -> type = "지급";
            case SET -> type = "설정";
            case REMOVE -> type = "차감";
        }

        return new StringBuilder()
                .append(String.format("&4경고 %d회 %s &0\n", alertLog.getCount(), type))
                .append(String.format(" └ 사유: %s\n", alertLog.getReason()))
                .append(String.format(" └ 처리: %s\n", alertLog.getHandler()))
                .append(String.format(" └ 일시: %s\n\n", alertLog.getTime()))
                .toString();
    }

}
