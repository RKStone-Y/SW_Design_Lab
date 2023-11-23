package ConcreteCommands.StatisticCommand;

import Interface.Command;
import Observer.CommandLog;
import Receiver.EditTools;

public class Stats extends Command {
    protected String target_file;
    public Stats(CommandLog command_log, String target_file){
        super(command_log);
        this.target_file = target_file;
    }
    @Override
    public void execute() {
        command_log.showStats(target_file);
    }
}
