package ConcreteCommands.StatisticCommand;

import Interface.Command;
import Observer.CommandLog;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("Stats")
public class Stats extends Command {
    public String target_file;
    public Stats(){}
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
