package simplexity.commands;

import simplexity.config.TTSConfig;
import simplexity.messages.Output;

public class ReloadCommand extends Command {
    public ReloadCommand(String name, String usage) {
        super(name, usage);
    }

    @Override
    public void execute() {
        TTSConfig.getInstance().reloadConfig();
        System.out.println(Output.RELOAD_MESSAGE);
    }
}
