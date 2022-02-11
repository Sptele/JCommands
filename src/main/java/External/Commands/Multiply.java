package External.Commands;

import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.InputEvent;

public class Multiply extends Command {
    public Multiply() {
        this.name = "multiplyf";
        this.aliases = new String[] {"mf", "mulf"};
        this.args = "[value] ...";
        this.help = "Multiplies as many floats as inputted!";
    }

    @Override
    protected void run(InputEvent event) {
        String[] args = event.getInputMessage().getArgs().split(" ");
        if(args.length <= 1) {
            event.replyln("You must provide at least two numbers to add together!");
            return;
        }

        float[] prod = new float[1];
        prod[0] = Float.parseFloat(args[0]);

        try {
            for (int i = 1; i < args.length; i++) {
                prod[0] *= Float.parseFloat(args[i]);
            }
        } catch (NumberFormatException ex) {
            event.replyln("You must provide all numbers as floats/integers!");
            return;
        }

        event.replyln("Sum: " + prod[0]);
    }
}
