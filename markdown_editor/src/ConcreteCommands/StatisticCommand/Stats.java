package ConcreteCommands.StatisticCommand;

import Interface.Command;
import Observer.CommandLog;

public class Stats extends Command {
    protected String target_file;
    public Stats(CommandLog command_log, String target_file){
        super(command_log);
        command_id = 12;
        this.target_file = target_file;
    }
    @Override
    public boolean execute() {
        return command_log.showStats(target_file);
    }
    @Override
    public boolean undo() {
        return true;
    }
}
