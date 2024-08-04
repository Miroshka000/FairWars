package miroshka.listeners;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import miroshka.core.Arena;
import miroshka.core.Config;
import miroshka.manager.PlayerManager;

public class BlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Arena arena = PlayerManager.getArena(event.getPlayer());
        if (arena != null) {
            event.setCancelled();
            Block block = event.getBlock();
            Vector3 vec3 = new Vector3(block.x, block.y, block.z);
            block.getLevel().setBlock(vec3, block);
            arena.blockpos.add(vec3);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Arena arena = PlayerManager.getArena(event.getPlayer());
        if (arena != null) {
            if (event.getBlock().getId() == 26) { // Assuming bed ID is 26
                event.setCancelled();
                String message;
                if (event.getPlayer().getName().equals(arena.blue.getName())) {
                    if (event.getBlock().getLocation().distance(Config.blue) > event.getBlock().getLocation().distance(Config.red)) {
                        arena.tpBlueToSpawn();
                        arena.tpRedToSpawn();
                        message = Config.prefix + Config.redBedBroken;
                        arena.blue.sendMessage(message);
                        arena.red.sendMessage(message);
                        ++arena.redBed;
                        arena.updateArena();
                    } else {
                        event.getPlayer().sendMessage(Config.prefix + Config.ownBedBreakError);
                    }
                } else if (event.getBlock().getLocation().distance(Config.blue) < event.getBlock().getLocation().distance(Config.red)) {
                    arena.tpBlueToSpawn();
                    arena.tpRedToSpawn();
                    message = Config.prefix + Config.blueBedBroken;
                    arena.blue.sendMessage(message);
                    arena.red.sendMessage(message);
                    ++arena.blueBed;
                    arena.updateArena();
                } else {
                    event.getPlayer().sendMessage(Config.prefix + Config.ownBedBreakError);
                }
            } else if (event.getBlock().getId() == 24) {
                event.setDropExp(0);
                event.setDrops(new Item[0]);
                Block block = event.getBlock();
                block.getLevel().setBlock(new Vector3(block.x, block.y, block.z), Block.get(0));
            } else {
                event.setCancelled();
            }
        }
    }
}
