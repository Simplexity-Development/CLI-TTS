package simplexity.commands;

public abstract class Command {
    public String name;
    public String description;

    public Command(String name, String usage) {
        this.name = name;
        this.description = usage;
    }

    public abstract void execute();

    public String getDescription(){
        return description;
    }
    public String getName(){
        return name;
    }
}
