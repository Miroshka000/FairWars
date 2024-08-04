package miroshka.core;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.Vector3;
import miroshka.FairWars;
import miroshka.utils.OtherUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class Config {
    public static String prefix;
    public static String title;
    public static int beds;
    public static Vector3 red;
    public static Vector3 blue;
    public static Item kbStick;
    public static Item pickaxe;
    public static Item block;
    public static String help;
    public static String chooseOption;
    public static String startMatch;
    public static String exitMatch;
    public static String closedForm;
    public static String youLose;
    public static String victory;
    public static String queueJoined;
    public static String alreadyInQueue;
    public static String notInQueue;
    public static String queueLeft;
    public static String againstPlayer;
    public static String redBedBroken;
    public static String blueBedBroken;
    public static String ownBedBreakError;
    public static String gameStarted;
    public static String youAreFightingAgainst;

    public static void load() {
        File configFile = new File(FairWars.plugin.getDataFolder().getPath() + "/config.yml");
        if (!configFile.exists()) {
            FairWars.plugin.getLogger().warning("Config file not found, creating default config.yml");
            OtherUtils.readJar("config.yml", FairWars.jarDir, FairWars.plugin.getDataFolder().getPath() + "/config.yml");
        }

        try (InputStream input = new FileInputStream(configFile)) {
            Yaml yaml = new Yaml(new Constructor(Map.class));
            Map<String, Object> config = yaml.load(input);

            prefix = (String) config.getOrDefault("prefix", "[FairWars]");
            help = (String) config.getOrDefault("help", "Use /duel to start a duel.");
            chooseOption = (String) config.getOrDefault("chooseOption", "Choose an option:");
            startMatch = (String) config.getOrDefault("startMatch", "Start Match");
            exitMatch = (String) config.getOrDefault("exitMatch", "Exit Match");
            closedForm = (String) config.getOrDefault("closedForm", "Form closed.");
            youLose = (String) config.getOrDefault("youLose", "You lose!");
            victory = (String) config.getOrDefault("victory", "Victory!");
            queueJoined = (String) config.getOrDefault("queueJoined", "You have joined the queue.");
            alreadyInQueue = (String) config.getOrDefault("alreadyInQueue", "You are already in the queue.");
            notInQueue = (String) config.getOrDefault("notInQueue", "You are not in the queue.");
            queueLeft = (String) config.getOrDefault("queueLeft", "You have left the queue.");
            againstPlayer = (String) config.getOrDefault("againstPlayer", "You are fighting against ");
            redBedBroken = (String) config.getOrDefault("redBedBroken", "Red's bed has been broken!");
            blueBedBroken = (String) config.getOrDefault("blueBedBroken", "Blue's bed has been broken!");
            ownBedBreakError = (String) config.getOrDefault("ownBedBreakError", "You can't break your own bed!");
            gameStarted = (String) config.getOrDefault("gameStarted", "Game Started!");
            youAreFightingAgainst = (String) config.getOrDefault("youAreFightingAgainst", "You Are Fighting Against");

            Map<String, Object> arena = (Map<String, Object>) config.get("arena");
            if (arena != null) {
                beds = (int) arena.getOrDefault("beds", 1);
                Map<String, Object> redPos = (Map<String, Object>) arena.get("red");
                red = new Vector3(((Number) redPos.get("x")).doubleValue(), ((Number) redPos.get("y")).doubleValue(), ((Number) redPos.get("z")).doubleValue());
                Map<String, Object> bluePos = (Map<String, Object>) arena.get("blue");
                blue = new Vector3(((Number) bluePos.get("x")).doubleValue(), ((Number) bluePos.get("y")).doubleValue(), ((Number) bluePos.get("z")).doubleValue());
            } else {
                FairWars.plugin.getLogger().error("Arena section is missing in config.yml");
                Server.getInstance().getPluginManager().disablePlugin(FairWars.plugin);
                return;
            }

            title = (String) config.getOrDefault("title", "FairWars");

            kbStick = new Item(369, 0, 1);
            kbStick.addEnchantment(Enchantment.get(12).setLevel((int) config.getOrDefault("kblevel", 1)));
            pickaxe = new Item(278, 0, 1);
            block = new Item(24, 0, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
