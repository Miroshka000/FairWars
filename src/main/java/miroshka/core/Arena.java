package miroshka.core;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.player.PlayerTeleportEvent.TeleportCause;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import miroshka.FairWars;
import miroshka.manager.ArenaManager;
import miroshka.manager.PlayerManager;

import java.util.ArrayList;
import java.util.Iterator;

public class Arena {
    private final FairWars plugin;
    public Level level;
    public Player red;
    public Player blue;
    public int redBed = 0;
    public int blueBed = 0;
    public ArrayList<Vector3> blockpos;
    private boolean gameEnded = false;

    public Arena(Level level, Player red, Player blue, FairWars plugin) {
        this.level = level;
        this.red = red;
        this.blue = blue;
        this.plugin = plugin;
        this.blockpos = new ArrayList<>();
    }

    public void tpRedToSpawn() {
        this.red.teleport(Position.fromObject(Config.red, this.level), TeleportCause.PLUGIN);
    }

    public void tpBlueToSpawn() {
        this.blue.teleport(Position.fromObject(Config.blue, this.level), TeleportCause.PLUGIN);
    }

    public void giveItem() {
        givePlayerItem(this.red);
        givePlayerItem(this.blue);
    }

    public void updateArena() {
        if (this.redBed >= Config.beds) {
            this.red.sendMessage(Config.prefix + Config.youLose);
            this.blue.sendTitle(Config.victory);
            this.remove(true);
        } else if (this.blueBed >= Config.beds) {
            this.red.sendTitle(Config.victory);
            this.blue.sendMessage(Config.prefix + Config.youLose);
            this.remove(true);
        } else {
            Iterator<Vector3> var1 = this.blockpos.iterator();

            while (var1.hasNext()) {
                Vector3 vec3 = var1.next();
                this.level.setBlock(vec3, Block.get(0));
            }

            this.blockpos.clear();
        }
    }

    public void remove(boolean tp) {
        this.gameEnded = true;
        Position spawn = Server.getInstance().getDefaultLevel().getSafeSpawn();
        if (tp) {
            this.red.teleport(spawn);
            this.blue.teleport(spawn);
            this.red.getInventory().clearAll();
            this.blue.getInventory().clearAll();
        }

        PlayerManager.playerArena.remove(this.blue.getName());
        PlayerManager.playerArena.remove(this.red.getName());
        ArenaManager.removeArena(this);
    }

    public static void givePlayerItem(Player player) {
        player.getInventory().clearAll();
        player.getInventory().addItem(Config.kbStick, Config.block, Config.pickaxe);
    }
}
