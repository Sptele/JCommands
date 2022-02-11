package Internal.Processing;

import Internal.Managing.Commands.Command;
import Internal.Managing.Core.CommandManager;

/**
 * This class was designed with Frontend in mind. The functions it contains allows to run any command or query, statically. It would be implemented in this type of environment:<br>
 * <blockquote><pre><code>
 * onButtonClick() {
 *      CommandProcessor#runCommand(new MyCommand(), "my args", "-h help", "-e", "runtime");
 * }
 *</code></pre></blockquote>
 * This class can also be used to automate long commands, such as turning: <code>myCommand -u user -db main -p 0123 -o manager -t 14:21 -d 4/8/2021 login at this time right now</code> into simply <code>logon</code>
 * @since 1.0
 */
public class CommandProcessor extends CommandManager {
    /**
     * Runs a query into the command query. This method is intended for Frontend use. See the {@link CommandProcessor#runCommand(Command, String, String...)} command for similar commands.
     * @param query The query to run. This must contain the command name, flags, and args.
     * @see CommandProcessor#runCommand(Command, String, String...)
     * @since 1.0
     */
    public static void runQuery(String query)
    {
        new CommandProcessor().submitRequest(query);
    }

                /**
                 * Allows you to run a command statically. The flagsAndParameters varArg should be done like this: <code>"-t test", "-n name"</code>
                 * <br>The method is designed to be run in Frontend, like the example below, but this is also useful for shorthanding lengthy commands.
                 * <blockquote><code><br>
                 *     CommandProcessor#runCommand(new MyCommand(), "arguments", "-f myFirstFlag", "-t mySecondFlag");
                 * </code></blockquote>
                 * @param cmd The Command object to run, implemented like this: <code>new MyCommand(myParams)</code>
                 * @param args The arguments for the command, as a String.
                 * @param flagsAndParameters A VarArgs containing the flags and their parameters, which each String being a flag and their parameters. Each String should be: "[flags] [params]". ("-s String")
                 * @since 1.0
                 */
        public static void runCommand(Command cmd, String args, String ... flagsAndParameters) {
            // New stringbuilder
            StringBuilder fp = new StringBuilder();
            // Loop through array, append to stringbuilder
            for(String f : flagsAndParameters) fp.append(f).append(" ");
            // Submit a request to the Command Processor
            new CommandProcessor().submitRequest(cmd.getName() + " " + fp.toString() + args);
    }

    // Submits request to the Command Processor, and runs the code.
    private void submitRequest(String query) {
        // Loop through all commands
        for(Command cmd : getCommands()) {
            if (cmd.getName().equalsIgnoreCase(query.split(" ")[0])) {
                nextMsg = query;
                if(executeAllCommands())
                    return;

                break;
            }
        }

        // Check if a command was every triggered
        //   if not: return error message
        //   if so: set commandTriggered to false
        if(!commandTriggered)
            getOut().println(getNoCommandMessage());
        else
            commandTriggered = false;
    }
}
