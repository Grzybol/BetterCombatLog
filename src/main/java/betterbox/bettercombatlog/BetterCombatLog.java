package betterbox.bettercombatlog;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;
import java.util.Set;

public final class BetterCombatLog extends JavaPlugin {

    private CombatLogger combatLogger;
    private PluginLogger pluginLogger;
    private ExtendedConfigManager configManager;

    @Override
    public void onEnable() {
        Set<PluginLogger.LogLevel> defaultLogLevels = EnumSet.of(PluginLogger.LogLevel.INFO,PluginLogger.LogLevel.DEBUG, PluginLogger.LogLevel.WARNING, PluginLogger.LogLevel.ERROR);
        pluginLogger = new PluginLogger(getDataFolder().getAbsolutePath(), defaultLogLevels,this);
        pluginLogger.log(PluginLogger.LogLevel.INFO,"BetterElo: onEnable: Starting BetterElo plugin");
        // Inicjalizacja combatLogger z nazwą Twojego pluginu
        combatLogger = new CombatLogger(this,pluginLogger);
        configManager = new ExtendedConfigManager(this, pluginLogger);
        getLogger().info("Plugin BetterCombatLog został włączony.");
    }
    // Słuchacz zdarzeń do śledzenia ataków graczy
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: onEntityDamageByEntity called with parameters attacker "+event.getDamager().getName()+" | victim: "+event.getEntity().getName());
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            double victimHPBefore = victim.getHealth();
            double victimHPAfter = victimHPBefore - event.getFinalDamage();
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing: calling getPlayerPing(attacker)");
            int attackerPing = getPlayerPing(attacker); // Tu uzyskujesz ping gracza
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing: attackerPing "+attackerPing);
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing: calling getPlayerPing(victim)");
            int victimPing = getPlayerPing(victim); // Tu uzyskujesz ping gracza
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing: victimPing "+victimPing);
            double distance = attacker.getLocation().distance(victim.getLocation());
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing: calling logCombatEvent(attacker, victim, victimHPBefore, victimHPAfter, attackerPing, victimPing,\n" +
                    " distance)");
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
        pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing called with parameters "+player.getName());
        // Pobieranie ping gracza za pomocą bukkit api
        int ping = 0; // Domyślna wartość

        try {
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing calling player.getClass().getMethod");
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing calling entityPlayer.getClass().getField");
            Object connection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing calling connection.getClass().getField");
            Object networkManager = connection.getClass().getField("networkManager").get(connection);
            pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing calling networkManager.getClass().getMethod");
            ping = (int) networkManager.getClass().getMethod("getPing").invoke(networkManager);
        } catch (Exception e) {
            pluginLogger.log(PluginLogger.LogLevel.ERROR,"BetterCombatLog: getPlayerPing "+e.getMessage());
        }
        pluginLogger.log(PluginLogger.LogLevel.DEBUG,"BetterCombatLog: getPlayerPing return PING "+ping);
        return ping;
    }

    // Metoda do uzyskiwania dostępu do combatLogger z innych klas
    public CombatLogger getCombatLogger() {
        return combatLogger;
    }
}
