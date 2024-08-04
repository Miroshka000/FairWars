package miroshka.manager;

import cn.nukkit.Player;
import miroshka.core.Arena;
import miroshka.core.Config;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    public static Map<String, Arena> playerArena = new HashMap<>();
    public static Player queuedPlayer;

    public static void addPlayer(Player player) {
        Arena arena = getArena(player);
        if (arena == null && !isQueuePlayer(player)) {
            if (queuedPlayer == null) {
                queuedPlayer = player;
            } else {
                ArenaManager.createArena(queuedPlayer, player);
                queuedPlayer = null;
            }

            player.sendMessage(Config.prefix + Config.queueJoined);
        } else {
            player.sendMessage(Config.prefix + Config.alreadyInQueue);
        }
    }

    public static void removePlayer(Player player) {
        if (isQueuePlayer(player)) {
            queuedPlayer = null;
        } else {
            Arena arena = getArena(player);
            if (arena == null) {
                player.sendMessage(Config.prefix + Config.notInQueue);
                return;
            }

            arena.remove(true);
        }

        player.sendMessage(Config.prefix + Config.queueLeft);
    }

    public static boolean isQueuePlayer(Player player) {
        return queuedPlayer != null && queuedPlayer.equals(player);
    }

    public static Arena getArena(Player player) {
        return playerArena.get(player.getName());
    }
}
