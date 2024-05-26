package simplexity.commands;

import simplexity.Main;

import java.util.Arrays;

public class HelpCommand extends Command{
    public HelpCommand(String name, String usage) {
        super(name, usage);
    }

    @Override
    public void execute() {
        System.out.println("Help command");
        for (Command command : Main.getCommandManager().getCommands().values()) {
            System.out.println(command.getName() + " - " + command.getDescription());
        }
    }
}
