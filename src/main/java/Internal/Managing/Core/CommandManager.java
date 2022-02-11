package Internal.Managing.Core;

import External.Commands.*;
import Internal.Commands.Help;
import Internal.Commands.Exit;
import Internal.Commands.Version;
import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.InputEvent;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * The manager for all Commands. This object stores a lot of information about Commands, and runs all commands.
 * This object should <i>not</i> be initialized using the default Constructor. Instead, use the CommandConfigurator object's {@link CommandConfigurator#build()}. This will return a {@link CommandManager} that has been configured
 * to the way that it was set in the configurator. Using the default constructor contains a lot of configurations: {@link CommandManager#CommandManager(int, InputStream, ArrayList, String, PrintStream, String, String, String, BiConsumer, boolean, boolean, boolean)}.<br>
 * This also has a default constructor, but it has no commands.<br><br>
 * <h1>Init:</h1>
 * Any of the commands requiring this object should be added <i>after</i> this object has been built, and passed in as a parameter:
 * <blockquote><code><pre>
 * CommandConfigurator configurator = new CommandConfigurator();
 * CommandManager manager = configurator.build();
 * manager.addCommand(new MyFirstCommand(manager));
 * </pre></code></blockquote>
 * This can then be used in those commands:
 * <blockquote><code><pre>
 * public class MyFirstCommand {
 *     CommandManager manager;
 *     public MyFirstCommand(CommandManager manager) {
 *         super.name = "Command";
 *
 *         this.manager = manager;
 *     }
 *
 *     .@Override
 *     protected void run(InputEvent event) {
 *         event.replyln(manager.getCommands());
 *     }
 * }
 * </pre></code></blockquote>
 */
public class CommandManager {

    /** The boolean that controls whether or not a command is triggered. */
    public static boolean commandTriggered = false;

    /** The String that is the latest input message. */
    protected static String nextMsg;

    /** The version of the framework. */
    public final static String VERSION = "1.0";

    // Variables //
    private int commandCheck;
    private InputStream inp;
    private static PrintStream out;
    private static ArrayList<Command> commands = new ArrayList<>() {};
    private String startMessage;
    private String inputPrefix;
    private static String flagNoParameterMessage;
    private BiConsumer<CommandManager, InputEvent> helpConsumer;
    private static String noCommandMessage;
    private boolean printOutputToPrintstream;
    private boolean printStackTrace;
    private boolean snapInternalCommands;

    /**
     * Instantiates a new CommandManager. This constructor SHOULD NOT be manually called, instead use {@link CommandConfigurator#build()}.
     * @param commandCheck How often to check the input stream for input.
     * @param inp The input stream to check for inputs.
     * @param commands The list of commands that have already been registered.
     * @param startMessage The message to send when the program has been started.
     * @param out The printstream to send output to.
     * @param inputPrefix The prefix to put in front of inputs.
     * @param flagNoParameterMessage the error message to display when a flag parameter is not sent.
     * @param helpConsumer A custom help consumer to display when the help command is run.
     * @param noCommandMessage The message to print if there is no command matching that name.
     * @param printOutputToPrintstream Controls whether or not to print to the print stream.
     * @param printStackTrace Controls whether or not to print the stack trace when an unhandled exception is thrown.
     * @param snapInternalCommands Controls whether or not to use internal Commands. See {@link CommandConfigurator#setUseInternalCommands(boolean)} for more information.
     */
    public CommandManager(int commandCheck, InputStream inp, ArrayList<Command> commands, String startMessage, PrintStream out, String inputPrefix, String flagNoParameterMessage, String noCommandMessage, BiConsumer<CommandManager, InputEvent> helpConsumer, boolean printOutputToPrintstream, boolean printStackTrace, boolean snapInternalCommands) {
        // Init variables
        this.commandCheck = commandCheck;
        this.inp = inp;
        this.out = out;
        this.commands = commands;
        this.startMessage = startMessage;
        this.inputPrefix = inputPrefix;
        this.flagNoParameterMessage = flagNoParameterMessage;
        this.helpConsumer = helpConsumer;
        this.noCommandMessage = noCommandMessage;
        this.printOutputToPrintstream = printOutputToPrintstream;
        this.printStackTrace = printStackTrace;
        this.snapInternalCommands = snapInternalCommands;

        // Init the Command Manager
        this.init();
    }

    /**
     * A default constructor for the Command Manager.
     */
    public CommandManager() {}

    /**
     * Gets the custom help consumer. If this is not set, it will return null.
     * @return A Help Consumer, or null if it is not set.
     */
    public BiConsumer<CommandManager, InputEvent> getHelpConsumer() {
        return this.helpConsumer;
    }

    /**
     * Gets whether or not printing output to the printstream is enabled.
     * @return Whether or not printing output to the printstream is enabled.
     */
    public boolean getPrintOutputToPrintStream() {
        return this.printOutputToPrintstream;
    }

    /**
     * Gets the input prefix. Default is '$'.
     * @return A <code>String</code> representing the input prefix.
     */
    public String getInputPrefix() {
        return this.inputPrefix;
    }

    /**
     * Gets how long the duration between input checks is.
     * @return an integer representing the duration between input checks.
     */
    public int getCommandCheck() {
        return this.commandCheck;
    }

    /**
     * Gets the input stream that is set in the CommandConfigurator.
     * @return An InputStream that is the CommandConfigurator.
     */
    public InputStream getInp() {
        return this.inp;
    }

    /**
     * Adds a command to the Command Manager, in case it is needed after the Command Manager is instantiated. 
     * @param command The command to add.
     */
    public void addCommand(Command command)  {
        commands.add(command);
    }

    /**
     * Gets an {@link Command} array, containing all of the commands that have been registered to the {@link CommandManager}.
     * @return A {@link Command} array containing all of the registered commands.
     */
    public Command[] getCommands() {
        return commands.toArray(new Command[commands.size()]);
    }

    /**
     * Gets the latest input message.
     * @return A string representing the latest input message.
     */
    public String getNextMsg()
    {
        return nextMsg;
    }

    /**
     * Gets the {@link PrintStream} to print output to.
     * @return A {@link PrintStream} object that represents the printstream to print to.
     */
    public PrintStream getOut() {
        return out;
    }

    /**
     * Skips to a new line, and returns the command.
     * @return The command in console.
     */
    public String NewLine() {
        Scanner scan = new Scanner(inp);
        out.print("");
        return scan.nextLine();
    }

    private void init()  {
        // Add internal commands
        this.addCommand(new Help(this));
        this.addCommand(new Version());
        this.addCommand(new Exit());

        /* Add-on commands */
        if(snapInternalCommands) {
            this.addCommand(new Add());
            this.addCommand(new Evaluate());
            this.addCommand(new Subtract());
            this.addCommand(new Multiply());
            this.addCommand(new Divide());
        }

        // Print the start message
        if(printOutputToPrintstream)
            out.println(startMessage);

        // Create the SEC (timer), run the run method.
        ScheduledExecutorService sec = new ScheduledThreadPoolExecutor(1);
        sec.scheduleAtFixedRate(() -> { if(run()) run(); }, 0, commandCheck, TimeUnit.SECONDS);
    }

    private boolean run() {
        // Print input prefix
        if(printOutputToPrintstream)
            out.print(inputPrefix + " ");

        // Set next message to command input
        nextMsg = NewLine();
        // Execute all commands; check if a command was run.
        executeAllCommands();
        anyCommand();

        return true;
    }

    /** Executes all commands that are registered. */
    protected boolean executeAllCommands() {
        for(Command c : getCommands()) {
            for(String a : c.getAliases(true)) {
                if (nextMsg.split(" ")[0].equalsIgnoreCase(a)) {
                    InputEvent event = new InputEvent(c, this);
                    commandTriggered = true;
                    c.execute(event, this);
                    return true;
                }
            }
        }

        return false;
    }

    private static void anyCommand() {
        if(!commandTriggered)
            out.println(noCommandMessage);
        else
            commandTriggered = false;
    }

    /**
     * Gets the message for the no parameter flag error message.
     * @return A string representing the error message.
     */
    public String getFlagNoParameterMessage() {
        return flagNoParameterMessage;
    }

    /**
     * Gets whether or not exceptions should print their stacktrace.
     * @return A boolean indicating whether or not exceptions should print their stacktrace.
     */
    public boolean isPrintStackTrace() {
        return printStackTrace;
    }

    /**
     * Gets the NoCommandExists Error message.
     * @return A {@link String} representing the No Command Message.
     */
    public static String getNoCommandMessage() {
        return noCommandMessage;
    }

    /**
     * Returns the parent {@link CommandManager}.
     * @return this object {@link CommandManager}.
     */
    protected CommandManager Super() {
        return this;
    }

    /**
     * Sets a new name for the category set. This affects ALL categories with the category name.
     * @param category The category's name.
     * @param newName The name you want to change them to.
     */
    public void setNameForAllCategories(String category, String newName) {
        for(Command c : getCommands()) {
            if (c.getCategory().getName() == null) continue;
            if (c.getCategory().getName().equals(category))
                c.getCategory().setName(newName);
        }
    }

    /**
     * Sets whether the category is hidden. This affects ALL categories with the name.
     * @param category The category to change.
     * @param hidden Whether or not to set the Commands to shown.
     */
    public void setHiddenForAllCategories(String category, boolean hidden) {
        for(Command c : getCommands()) {
            if (c.getCategory().getName() == null) continue;
            if (c.getCategory().getName().equals(category))
                c.getCategory().setHidden(hidden);
        }
    }

    /**
     * Converts this object to a string, in this library's average string format ( "{ setting1=value1, ...}" )
     * @return A String in average string format.
     */
    public String toString() {
        if(helpConsumer != null)
            return new Formatter().format("{ commands=%s, commandcheck=%s, inputstream=%s, printstream=%s, startmessage=%s, inputprefix=%s, flagnoparametermessage=%s, helpconsumer=%s, nocommandmessage=%s, printoutputtoprintstream=%s, printstacktrace=%s, snapinternalcommands=%s }", Arrays.toString(commands.toArray(new Command[commands.size()])), commandCheck, inp.toString(), out.toString(), startMessage, inputPrefix, flagNoParameterMessage, helpConsumer.toString(), noCommandMessage, printOutputToPrintstream, printStackTrace, snapInternalCommands).toString();
        else
            return new Formatter().format("{ commands=%s, commandcheck=%s, inputstream=%s, printstream=%s, startmessage=%s, inputprefix=%s, flagnoparametermessage=%s, helpconsumer=default, nocommandmessage=%s, printoutputtoprintstream=%s, printstacktrace=%s, snapinternalcommands=%s }", Arrays.toString(commands.toArray(new Command[commands.size()])), commandCheck, inp.toString(), out.toString(), startMessage, inputPrefix, flagNoParameterMessage, noCommandMessage, printOutputToPrintstream, printStackTrace, snapInternalCommands).toString();
    }
}
