package betterbox.bettercombatlog;

import org.bukkit.entity.Player;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CombatLogger {

    private BufferedWriter logWriter;
    private Map<Player, Long> previousAttackTimes; // Mapa przechowująca czasy poprzednich ataków graczy

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

            // Inicjalizacja mapy poprzednich ataków
            previousAttackTimes = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logCombatEvent(Player attacker, Player victim, double victimHPBefore, double victimHPAfter,
                               int attackerPing, int victimPing, double attackerAttackSpeed, double distance) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = dateFormat.format(new Date());

            String alert = (distance > 5.0) ? "[ALERT] " : "";

            // Pobieranie czasu poprzedniego ataku gracza
            long previousAttackTime = getPreviousAttackTime(attacker);

            String logMessage = String.format("%s%s - Attacker: %s, Victim: %s, Victim HP: %.2f -> %.2f, " +
                            "Attacker Ping: %d, Victim Ping: %d, Attacker AttackSpeed: %.2f, Distance: %.2f, " +
                            "Time Since Previous Attack: %s",
                    alert, timestamp, attacker.getName(), victim.getName(), victimHPBefore, victimHPAfter,
                    attackerPing, victimPing, attackerAttackSpeed, distance,
                    formatTimeDifference(previousAttackTime)); // Dodaj różnicę czasu od poprzedniego ataku

            logWriter.write(logMessage);
            logWriter.newLine();
            logWriter.flush();

            // Aktualizacja czasu poprzedniego ataku gracza
            updatePreviousAttackTime(attacker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda do uzyskiwania czasu poprzedniego ataku gracza
    public long getPreviousAttackTime(Player player) {
        return previousAttackTimes.getOrDefault(player, 0L);
    }

    // Metoda do aktualizacji czasu poprzedniego ataku gracza
    public void updatePreviousAttackTime(Player player) {
        previousAttackTimes.put(player, System.currentTimeMillis());
    }

    // Metoda do formatowania różnicy czasu w czytelny sposób (HH:MM:SS)
    public String formatTimeDifference(long previousAttackTime) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - previousAttackTime;

        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
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
