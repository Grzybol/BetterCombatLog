package betterbox.bettercombatlog;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public final class BetterCombatLog extends JavaPlugin {

    private CombatLogger combatLogger;
    private Map<Player, Double> playerAttackSpeeds; // Mapa przechowująca prędkości ataku graczy

    @Override
    public void onEnable() {
        // Inicjalizacja combatLogger z nazwą Twojego pluginu
        combatLogger = new CombatLogger(this);

        // Inicjalizacja mapy prędkości ataku graczy
        playerAttackSpeeds = new HashMap<>();

        getLogger().info("Plugin BetterCombatLog został włączony.");
    }

    @Override
    public void onDisable() {
        // Zamknięcie combatLogger
        if (combatLogger != null) {
            combatLogger.close();
        }

        getLogger().info("Plugin BetterCombatLog został wyłączony.");
    }

    // Metoda do uzyskiwania dostępu do combatLogger z innych klas
    public CombatLogger getCombatLogger() {
        return combatLogger;
    }

    // Metoda do uzyskiwania prędkości ataku gracza
    public double getPlayerAttackSpeed(Player player) {
        return playerAttackSpeeds.getOrDefault(player, 1.0);
    }

    // Metoda do aktualizacji prędkości ataku gracza
    public void setPlayerAttackSpeed(Player player, double attackSpeed) {
        playerAttackSpeeds.put(player, attackSpeed);
    }
}
