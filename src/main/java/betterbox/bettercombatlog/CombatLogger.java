package betterbox.bettercombatlog;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatLogger implements Listener {

    private final PluginLogger pluginLogger;
    private final JavaPlugin plugin;

    public CombatLogger(JavaPlugin plugin, PluginLogger pluginLogger) {
        this.plugin = plugin;
        this.pluginLogger = pluginLogger;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Sprawdź, czy obrażenia pochodzą od gracza i skierowane są na gracza
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            double attackSpeed = attacker.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED).getValue();
            double victimHealthBefore = victim.getHealth(); // Punkty życia ofiary przed obrażeniem

            // Wykonaj obrażenia
            double damage = event.getFinalDamage();
            victim.damage(damage);

            double victimHealthAfter = victim.getHealth(); // Punkty życia ofiary po obrażeniu
            Location attackerLocation = attacker.getLocation();
            Location victimLocation = victim.getLocation();
            double distance = attackerLocation.distance(victimLocation);
            int attackerPing = getPlayerPing(attacker);
            int victimPing = getPlayerPing(victim);

            String logMessage = String.format("Attacker: %s, Attack Speed: %f, Victim: %s, Victim Health Before: %f, Victim Health After: %f, Attacker Ping: %d, Victim Ping: %d, Distance: %f",
                    attacker.getName(), attackSpeed, victim.getName(), victimHealthBefore, victimHealthAfter, attackerPing, victimPing, distance);

            // Dodaj prefix "[ALERT]" jeśli dystans między graczami jest większy niż 5 bloków
            if (distance > 5) {
                logMessage = "[ALERT] " + logMessage;
            }

            pluginLogger.log(logMessage);
        } else {
            pluginLogger.log("Either damager or entity is not a player");
        }
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
        }
        return ping;
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null) {
            String logMessage = String.format("Player %s was killed by %s", player.getName(), killer.getName());
            pluginLogger.log(logMessage);
        }
    }
}
