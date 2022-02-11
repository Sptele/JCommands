package Central;

import Internal.Managing.Commands.Command;
import Internal.Managing.Core.CommandConfigurator;
import Internal.Managing.Core.CommandManager;

/**
 * A simple but modular command framework that is designed to make your command infrastructure as easy as possible.
 * The commands contain a name, flags that have parameters, flags that do not have parameters, and arguments.
 * The setup process is pretty simple:
 * Create a {@link CommandConfigurator} using the default Constructor.
 * Configure the settings to your liking.
 * And build it to create a {@link CommandManager}!
 * You then create a {@link Command}, and do what you want in it!
 *
 * @author Gautam Khajuria
 * @version 1.0
 * @since 3/27/2021
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Description of CommandCreator:");
        System.out.println(" * A simple but modular command framework that is designed to make your command infrastructure as easy as possible.\n" +
                " * The commands contain a name, flags that have parameters, flags that do not have parameters, and arguments.\n" +
                " * The setup process is pretty simple:\n" +
                " * Create a CommandConfigurator using the default Constructor.\n" +
                " * Configure the settings to your liking.\n" +
                " * And build it to create a CommandManager!\n" +
                " * You then create a Command, and do what you want in it!");
        System.out.println("Check out the github here: ");
    }
}
