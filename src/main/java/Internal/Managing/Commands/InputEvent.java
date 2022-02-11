package Internal.Managing.Commands;

import Internal.Managing.Core.CommandManager;

import java.util.Formatter;

/**
 * An Event that stores all possible information about an Input.
 *<br>
 * This is passed in through the {@link Command#run(InputEvent)} method as a parameter. To get the Input message, use {@link InputEvent#getInputMessage()}.
 * This will return a {@link CommandMessage}, that contains information about the Input message/
 */
public class InputEvent {
    Command cmd;
    CommandManager man;

    /**
     * Initializes a new InputEvent to use in Commands.
     * @param cmd The command that this InputEvent is associated with.
     * @param man The Command Manager that manages this Input Event
     */
    public InputEvent(Command cmd, CommandManager man) {
        this.cmd = cmd;
        this.man = man;
    }

    /**
     * Gets the input message from the input stream as a {@link CommandMessage}. This object contains information about the input message, such as the message, arguments, and flags.
     * @return A {@link CommandMessage} containing information about the input message.
     */
    public CommandMessage getInputMessage()
    {
        return new CommandMessage(cmd, man);
    }


    /**
     * Replies to the console input and then terminates the line. This works like System#out#println.
     * @param message The message to print.
     */
    public void replyln(String message) {
        man.getOut().println(message);
    }

    /**
     * Replies to the input message on the printstream that was specified in the configurator.
     * @param message The message to print.
     */
    public void reply(String message)
    {
        man.getOut().print(message);
    }

    /**
     * Requests to the Command Manager to wait for a new input to be sent.
     * @return The input message that was prompted by this wait requests.
     */
    public String waitForNextEvent()  {
        if(man.getPrintOutputToPrintStream())
            man.getOut().print(man.getInputPrefix() + " ");

        String msg = man.NewLine();
        CommandManager.commandTriggered = true;

        return msg;
    }

    /**
     * Waits for a new input event, with a prompt message.
     * @param promptMessage A message to display before waiting.
     * @return A String that represents the input of the next input.
     */
    public String waitForNextEvent(String promptMessage)  {
        this.replyln(promptMessage);
        if(man.getPrintOutputToPrintStream())
            man.getOut().print(man.getInputPrefix() + " ");

        String msg = man.NewLine();
        CommandManager.commandTriggered = true;

        return msg;
    }

    /**
     * Prints a message with a border around it.
     * @param message The message to print inside the border.
     * @param borderChar The character of the border.
     * @param borderLength The length for the border.
     */
    public void replyWithBorder(String message, String borderChar, int borderLength) {
        StringBuilder line = new StringBuilder(borderLength);
        line.append(String.valueOf(borderChar).repeat(Math.max(0, borderLength)));

        this.replyln(line.toString());
        this.replyln(message);
        this.replyln(line.toString());
    }

    /**
     * Prints a message with a border around it, with different top and bottom border lengths.
     * @param message The message to print inside the border.
     * @param borderChar The character of the border.
     * @param topBorderLength The length of the top border line.
     * @param bottomBorderLength The length of the bottom border line.
     */
    public void replyWithBorder(String message, String borderChar, int topBorderLength, int bottomBorderLength) {
        this.replyln(String.valueOf(borderChar).repeat(Math.max(0, topBorderLength)));
        this.replyln(message);
        this.replyln(String.valueOf(borderChar).repeat(Math.max(0, bottomBorderLength)));
    }

    /**
     * Prints a message with a border around it, with different top and bottom borders.
     * @param message The message to print inside the border.
     * @param topBorderChar The character of the top border line.
     * @param bottomBorderChar The character of the bottom border line.
     * @param topBorderLength The length of the top border line.
     * @param bottomBorderLength The length of the bottom border line.
     */
    public void replyWithBorder(String message, String topBorderChar, String bottomBorderChar, int topBorderLength, int bottomBorderLength) {
        this.replyln(String.valueOf(topBorderChar).repeat(Math.max(0, topBorderLength)));
        this.replyln(message);
        this.replyln(String.valueOf(bottomBorderChar).repeat(Math.max(0, bottomBorderLength)));
    }

    /**
     * Prints a message with a border around it.
     * @param message The message to print inside the border.
     * @param topBorderChar The character of the top border line.
     * @param bottomBorderChar The character of the bottom border line.
     * @param borderLength The length of the entire border.
     */
    public void replyWithBorder(String message, String topBorderChar, String bottomBorderChar, int borderLength) {
        this.replyln(String.valueOf(topBorderChar).repeat(Math.max(0, borderLength)));
        this.replyln(message);
        this.replyln(String.valueOf(bottomBorderChar).repeat(Math.max(0, borderLength)));
    }

    /** Prints a new, empty line to the print stream. */
    public void replyln() {
        man.getOut().println();
    }

    /**
     * Prints a debug message to console. This will have a "[DEBUG]" prefix in front of it.
     * @param message The message to print.
     */
    public void debug(String message) {
        this.replyln("[DEBUG] " + message);
    }

    /**
     * Converts this object to a string using this library's standard format: { var1=value1, ... }
     * @return A string representing this object.
     */
    public String toString() {
        return new Formatter().format("{ command=%s, manager=%s }", cmd.toString(), man.toString()).toString();
    }
}
