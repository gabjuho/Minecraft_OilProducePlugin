package com.gabjuho.oilproduceplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class YamlManager {
    private final Main plugin; // 메인클래스 객체
    private static YamlManager instance = new YamlManager(Main.getPlugin(Main.class)); //yaml 매니저는 싱글톤으로 생성
    private FileConfiguration loc = null;
    private File locFile = null;

    private YamlManager(Main plugin) // 생성자 막아놓음 (싱글톤 패턴으로 인한)
    {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() // 데이터 리로드
    {
        if(this.locFile == null)
            this.locFile = new File(this.plugin.getDataFolder(), "loc.yml");

        this.loc = YamlConfiguration.loadConfiguration(this.locFile);
        InputStream defaultStream = this.plugin.getResource("loc.yml");
        if(defaultStream != null)
        {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.loc.setDefaults(defaultConfig);
        }
    }

    public static YamlManager getInstance() // 싱글톤 인스턴스 반환
    {
        if(instance == null)
            instance = new YamlManager(Main.getPlugin(Main.class));
        return instance;
    }

    public FileConfiguration getLoc()
    {
        if(this.loc == null)
            reloadConfig();

        return this.loc;
    }

    public void saveConfig() // 데이터 저장
    {
        if(this.loc == null || this.locFile == null)
            return;

        try
        {
            this.getLoc().save(this.locFile);
        }catch(IOException e)
        {
            plugin.getLogger().log(Level.SEVERE,"data 파일이 저장되지 않았습니다. -> " + this.locFile,e);
        }
    }

    public void saveDefaultConfig() // 데이터 디폴트 저장
    {
        if(this.locFile == null)
            this.locFile = new File(this.plugin.getDataFolder(), "loc.yml");

        if(!this.locFile.exists())
        {
            this.plugin.saveResource("loc.yml",false);
        }
    }

}
