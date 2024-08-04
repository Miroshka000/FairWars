package miroshka.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.level.Position;
import java.util.Timer;
import java.util.TimerTask;
import miroshka.core.Arena;
import miroshka.manager.PlayerManager;

public class PlayerListener implements Listener {
    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            Arena arena = PlayerManager.getArena(player);
            if (arena != null) {
                if (event.getCause().equals(DamageCause.VOID)) {
                    if (player.getName().equals(arena.blue.getName())) {
                        arena.tpBlueToSpawn();
                    } else {
                        arena.tpRedToSpawn();
                    }

                    event.setCancelled();
                } else {
                    event.setDamage(-1.0F);
                }
            }
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onInteract(PlayerInteractEvent event) {
        Arena arena = PlayerManager.getArena(event.getPlayer());
        if (arena != null) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getBlock().getId() == 26) {
                event.setCancelled();
            }

            event.getPlayer().getFoodData().setLevel(20);
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onQuit(final PlayerQuitEvent event) {
        if (PlayerManager.isQueuePlayer(event.getPlayer())) {
            PlayerManager.queuedPlayer = null;
        }

        final Arena arena = PlayerManager.getArena(event.getPlayer());
        if (arena != null) {
            (new Timer()).schedule(new TimerTask() {
                public void run() {
                    Position spawn = Server.getInstance().getDefaultLevel().getSafeSpawn();
                    if (event.getPlayer().getName().equals(arena.blue.getName())) {
                        arena.red.teleport(spawn);
                        arena.red.getInventory().clearAll();
                    } else {
                        arena.blue.teleport(spawn);
                        arena.blue.getInventory().clearAll();
                    }

                    arena.remove(false);
                }
            }, 1000L);
        }

    }
}
