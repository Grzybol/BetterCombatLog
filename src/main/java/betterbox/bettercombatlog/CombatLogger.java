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

    public CombatLogger(BetterCombatLog plugin) {
        try {
            // Utwórz folder pluginu, jeśli nie istnieje
            File pluginFolder = new File(plugin.getDataFolder(), "logs");
            if (!pluginFolder.exists()) {
                pluginFolder.mkdirs();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
            String fileName = pluginFolder.getPath() + File.separator + dateFormat.format(new Date()) + ".txt";
            logWriter = new BufferedWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logCombatEvent(Player attacker, Player victim, double victimHPBefore, double victimHPAfter,
                               int attackerPing, int victimPing, double distance) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = dateFormat.format(new Date());

            String alert = (distance > 5.0) ? "[ALERT] " : "";

            String logMessage = String.format("%s%s - Attacker: %s, Victim: %s, Victim HP: %.2f -> %.2f, " +
                            "Attacker Ping: %d, Victim Ping: %d, Distance: %.2f",
                    alert, timestamp, attacker.getName(), victim.getName(), victimHPBefore, victimHPAfter,
                    attackerPing, victimPing, distance);

            logWriter.write(logMessage);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (logWriter != null) {
            try {
                logWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
