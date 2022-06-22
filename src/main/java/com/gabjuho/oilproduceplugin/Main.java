package com.gabjuho.oilproduceplugin;

import com.gabjuho.oilproduceplugin.commands.Command;
import com.gabjuho.oilproduceplugin.events.Event;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        YamlManager yamlManager = YamlManager.getInstance();

        for(String key : yamlManager.getCoolTime().getKeys(false))
            PlayerOilProducerManager.getOilCoolTime().put(UUID.fromString(key),yamlManager.getCoolTime().getInt(key));

        for(String key : yamlManager.getCoolTime().getKeys(false)) {
            BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(Main.class), () -> PlayerOilProducerManager.ConsumingOilCoolTime(UUID.fromString(key)) , 0, 20);
            PlayerOilProducerManager.AddCoolTimeTask(UUID.fromString(key),task);
        }

        getServer().getPluginManager().registerEvents(new Event(), this);
        getCommand("석유시간").setExecutor(new Command());

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[OilProducePlugin]: Plugin is enabled!");
    }

    @Override
    public void onDisable() {
        YamlManager yamlManager = YamlManager.getInstance();

        for (String key : yamlManager.getCoolTime().getKeys(false))
            yamlManager.getCoolTime().set(key, null);
        yamlManager.saveCoolTime();

        for (Map.Entry<UUID, Integer> entry : PlayerOilProducerManager.getOilCoolTime().entrySet()) {
            yamlManager.getCoolTime().set("" + entry.getKey(), entry.getValue());
        }
        yamlManager.saveCoolTime();

        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[OilProducePlugin]: Plugin is disabled!");
    }
}