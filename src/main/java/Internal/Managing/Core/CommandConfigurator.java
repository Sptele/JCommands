package Internal.Managing.Core;

import External.Commands.*;
import Internal.Commands.Help;
import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.InputEvent;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * A Configurator for the CommandManager. Use this object's {@link CommandConfigurator#build()} method to build a new configured {@link CommandManager}.
 * <br><h1>Usage:</h1>
 *     Initialization of this object just uses the standard constructor:<br><blockquote><code><pre>
 * CommandConfigurator configurator = new CommandConfigurator();
 *     </pre></code></blockquote>
 *     You can configure it using the setter methods:<br><blockquote><code><pre>
 * configurator.setStartMessage("Enter a command!");
 * configurator.setInputPrefix("[INP]");
 * configurator.setPrintStream(MyCustomPrintStream);
 * configurator.addCommand(myBestCommand());
 *     </code></blockquote></pre>
 *     Finally, you should build a new, configured {@link CommandManager} using this object's {@link CommandConfigurator#build()}:<br><blockquote><code><pre>
 * CommandManager manager = configurator.build();
 *     </pre></code></blockquote>
 */
public class CommandConfigurator {

    /* Variables and Objects */
    private int commandCheck = 1;
    private InputStream inp = System.in;
    private PrintStream out = System.out;
    private ArrayList<Command> cmds = new ArrayList<>();
    private String startMessage = "Please enter a command! (Type helpf to access the help command)";
    private String inputPrefix = "$";
    private String flagNoParameterMessage = "You must provide a %s for the flag %s!";
    private BiConsumer<CommandManager, InputEvent> helpConsumer;
    private boolean printOutputToPrintstream = true;
    private String noCommandMessage = "There is no command matching that name! Use the command help to return the help message!";
    private boolean printStackTrace = true;
    private boolean snapInternalCommands = false;

    /* Constructor */
    /**
     * Builds the Command Configurator and generates a new CommandManager to use. This method MUST be registered for the framework to work.
     * @return A fully configured CommandManager that is ready to use.
     * @since 1.0
     */
    public CommandManager build() { return new CommandManager(commandCheck, inp, cmds, startMessage, out, inputPrefix, flagNoParameterMessage, noCommandMessage, helpConsumer, printOutputToPrintstream, printStackTrace, snapInternalCommands); }

    /* Getters and Setters */
    /**
     * Gets the amount of time between console input checks in seconds.
     * @return The amount of time between console input checks in seconds.
     */
    public int getCommandCheck() {
        return commandCheck;
    }

    /**
     * Sets how often the framework will check for console input in seconds. <b>DEFAULT = 1</b>
     * @param commandCheck The amount of time between console input checks in seconds.
     */
    public void setCommandCheck(int commandCheck) {
        this.commandCheck = commandCheck;
    }

    /**
     * Gets the current Input Stream.
     * @return The current InputStream.
     */
    public InputStream getInp() {
        return inp;
    }

    /**
     * Adds a command to the Command Manager.
     * @param command The command to add
     */
    public void addCommand(Command command) {
        cmds.add(command);
    }

    /**
     * Gets all the commands registered to the Command Configurator.
     * @return An array containing all the commands registered to the configurator. This does NOT include the commands registered in the manager.
     */
    public Command[] getCommands()
    {
        return cmds.toArray(new Command[cmds.size()]);
    }

    /**
     * Sets the input stream for the console application. THIS INPUT STREAM MUST BE TEXT BASED, OTHERWISE IT WILL NOT WORK! <b>DEFAULT = <code>System.in</code></b>
     * @param inp the input stream for the console application.
     */
    public void setInp(InputStream inp, boolean printOutputToPrintstream) {
        this.inp = inp;
        this.printOutputToPrintstream = printOutputToPrintstream;
    }

    /**
     * Gets the starting message for the program. This is the message that is automatically sent at program start.
     * @return A String representing the starting message.
     */
    public String getStartMessage() {
        return startMessage;
    }

    /**
     * Sets the starting message for the program. Default is: <code>"Please enter a command!"</code>
     * @param startMessage The message to send on start.
     */
    public void setStartMessage(String startMessage) {
        this.startMessage = startMessage;
    }

    /**
     * Returns the printstream that the framework will print output messages to. Default is: <code>System.out</code>
     * @return A Printstream object that is the printstream to use.
     */
    public PrintStream getOut() {
        return out;
    }

    /**
     * Sets the printstream that the framework will print output messages to. Default is: <code>System.out</code>
     * @param out The PrintStream to set.
     */
    public void setOut(PrintStream out) {
        this.out = out;
    }

    /**
     * Sets the command prefix. This is prefixed before all inputs (with a whitespace): <code>$ [input]</code>. Default is <code>$</code>.
     * @param prefix The prefix to set.
     */
    public void setInputPrefix(String prefix)
    {
        this.inputPrefix = prefix;
    }

    /**
     * Gets the command prefix.
     * @return The prefix.
     */
    public String getInputPrefix()
    {
        return this.inputPrefix;
    }

    /**
     * The no parameter error message. This uses placeholders.
     * @return The Error message.
     * @see CommandConfigurator#setFlagNoParameterMessage(String)
     */
    public String getFlagNoParameterMessage() {
        return flagNoParameterMessage;
    }

    /**
     * Sets a custom help consumer to use for the command <code>help</code>. If this is not set, than the default will be used.
     * @param run The Consumer to run code inside.
     * @see Help
     */
    public void setHelpConsumer(BiConsumer<CommandManager, InputEvent> run)
    {
        this.helpConsumer = run;
    }

    /**
     * Gets the {@link BiConsumer} for a custom help command. This will return <code>null</code> if a help consumer is not set.
     * @return A {@link BiConsumer} representing the help command.
     */
    public BiConsumer<CommandManager, InputEvent> getHelpConsumer() {
        return this.helpConsumer;
    }

    /**
     * Sets the error message for if there is no flag parameter. The message uses placeholders to represent the flag and values: <code>%s</code>. The flag will be the first <code>%s</code> is the flag, and the second <code>%s</code> is the arguments, ie: <br><code>-f, arguments</code> is <code>%s, %s</code><br> This uses the same formatting scheme as printf, though it is more simple.
     * @param flagNoParameterMessage The message with the placeholders.
     */
    public void setFlagNoParameterMessage(String flagNoParameterMessage) {
        this.flagNoParameterMessage = flagNoParameterMessage;
    }

    /**
     * Gets the error message for when the specified message does not exist.
     * @return A {@link String} representing the error message.
     */
    public String getNoCommandMessage() {
        return noCommandMessage;
    }

    /**
     * Sets the error message for when the specified message does not exist.
     * @param noCommandMessage The error message.
     */
    public void setNoCommandMessage(String noCommandMessage) {
        this.noCommandMessage = noCommandMessage;
    }

    /**
     * Gets whether or not Internal Commands are enabled.
     * @return Whether or not Internal Commands are enabled.
     */
    public boolean isUseInternalCommands() {
        return this.snapInternalCommands;
    }

    /**
     * Sets whether or not an internal command command library to the {@link CommandManager}. Default is <code>false</code>.<br>
     * These are the following commands in the internal library: (They have an 'f' suffix after the name)<br>
     * {@link Add} (addf)<br>
     * {@link Subtract} (subf)<br>
     * {@link Multiply} (mulf)<br>
     * {@link Divide} (divf)<br>
     * {@link Evaluate} (evalf)<br>
     * @param InternalCommands Whether or not to enable an internal command library.
     */
    public void setUseInternalCommands(boolean InternalCommands) {
        this.snapInternalCommands = InternalCommands;
    }
}
