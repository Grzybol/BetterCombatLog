package betterbox.bettercombatlog;


import org.bukkit.entity.Player;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CombatLogger {

    private BufferedWriter logWriter;
    private final File logFile;
    private PluginLogger pluginLogger;

    public CombatLogger(BetterCombatLog plugin,PluginLogger pluginLogger) {
        pluginLogger.log(PluginLogger.LogLevel.DEBUG,"CombatLogger called");
        this.pluginLogger =pluginLogger;
        try {
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"CombatLogger: creating logs folder");
            // Utwórz folder pluginu, jeśli nie istnieje
            File pluginFolder = new File(plugin.getDataFolder(), "logs");
            if (!pluginFolder.exists()) {
                pluginLogger.log(PluginLogger.LogLevel.DEBUG,"CombatLogger logs folder created");
                pluginFolder.mkdirs();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date date = new Date();
            String fileName = "BetterCombatLog_"+formatter.format(date) + ".log";
            logFile = new File(pluginFolder, fileName);

            try {
                // Jeśli plik nie istnieje, to go utworzymy
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
            } catch (IOException e) {
                plugin.getLogger().severe("PluginLogger: Could not create log file! "+e.getMessage());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
            String fileName = pluginFolder.getPath() + File.separator + dateFormat.format(new Date()) + ".txt";
            logWriter = new BufferedWriter(new FileWriter(fileName, true));
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"CombatLogger AC log file created.");
        } catch (IOException e) {
            pluginLogger.log(PluginLogger.LogLevel.ERROR,"CombatLogger: "+e.getMessage());
        }
    }

    public void logCombatEvent(Player attacker, Player victim, double victimHPBefore, double victimHPAfter,
                               int attackerPing, int victimPing, double distance) {
        pluginLogger.log(PluginLogger.LogLevel.DEBUG,"CombatLogger: logCombatEvent called with parameters |attacker: "+attacker.getName()+"|victim: "+victim.getName()+"|victimHPBefore: "+victimHPBefore+"|victimHPAfter: "+victimHPAfter+"|attackerPing: "+attackerPing+"|victimPing: "+victimPing+"|distance: "+distance);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = dateFormat.format(new Date());

            String alert = (distance > 5.0) ? "[ALERT] " : "";

            String logMessage = String.format("%s%s - Attacker: %s, Victim: %s, Victim HP: %.2f -> %.2f, " +
                            "Attacker Ping: %d, Victim Ping: %d, Distance: %.2f",
                    alert, timestamp, attacker.getName(), victim.getName(), victimHPBefore, victimHPAfter,
                    attackerPing, victimPing, distance);
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            pluginLogger.log(PluginLogger.LogLevel.ERROR,"CombatLogger: logCombatEvent"+e.getMessage());
        }
    }

    public void close() {
        if (logWriter != null) {
            try {
                logWriter.close();
            } catch (IOException e) {
                pluginLogger.log(PluginLogger.LogLevel.ERROR,"CombatLogger: close "+e.getMessage());
            }
        }
    }
}
