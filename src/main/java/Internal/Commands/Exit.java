package Internal.Commands;

import Internal.Managing.Commands.Category;
import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.Flags;
import Internal.Managing.Commands.InputEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exits the current command prompt.<br>
 * <h1>Usage:</h1>
 *     Here is a command prompt: <blockquote><code>
 *
 *         Please type a command!<br>
 *         $ exitf
 *
 *     </code></blockquote>
 *     There are multiple aliases, such as:
 *     <blockquote><code>
 *         $ exitf  # Valid exit command <br>
 *         $ ef  # Also a valid exit command <br>
 *         $ \q  # This is also a valid exit command
 *     </code></blockquote>
 *     This command also takes in a flag that represents the exit status. The flag is: "-s [status]":
 *     <blockquote><code>
 *         $ exitf -s 0  # Exits command prompt with status of 0 (no errors)<br>
 *         $ exitf -s 1  # Exits command prompt with status of 1 (error)<br>
 *     </code></blockquote>
 */
public class Exit extends Command {
    public Exit() {
        this.name = "exitf";
        this.aliases = new String[] {"ef", "\\q"};
        this.help = "Exits the Command Prompt!";
        this.flags = new Flags().putParamsFlags("-s").putParamsArgs("[status]");
        this.category = new Category("Internal");
    }

    @Override
    protected void run(InputEvent event) {
        // Get flags, create atomic integer
        HashMap<String, String> hm = event.getInputMessage().getFlagsAndArguments();
        AtomicInteger status = new AtomicInteger(0); // Default is 0

        // Check for flag is hm is not null
        if(hm != null) {
            hm.forEach((k, v) -> {
                if(k.equalsIgnoreCase("-s"))
                    status.set(Integer.parseInt(v));
            });
        }
        // Check if status is not 0, if so, print a new line (it prints the output weird otherwise)
        if(status.get() != 0)
            event.replyln();

        // Create DateTimeFormatter with pattern "dd/MM/yyyy hh:mm:ss a"
        DateTimeFormatter fmr = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a");
        String msg = "Exiting command prompt at " + fmr.format(LocalDateTime.now()) + "!"; // Create message with timestamp
        event.replyWithBorder(msg, "=", msg.length()); // Print
        System.exit(status.get()); // Exit with status
    }
}
