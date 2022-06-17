package com.gabjuho.oilproduceplugin.events;

import com.gabjuho.oilproduceplugin.YamlManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Event implements Listener {

    YamlManager yamlManager = YamlManager.getInstance();
    @EventHandler
    void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!player.getWorld().getName().equals("world"))
            return;

        Location loc1 = event.getBlock().getLocation(); // craftTable loc
        Location loc2 = new Location(world, loc1.getBlockX(), loc1.getBlockY() - 1, loc1.getBlockZ()); // dropper loc
        Location loc3 = new Location(world, loc1.getBlockX(), loc1.getBlockY() - 2, loc1.getBlockZ()); // hopper loc
        Location loc4 = new Location(world, loc1.getBlockX(), loc1.getBlockY() - 3, loc1.getBlockZ()); // bedrock loc

        if (event.getBlock().getType() == Material.CRAFTING_TABLE && loc2.getBlock().getType() == Material.DROPPER && loc3.getBlock().getType() == Material.HOPPER && loc4.getBlock().getType() == Material.BEDROCK) {
            if(yamlManager.getLoc().contains(player.getUniqueId().toString()))
            {
                player.sendMessage(ChatColor.RED + "이미 소유하고 있는 석유 생성기가 있습니다.");
                event.setCancelled(true);
                return;
            }
            yamlManager.getLoc().set(player.getUniqueId().toString(), loc3);
            yamlManager.saveLoc();
            player.sendMessage(ChatColor.GREEN + "석유 생성기가 만들어졌습니다!!");
        }
    }

}
