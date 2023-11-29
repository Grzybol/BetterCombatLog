package betterbox.bettercombatlog;

import org.bukkit.plugin.java.JavaPlugin;

public final class BetterCombatLog extends JavaPlugin {

    private PluginLogger pluginLogger;
    private CombatLogger combatLogger;


    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        String logFilePath = getDataFolder() + "/log.txt"; // Używając metody getDataFolder() otrzymasz folder pluginu
        pluginLogger = new PluginLogger(logFilePath);
        pluginLogger.log("Plugin has been enabled");
        combatLogger = new CombatLogger(this,pluginLogger);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        pluginLogger.log("Plugin has been disabled");
    }
}
