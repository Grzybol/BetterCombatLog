package betterbox.bettercombatlog;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterCombatLog extends JavaPlugin {

    private CombatLogger combatLogger;

    @Override
    public void onEnable() {
        // Inicjalizacja combatLogger z nazwą Twojego pluginu
        combatLogger = new CombatLogger(this);

        getLogger().info("Plugin BetterCombatLog został włączony.");
    }
    // Słuchacz zdarzeń do śledzenia ataków graczy
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            double victimHPBefore = victim.getHealth();
            double victimHPAfter = victimHPBefore - event.getFinalDamage();
            int attackerPing = getPlayerPing(attacker); // Tu uzyskujesz ping gracza
            int victimPing = getPlayerPing(victim); // Tu uzyskujesz ping gracza
            double distance = attacker.getLocation().distance(victim.getLocation());

            combatLogger.logCombatEvent(attacker, victim, victimHPBefore, victimHPAfter, attackerPing, victimPing,
                    distance);
        }
    }
    @Override
    public void onDisable() {
        // Zamknięcie combatLogger
        if (combatLogger != null) {
            combatLogger.close();
        }

        getLogger().info("Plugin BetterCombatLog został wyłączony.");
    }
    private int getPlayerPing(Player player) {
        // Pobieranie ping gracza za pomocą bukkit api
        int ping = 0; // Domyślna wartość

        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            Object networkManager = connection.getClass().getField("networkManager").get(connection);
            ping = (int) networkManager.getClass().getMethod("getPing").invoke(networkManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ping;
    }

    // Metoda do uzyskiwania dostępu do combatLogger z innych klas
    public CombatLogger getCombatLogger() {
        return combatLogger;
    }
}
