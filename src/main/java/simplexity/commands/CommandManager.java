package simplexity.commands;

import simplexity.messages.Errors;

import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public boolean runCommand(String command) {
        if (command.startsWith("--") && !commands.containsKey(command)) {
            System.out.println(Errors.UNKNOWN_COMMAND.replace("%command%", command));
            return true;
        }
        if (!commands.containsKey(command)){
            return false;
        }
        commands.get(command).execute();
        return true;
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }
}
