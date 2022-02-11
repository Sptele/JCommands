package External.Commands;

import Internal.Managing.Commands.Command;
import Internal.Managing.Commands.InputEvent;

public class Add extends Command {
    public Add() {
        this.name = "addf";
        this.aliases = new String[] {"additionf", "af"};
        this.help = "Adds as many floats as provided together.";
        this.args = "[value] ...";
    }

    @Override
    protected void run(InputEvent event) {
        String[] args = event.getInputMessage().getArgs().split(" ");
        if(args.length <= 1) {
            event.replyln("You must provide at least two numbers to add together!");
            return;
        }

        float[] sum = new float[1];

        try {
            for (String arg : args) {
                sum[0] += Float.parseFloat(arg);
            }
        } catch (NumberFormatException ex) {
            event.replyln("You must provide all numbers as floats/integers!");
            return;
        }

        event.replyln("Sum: " + sum[0] + "");
    }
}
