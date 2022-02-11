package Internal.Managing.Commands;

import Internal.Managing.Core.CommandManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

/**
 * The object containing the input message from the inputstream.<br>
 * To get the content of the input as a <code>String</code>, use the method {@link CommandMessage#getContent()}. <br>
 * All the methods inside this object get the information from the input message, such as how {@link CommandMessage#getFlagsAndArguments()} will return the flags and parameters <i>from</i> the message.
 */
public class CommandMessage {
    Command cmd;
    CommandManager man;

    private String contentMessage;
    private String[] noParamsFlags;
    private HashMap<String, String> flagsAndParams;
    private String[] everyFlag;
    private String args;

    /**
     * Initializes a new CommandMessage. This should not be manually run! Instead, use {@link InputEvent#getInputMessage()} instead instead.
     * @param cmd The command that this is associated with.
     * @param man The manager that this is associated with.
     */
    public CommandMessage(Command cmd, CommandManager man) {
        this.cmd = cmd;
        this.man = man;

        Flags flagsObj = cmd.getFlags();
        String[] flags = cmd.getFlags().getNoParamFlags();

        // String message
        this.contentMessage = man.getNextMsg();

        String[] msg = getContent().split(" ");

        // No Param Flags
        ArrayList<String> msgFlags = new ArrayList<>();

        for(String m : msg) {
            for(String f : flags) {
                if(m.equals(f)) {
                    msgFlags.add(f);
                }
            }
        }
        this.noParamsFlags = msgFlags.toArray(new String[msgFlags.size()]);

        // All flags
        ArrayList<String> allFlags = new ArrayList<>();
        String[] noParamFlags = getNoParamsFlags();

        flagsObj.match().forEach((f, value) -> {
            for (String m : msg) {
                if (m.equals(f)) {
                    allFlags.add(m);
                }
            }
        });
        allFlags.addAll(Arrays.asList(noParamFlags));

        this.everyFlag = allFlags.toArray(new String[allFlags.size()]);

        // Param Flags
        HashMap<String, String> hm = new HashMap<>();
        boolean[] error = new boolean[1];

        // Loop through all matched flags and parameters.
        flagsObj.match().forEach((f, value) -> {
            for (int i = 0; i < msg.length; i++) {
                String m = msg[i];
                if (m.equals(f)) {
                    if(i+1 < msg.length) {
                        for(String flag : getAllFlags()) {
                            if(msg[i + 1].equals(flag)) {
                                // Set to return null if there is no param for the flag.
                                error[0] = true;
                                break;
                            }
                        }
                        hm.put(f, msg[i + 1]);
                    } else {
                        // Set to return null if there is no param for the flag.
                        error[0] = true;
                        break;
                    }
                }
            }
        });

        // Return null
        if(error[0]) {
            this.flagsAndParams = null;
        } else {
            this.flagsAndParams = hm;
        }

        // Arguments
        String msgString = getContent();
        HashMap<String, String> flagsWithParams = getFlagsAndArguments();

        for(String a : cmd.getAliases(true)) if(a.equals(msgString.split(" ")[0])) msgString = msgString.substring(a.length());

        String[] finalMsg = new String[] {msgString};

        flagsWithParams.forEach((k, v) -> {
            finalMsg[0] = finalMsg[0].replace(k + " ", "");
            finalMsg[0] = finalMsg[0].replace(v + " ", "");
        });

        for(String f : flags) {
            finalMsg[0] = finalMsg[0].replace(f + " ", "");
        }

        if(finalMsg[0].split(" ").length == 1) {
            flagsWithParams.forEach((k, v) -> {
                finalMsg[0] = finalMsg[0].replace(k, "");
                finalMsg[0] = finalMsg[0].replace(v, "");
            });
            if(finalMsg[0].equals(" ")) {
                args = "";
            }
        }

        finalMsg[0] = finalMsg[0].trim();
        this.args = finalMsg[0];
    }

    /**
     * Gets the full String content from the CommandMessage.
     * @return A String that is the console message.
     */
    public String getContent() {
        return this.contentMessage;
    }

    /**
     * Returns the Flags that do not have parameters. These are flags such as <code>-h, --help</code>.
     * @return A String array containing the flags without parameters.
     */
    public String[] getNoParamsFlags() {
        return this.noParamsFlags;
    }

    /**
     * Gets flags and their arguments from the {@link Flags} object. Will return <code>null</code> if a parameter for the flag is not provided.
     * @return Either a hashmap containing the flags and their parameters in the message, or <code>null</code> if a parameter is not provided, as well as printing an error message.
     */
    public HashMap<String, String> getFlagsAndArguments() {
        return this.flagsAndParams;
    }

    /**
     * Gets the arguments of the command.
     * @return The arguments as a standard String, or an empty String ("") if there are no args.
     */
    public String getArgs() {
        return this.args;
    }

    /**
     * Gets all flags from the input message. This includes no parameter flags and parameter flags, but not parameters. Use <code>this#getFlagsAndArguments()</code> instead.
     * @return A String array containing all flags from the message.
     */
    public String[] getAllFlags() {
        return this.everyFlag;
    }

    /**
     * Converts this object to a string containing all information about an input
     * @return A String with all known information about the input message
     */
    public String toString() {
        StringBuilder noParamFlagsBuilder = new StringBuilder(), everyFlagBuilder = new StringBuilder();
        for(int i = 0; i < noParamsFlags.length; i++) {
            noParamFlagsBuilder.append(noParamsFlags[i]);
            if(i != noParamsFlags.length) noParamFlagsBuilder.append(", ");
        }
        for(int i = 0; i < everyFlag.length; i++) {
            everyFlagBuilder.append(everyFlag[i]);
            if(i != everyFlag.length) everyFlagBuilder.append(", ");
        }
        return new Formatter().format("{ message=%s, noParamsFlags=%s, flagsAndParams=%s, everyFlag=%s, args=%s }", contentMessage, noParamFlagsBuilder.toString(), flagsAndParams, everyFlagBuilder.toString(), args).toString();
    }

    /**
     * Converts this object to a JSON readable string. Useful for APIs.
     * <h1>Appearance:</h1>
     * Here is how it would look for a Command named 'MyCommand', with the flags '-t' and '-p'.
     * <blockquote><code><pre>
     * Input: $ MyCommand -t admin -p password logon
     * JSON: { 'message': 'MyCommand -t admin -p password', 'noParamsFlags': [  ], 'flagsAndParams': { '-t': 'admin', '-p': 'password' }, 'everyFlag': [ '-t', '-p'], 'args': 'logon' }
     * </pre></code></blockquote>
     * @return A JSON formatted String.
     */
    public String JSON() {
        StringBuilder npf = new StringBuilder();
        StringBuilder ef = new StringBuilder();
        StringBuilder obj = new StringBuilder();
        for(int i = 0; i < noParamsFlags.length; i++) {
            npf.append("'").append(noParamsFlags[i]).append("'");
            if(i != noParamsFlags.length - 1) npf.append(", ");
        }
        for(int i = 0; i < everyFlag.length; i++) {
            ef.append("'").append(everyFlag[i]).append("'");
            if(i != everyFlag.length - 1) ef.append(", ");
        }
        for(int i = 0; i < flagsAndParams.size(); i++) {
            obj.append("'").append(cmd.getFlags().getParamsFlags()[i]).append("': ");
            obj.append("'").append(flagsAndParams.get(cmd.getFlags().getParamsFlags()[i])).append("'");
            if(i != flagsAndParams.size() - 1) obj.append(", ");
        }
        return new Formatter().format("{ 'message': '%s', 'noParamsFlags': [ %s ], 'flagsAndParams': { %s }, 'everyFlag': [ %s ], 'args': '%s' }", contentMessage, npf.toString(), obj.toString(), ef.toString(), args).toString();
    }
}
