package Internal.Managing.Commands;

import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

/**
 * An object that contains information about the {@link Command}'s flags.<br>
 * There are three arrays: <br>
 * <pre>
 *     - noParamFlags  # the flags that have no parameters
 *     - flags  # the flags that have parameters
 *     - params  # the parameters for the flags
 * </pre>
 */
public class Flags {
    private String[] noParamFlags;
    private String[] f = new String[] {};
    private String[] args = new String[] {};

    /**
     * Creates a new flag object. This constructor takes in a VarArg that represents the flags with no parameters, such as <code>-h</code>.
     * <br>
     * <b>Important Note for flag priority:</b> flags that are specified before others will be put in higher priority, and will be called before other flags. This applies to all setter methods.<br>Also, no parameter flags will take priority over parameter flags.
     * @param flags The No Parameter flags as a VarArg.
     */
    public Flags(String ... flags)
    {
        this.noParamFlags = flags;
    }

    /**
     * Puts an arbitrary amount of flags with parameters. The parameters and flags are matched by order, so that <code>flags[0]</code> is matched with <code>params[1]</code>
     * @param flags The flags to add.
     * @return A flag object (just to chain the methods :) )
     */
    public Flags putParamsFlags(String ... flags) {
        this.f = flags;
        return this;
    }

    /**
     * Puts an arbitrary amount of parameters. The parameters and flags are matched by order, so that <code>flags[0]</code> is matched with <code>params[1]</code>
     * @param arguments The arguments to add.
     * @return A flag object (just to chain the methods :) )
     */
    public Flags putParamsArgs(String ... arguments) {
        this.args = arguments;
        return this;
    }

    /**
     * Returns the flags with parameters. Flags are matched to their parameters by order, such that a parameter at <code>parameter[0]</code> matches with its respective flag at <code>flag[0]</code>.<br>
     * If you wish to return the flags <i>matched</i> with their parameters, use the method <code>flags.match()</code>. This will return a Hashmap of Strings, with the flags as the key, and their parameter as the value, matched to each other.
     * @return A String array containing the flags with parameters.
     * @see Flags#match()
     * @since 1.0
     */
    public String[] getParamsFlags() {
        return this.f;
    }

    /**
     * Returns the parameters for their respective flags. Flags are matched to their parameters by order, such as a parameter at <code>parameter[0]</code> matches with its respective flag at <code>flag[0]</code>.<br>
     * If you wish to return the flags <i>matched</i> with their parameters, use the method {@link Flags#match()}. This will return a {@link HashMap} of Strings, with the flags as the key, and their parameter as the value, matched to each other.
     * @return A String array containing the parameters for the flags.
     * @since 1.0
     */
    public String[] getParamsArgs() {
        return this.args;
    }

    /**
     * Gets the Flags that have no parameters.
     * @return A String array that contains the flags with no parameters.
     */
    public String[] getNoParamFlags()
    {
        return this.noParamFlags;
    }

    /**
     * Returns a Hashmap with a String as the key, and a String as the value.
     * The key is the flag, and the value is that flag's respective parameter.
     * @return A Hashmap of Strings with the flag as the key and the parameter for the flag as the value.
     * @since 1.0
     */
    public HashMap<String, String> match() {
        HashMap<String, String> hm = new HashMap<>();
        for(int i = 0; i < f.length; i++)
            hm.put(f[i], args[i]);

        return hm;
    }

    /**
     * Converts this object to a string using this library's standard format: { var1=value1, ... }
     * @return A String representing this object
     */
    public String toString() {
        return new Formatter().format("{ noparamflags=%s, flags=%s, flagparams=%s }", Arrays.toString(noParamFlags), Arrays.toString(f), Arrays.toString(args)).toString();
    }

    public String JSON() {
        StringBuilder noparam = new StringBuilder();
        StringBuilder fl = new StringBuilder();
        StringBuilder par = new StringBuilder();
        for(int i = 0; i < noParamFlags.length; i++) {
            noparam.append("'").append(noParamFlags[i]).append("'");
            if(i < noParamFlags.length - 1) noparam.append(", ");
        }
        for(int i = 0; i < f.length; i++) {
            fl.append("'").append(f[i]).append("'");
            if(i < f.length - 1) fl.append(", ");
        }
        for(int i = 0; i < args.length; i++) {
            par.append("'").append(args[i]).append("'");
            if(i < args.length - 1) par.append(", ");
        }
        return new Formatter().format("{ 'noparamflags': [ %s ], 'flags': [ %s ], 'flagparams': [ %s ] }", noparam.toString(), fl.toString(), par.toString()).toString();
    }
}
