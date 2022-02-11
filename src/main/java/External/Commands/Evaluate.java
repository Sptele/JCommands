package External.Commands;

import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.InputEvent;
import jdk.jshell.JShell;

public class Evaluate extends Command {
    public Evaluate() {
        this.name = "evalf";
        this.aliases = new String[] {"evaluatef", "ef"};
        this.help = "Evaluates java code.";
        this.args = "[Code in java]";
    }

    @Override
    protected void run(InputEvent event) {
        String args = event.getInputMessage().getArgs();
        if(args.equals("")) {
            event.replyln("You must provide code to evaluate!");
            return;
        }

        event.replyln("[OUTPUT BELOW]");
        JShell.create().eval(args);
    }
}
