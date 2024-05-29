package simplexity.commands;

import simplexity.Main;
import simplexity.messages.Output;

public class HelpCommand extends Command{
    public HelpCommand(String name, String usage) {
        super(name, usage);
    }

    @Override
    public void execute() {
        System.out.println(Output.HELP_HEADER);
        for (Command command : Main.getCommandManager().getCommands().values()) {
            System.out.println(Output.HELP_COMMAND_MESSAGE.replace("%command_name%", command.getName())
                    .replace("%command_description%", command.getDescription()));
        }
    }
}
