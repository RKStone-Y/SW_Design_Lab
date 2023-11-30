package Interface;


import Observer.CommandLog;
import Receiver.Workspace;

public abstract class Command {
    protected Workspace workspace;
    protected CommandLog command_log;
    protected int command_id;

    public int getCommand_id() {
        return command_id;
    }

    public abstract boolean execute();
    public abstract boolean undo();

    // notify first, command execution later,
    // cause the content should be the original one
    public void notifyHistoryObserver(){
        workspace.file_holder.history.commandPush(this,workspace.file_holder.content);
    }
    public Command(Workspace workspace){
        this.workspace = workspace;
    }
    public Command(CommandLog command_log){ this.command_log = command_log;}
    public Command(){}
}
