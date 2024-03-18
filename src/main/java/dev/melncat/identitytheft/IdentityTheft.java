package dev.melncat.identitytheft;

import dev.melncat.identitytheft.command.IdentityTheftCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class IdentityTheft extends JavaPlugin {
    private IdentityTheftConfig config;
    
    public IdentityTheftConfig getITConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = new IdentityTheftConfig(getConfig());
        IdentityTheftCommand command = new IdentityTheftCommand(this);
        getCommand("identitytheft")
            .setExecutor(command);
        getCommand("identitytheft")
            .setTabCompleter(command);
        
        Bukkit.getPluginManager().registerEvents(new IdentityTheftListener(this), this);
    }
    

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
