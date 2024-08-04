package miroshka;

import cn.nukkit.plugin.PluginBase;
import miroshka.core.Config;
import miroshka.command.FairWarsCommand;
import miroshka.listeners.BlockListener;

public class FairWars extends PluginBase {

    public static FairWars plugin;
    public static String jarDir;

    @Override
    public void onEnable() {
        plugin = this;
        jarDir = this.getFile().getPath();
        Config.load();
        this.getServer().getCommandMap().register("duel", new FairWarsCommand());
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }
}
