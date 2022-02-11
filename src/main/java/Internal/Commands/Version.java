package Internal.Commands;

import Internal.Managing.Commands.Category;
import Internal.Managing.Core.CommandManager;
import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.InputEvent;

/**
 * Returns the version of the framework currently. It has a border around it:
 * <code><blockquote><pre>
 * ============================================
 * Current version of Command_Framework is: 1.0
 * ============================================
 * </pre></blockquote></code>
 */
public class Version extends Command {
    public Version() {
        this.name = "versionf";
        this.aliases = new String[] {"vf"};
        this.help = "Gets the current version of the framework!";
        this.category = new Category("Internal");
    }

    @Override
    protected void run(InputEvent event) {
        // Creates message, prints it in a border
        String msg = "Current version of CommandCreator is: " + CommandManager.VERSION;
        event.replyWithBorder(msg, "=", msg.length());
    }
}
