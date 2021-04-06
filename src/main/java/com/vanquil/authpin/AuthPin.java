package com.vanquil.authpin;

import com.google.common.io.ByteStreams;
import com.vanquil.authpin.database.DatabaseManager;
import com.vanquil.authpin.database.PinDatabase;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;

public final class AuthPin extends Plugin {

    private static File file;
    private static Configuration config;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        try {
            DatabaseManager.connect(getConfig());
            getLogger().info("Successfully connected to database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

        // logout all players logged in already
        for(ProxiedPlayer player : getProxy().getPlayers()) {
            if(getConfig().getStringList("Staffs").contains(player.getName())) {
                PinDatabase pinDatabase = new PinDatabase(player);
                if(pinDatabase.isLoggedIn())
                    pinDatabase.logout();
                pinDatabase = null;
            }
        }

        // disconnect database
        DatabaseManager.disconnect();
    }

    public void saveDefaultConfig() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        file = new File(getDataFolder(), "config.yml");
        try {
            if(!file.exists()) {
                file.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(file)) {
                    ByteStreams.copy(is, os);
                }
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);


        }catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not load config.yml");
        }
    }

    public static Configuration getConfig() {
        return config;
    }
}
