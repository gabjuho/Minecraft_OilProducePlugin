package com.gabjuho.oilproduceplugin;

import com.gabjuho.oilproduceplugin.events.Event;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable()
    {

        getServer().getPluginManager().registerEvents(new Event(),this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[OilProducePlugin]: Plugin is enabled!");
    }
    @Override
    public void onDisable()
    {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[OilProducePlugin]: Plugin is disabled!");
    }
}