package com.gabjuho.oilproduceplugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable()
    {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[OilProducePlugin]: Plugin is enabled!");
    }
    @Override
    public void onDisable()
    {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[OilProducePlugin]: Plugin is disabled!");
    }
}