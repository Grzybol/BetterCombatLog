package betterbox.bettercombatlog;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class BetterCombatLog extends JavaPlugin {

    private PluginLogger pluginLogger;
    private CombatLogger combatLogger;


    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Utwórz unikalną nazwę pliku logów z nazwą pluginu i datą
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String logFileName = "log_" + getDescription().getName() + "_" + dateFormat.format(new Date()) + ".txt";
        File logFile = new File(getDataFolder(), logFileName);

        // Utwórz nowy plik logów
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        pluginLogger = new PluginLogger(logFile.getPath());
        pluginLogger.log("Plugin has been enabled");
        combatLogger = new CombatLogger(this, pluginLogger);
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        pluginLogger.log("Plugin has been disabled");
    }
}
