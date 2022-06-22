package com.gabjuho.oilproduceplugin.commands;

import com.gabjuho.oilproduceplugin.PlayerOilProducerManager;
import com.gabjuho.oilproduceplugin.YamlManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("오직 플레이어만 명령어를 사용할 수 있습니다.");
            return true;
        }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("석유시간")) {
            if (PlayerOilProducerManager.getOilCoolTime().containsKey(player.getUniqueId()))
                player.sendMessage(ChatColor.GRAY + player.getName() + "님의 석유 생성시간 까지 " + PlayerOilProducerManager.getOilCoolTime().get(player.getUniqueId()) + "초 남았습니다.");
            else
                player.sendMessage(ChatColor.RED + "현재 가동하고 있는 석유 생성기가 존재하지 않습니다.");
        }

        if (cmd.getName().equalsIgnoreCase("석유위치")) {
            if (YamlManager.getInstance().getLoc().contains(player.getUniqueId().toString())) {
                Location loc = YamlManager.getInstance().getLoc().getLocation(player.getUniqueId().toString());
                player.sendMessage(ChatColor.AQUA + player.getName() + "님의 석유 생성기 위치:");
                player.sendMessage(ChatColor.WHITE + "X: "+ loc.getBlockX() + " Y: "+ loc.getBlockY() + " Z: "+loc.getBlockZ());
            } else {
                player.sendMessage(ChatColor.RED + "보유하고 있는 석유 생성기가 존재하지 않습니다.");
            }
        }
        return true;
    }
}
