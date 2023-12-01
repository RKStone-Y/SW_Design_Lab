package ConcreteCommands.LogCommand;

import Interface.Command;
import Observer.CommandLog;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("History")
public class History extends Command {
    public int line;
    public boolean isAll;
    public History(){}
    public History(CommandLog command_log, int line){
        super(command_log);
        command_id = 11;
        this.line =line;
        this.isAll =false;
    }
    public History(CommandLog command_log){
        super(command_log);
        command_id = 11;
        this.isAll = true;
    }
    @Override
    public boolean undo() {
        return true;
    }
    @Override
    public boolean execute() {
        if(isAll){
            return command_log.showTheHistory();
        }
        else {
            return command_log.showTheHistory(this.line);
        }
    }
}
