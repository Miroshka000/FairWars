package miroshka.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import miroshka.manager.PlayerManager;
import miroshka.core.Config;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

public class FairWarsCommand extends Command {
    public FairWarsCommand() {
        super("duel", Config.help);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Config.help);
            return true;
        }

        Player player = (Player) sender;

        SimpleForm form = new SimpleForm("FairWars");
        SimpleFormHandler handler = (p, button) -> {
            switch (button.index) {
                case 0:
                    PlayerManager.addPlayer(player);
                    break;
                case 1:
                    PlayerManager.removePlayer(player);
                    break;
            }
        };

        form.setContent(Config.chooseOption)
                .addButton(Config.startMatch, handler)
                .addButton(Config.exitMatch, handler);

        form.setNoneHandler(p -> {
            p.sendMessage(Config.closedForm);
        });

        form.send(player);

        return false;
    }
}
