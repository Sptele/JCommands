package External.Commands;

import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.InputEvent;

public class Subtract extends Command {
    public Subtract() {
        this.name = "subf";
        this.aliases = new String[] {"subtractf", "subtractionf", "sf"};
        this.help = "Subtracts as many floats as inputted!";
        this.args = "[value] ...";
    }

    @Override
    protected void run(InputEvent event) {
        String[] args = event.getInputMessage().getArgs().split(" ");
        if(args.length <= 1) {
            event.replyln("You must provide at least two numbers to add together!");
            return;
        }

        float[] diff = new float[1];
        diff[0] += Float.parseFloat(args[0]);

        try {
            for (int i = 1; i < args.length; i++) {
                diff[0] -= Float.parseFloat(args[i]);
            }
        } catch (NumberFormatException ex) {
            event.replyln("You must provide all numbers as floats/integers!");
            return;
        }

        event.replyln("Difference: " + diff[0]);
    }
}
