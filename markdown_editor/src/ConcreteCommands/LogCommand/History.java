package ConcreteCommands.LogCommand;

import Interface.Command;
import Invoker.Editor;
import Observer.CommandLog;
import Receiver.EditTools;

public class History extends Command {
    int line;
    boolean isAll;
    public History(CommandLog command_log, int line){
        super(command_log);
        this.line =line;
        this.isAll =false;
    }
    public History(CommandLog command_log){
        super(command_log);
        this.isAll = true;
    }
    @Override
    public void execute() {
        if(isAll){
            command_log.showTheHistory();
        }
        else {
            command_log.showTheHistory(this.line);
        }
    }
}
