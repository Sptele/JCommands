package Internal.Commands;

import Internal.Managing.Commands.Category;
import Internal.Managing.Core.CommandManager;
import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.InputEvent;

/**
 * The help command for the framework's commands.
 *
 * <h1>Usage:</h1>
 * Note: This will only go over the default help consumer.
 * <br>
 * This command will print a list of all the commands inside of a border:
 * <blockquote><code><pre>
 * $ helpf # suffix 'f' is used in all internal commands
 * -------------------------------------------------
 * Commands:
 *    MyFirstCommand:
 *      Aliases: mfc, firstCommand
 *      Help: My First Command!
 *      Flags with parameters:
 *          #1 -f flag
 * -------------------------------------------------
 * </pre></code></blockquote>
 */
public class Help extends Command {

    CommandManager man;

    public Help(CommandManager man)
    {
        this.name = "helpf";
        this.aliases = new String[] {"hf"};
        this.help = "Returns information about all of the commands!";
        this.category = new Category("Internal");

        this.man = man;
    }

    @Override
    protected void run(InputEvent event) {
        // Runs the custom help consumer.
        if(man.getHelpConsumer() != null) {
            man.getHelpConsumer().accept(man, event);
            return;
        }

        // Runs the default help consumer.
        event.replyln("-------------------------------------------------");
        event.replyln("Commands:");
        for(Command cmd : man.getCommands()) {
            if(cmd.getCategory().isHidden()) continue;
            // Command Name
            event.replyln("   " + cmd.getName().toUpperCase() + ":");
            // Aliases
            if(cmd.getAliases() != null) {
                StringBuilder aliases = new StringBuilder();
                for(int i = 0; i < cmd.getAliases().length; i++) {
                    aliases.append(cmd.getAliases()[i]);
                    if(i != cmd.getAliases().length - 1) aliases.append(", ");
                }
                event.replyln("      Aliases: " + aliases.toString() + "");
            }
            // Category
            String msg = cmd.getCategory().getName() != null ? "      Category: " + cmd.getCategory().getName() : "      Category: none";
            event.replyln(msg);
            // Help
            if(cmd.getHelp() != null) {
                event.replyln("      Help: " + cmd.getHelp() + "");
            }
            // Arguments
            if(cmd.getArgs() != null) {
                event.replyln("      Arguments: " + cmd.getArgs());
            }
            // Flags with no parameters
            if(cmd.getFlags().getNoParamFlags().length > 0) {
                event.replyln("      Flags with no parameters:");
                int count = 0;
                for(String f : cmd.getFlags().getNoParamFlags()) {
                    count++;
                    event.replyln("         #" + count + ": " + f);
                }
            }
            // Flags with parameters
            if(cmd.getFlags().getParamsFlags().length > 0) {
                event.replyln("      Flags with parameters:");
                for(int i = 0; i < cmd.getFlags().getParamsFlags().length; i++) {
                    String flag = cmd.getFlags().getParamsFlags()[i];
                    String args = cmd.getFlags().getParamsArgs()[i];

                    event.replyln("         #" + (i + 1) + ": " + flag + " "+ args);
                }
            }
        }
        event.replyln("-------------------------------------------------");
    }
}
