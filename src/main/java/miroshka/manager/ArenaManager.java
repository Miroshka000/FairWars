package miroshka.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import miroshka.FairWars;
import miroshka.core.Arena;
import miroshka.core.Config;
import miroshka.utils.OtherUtils;

public class ArenaManager {
    public static ArrayList<Arena> arenas = new ArrayList<>();

    public static void check() {
        File file = new File(FairWars.plugin.getDataFolder().getPath() + "/map/");
        if (!file.exists()) {
            FairWars.plugin.getLogger().warning("Map Folder NOT Found! Please put rush map to " + FairWars.plugin.getDataFolder().getPath() + "/map/");
            Server.getInstance().getPluginManager().disablePlugin(FairWars.plugin);
        }

        if (!file.isDirectory()) {
            FairWars.plugin.getLogger().warning("Map Folder INCORRECT! Please check if map folder is wrong!");
            Server.getInstance().getPluginManager().disablePlugin(FairWars.plugin);
        }
    }

    public static void createArena(Player red, Player blue) {
        try {
            String levelName = getRandomLevelName();
            OtherUtils.copyDir(FairWars.plugin.getDataFolder().getPath() + "/map/", "./worlds/" + levelName + "/");
            Server.getInstance().loadLevel(levelName);
            Level level = Server.getInstance().getLevelByName(levelName);
            level.stopTime();
            level.setTime(0);
            level.setAutoSave(false);
            final Arena arena = new Arena(level, red, blue, FairWars.plugin);
            PlayerManager.playerArena.put(blue.getName(), arena);
            PlayerManager.playerArena.put(red.getName(), arena);
            arenas.add(arena);
            arena.tpRedToSpawn();
            arena.tpBlueToSpawn();
            arena.giveItem();
            red.sendMessage(Config.gameStarted);
            blue.sendMessage(Config.gameStarted);
            red.sendMessage(Config.prefix + Config.youAreFightingAgainst + " " + blue.getName());
            blue.sendMessage(Config.prefix + Config.youAreFightingAgainst + " " + red.getName());
            arena.updateArena();
            (new Timer()).schedule(new TimerTask() {
                public void run() {
                    arena.red.getInventory().removeItem(new Item[]{Config.block});
                    arena.level.dropItem(arena.red, Config.block);
                    arena.blue.getInventory().removeItem(new Item[]{Config.block});
                    arena.level.dropItem(arena.blue, Config.block);
                }
            }, 1000L);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public static void removeArena(Arena arena) {
        try {
            arenas.remove(arena);
            String levelName = arena.level.getFolderName();
            Server.getInstance().unloadLevel(arena.level);
            OtherUtils.delDir("./worlds/" + levelName + "/");
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public static String getRandomLevelName() {
        return UUID.randomUUID().toString().substring(0, 10).replaceAll("-", "").toUpperCase();
    }
}
