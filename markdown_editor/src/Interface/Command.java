package Interface;


import Observer.CommandLog;
import Receiver.EditTools;

import java.util.List;

public abstract class Command {
    protected EditTools edit_tools;
    protected CommandLog command_log;
    public abstract void execute();

    // notify first, command execution later,
    // cause the content should be the original one
    public void notifyHistoryObserver(){
        edit_tools.file_holder.history.commandPush(this,edit_tools.file_holder.content);
    }
    public void notifyHistoryObserver(List<String> origin_content){
        edit_tools.file_holder.origin_content.clear();
        edit_tools.file_holder.origin_content.addAll(origin_content);
        edit_tools.file_holder.history.commandPush(this,edit_tools.file_holder.content);
    }

    public Command(EditTools edit_tools){
        this.edit_tools = edit_tools;
    }
    public Command(CommandLog command_log){ this.command_log = command_log;}
}
