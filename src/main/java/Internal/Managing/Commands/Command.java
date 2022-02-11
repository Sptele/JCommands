package Internal.Managing.Commands;

import Internal.Managing.Core.CommandManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

/**
 * The Command class. To create a command, extend a normal java class with this class:<br>
 * <blockquote><code><pre>
 * public class myCommand extends Command {
 *         public myCommand() {
 *              super.name = "MyFirstCommand";
 *              super.help = "This is my first command!";
 *         }
 *
 *         protected void run(InputEvent event) {
 *              event.replyln("Hello World!");
 *         }
 * }
 * </pre></code></blockquote>
 *
 * Then, it would easily be run like this, in console:
 *
 * <blockquote><code><pre>
 * Please enter a command!
 * $ MyFirstCommand
 * Hello World!
 * $
 * </pre></code></blockquote>
 */
public abstract class Command {

    /** The default constructor for the Command object. This should not be called manually! To create a command, create a class and extend this class. */
    public Command() {}

    /** The name of the command. */
    protected String name;

    /** The aliases for the command. This is optional. */
    protected String[] aliases = new String[] {};

    /** The arguments for the command. */
    protected String args;

    /** The help String for the command. */
    protected String help;

    /** The flags for the command. Put flags with no parameters in the default constructor, and to put flags with parameters use {@link Flags#putParamsFlags(String...)} and {@link Flags#putParamsFlags(String...)}*/
    protected Flags flags = new Flags();

    /** The category for this command. You can set the name for this category, and set to whether it (and by extension, its commands) is hidden.
     * @see Category#Category(String)
     * @see Category#Category(String, boolean) */
    protected Category category = new Category();

    /**
     * The method that has the running code inside.
     * @param event The InputEvent event.
     */
    protected abstract void run(InputEvent event);

    /**
     * Executes this command.
     * @param event The InputEvent to read from.
     * @param man The executing CommandManager.
     */
    public void execute(InputEvent event, CommandManager man) {
        try {
            run(event); // Executes the run method, which runs the code inside.
        } catch (Exception ex) {
            // Exception handling
            man.getOut().print("Sorry, an error occurred. Try again!");
            if(man.isPrintStackTrace()) {
                man.getOut().println();
                ex.printStackTrace(man.getOut());
            }
        }
    }

    /**
     * Returns the name of the command. This does not return the aliases, if you want the aliases use this#getAliases().
     * @return A String that represents the name of this command.
     * @see Command#getAliases(boolean)
     * @since 1.0
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns the aliases of the command. This will return the name along with the aliases if the option is checked.
     * @param includeName Selects whether or not to include the name in the return array. If this is <code>true</code>, the name will be the at the first index of the return array.
     * @return A String array containing the aliases for this command.
     * @since 1.0
     */
    public String[] getAliases(boolean includeName) {
        // Return aliases with names
        if(includeName) {
            ArrayList<String> allNames = new ArrayList<>();
            allNames.add(name);
            allNames.addAll(Arrays.asList(this.aliases));
            return allNames.toArray(new String[allNames.size()]);
        }

        // Else, return aliases without name
        return this.aliases;
    }

    /**
     * Returns the aliases of the command.
     * @return A String array containing the aliases for this command.
     * @since 1.0
     */
    public String[] getAliases() {
        return this.aliases;
    }

    /**
     * Gets the flags of this command.
     * @return A {@link Flags} object containing all message flags and their parameters.
     */
    public Flags getFlags() { return this.flags; }

    /**
     * Gets the help message for this command.
     * @return A {@link String} containing this command's help message.
     */
    public String getHelp() { return this.help; }

    /**
     * Gets the arguments for this command.
     * @return A String representing the arguments of this command.
     */
    public String getArgs() { return this.args; }

    /**
     * Gets the category for this command.
     * @return A {@link Category} representing the category of this command.
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Converts this object to a string, using this library's standard format: { var1=value1, ... }
     * @return A String version of this object.
     */
    public String toString() {
        String arguments = "none";
        if(args != null)
            arguments = args;

        String helpmsg = "none";
        if(help != null)
            helpmsg = help;

        String namemsg = "none";
        if(name != null)
            namemsg = name;

        return new Formatter().format("{ name=%s, aliases=%s, arguments=%s, help=%s, flags=%s, category=%s }", namemsg, Arrays.toString(aliases), arguments, helpmsg, flags.toString(), category.toString()).toString();
    }

    public String JSON() {
        StringBuilder alias = new StringBuilder();
        for(int i = 0; i < aliases.length; i++) {
            alias.append("'").append(aliases[i]).append("'");
            if(i < aliases.length - 1) alias.append(", ");
        }

        return new Formatter().format("{ 'name': '%s', 'aliases': [ %s ], 'arguments': '%s', 'help': '%s', 'flags': %s, 'category': %s }", name, alias.toString(), args != null ? args : "", help != null ? args : "", flags.JSON(), category.JSON()).toString();
    }
}
