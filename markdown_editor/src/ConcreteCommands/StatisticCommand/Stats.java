package ConcreteCommands.StatisticCommand;

import Interface.Command;
import Invoker.Editor;
import Observer.CommandLog;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("Stats")
public class Stats extends Command {
    public String target_file;
    public Stats(){}
    public Stats(Editor editor, String target_file){
        super(editor);
        command_id = 12;
        this.target_file = target_file;
    }
    @Override
    public boolean execute() {
        return editor.command_log.showStats(editor,target_file);
    }
    @Override
    public boolean undo() {
        return true;
    }
}
