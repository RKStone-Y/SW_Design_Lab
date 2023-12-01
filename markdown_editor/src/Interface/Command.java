package Interface;


import ConcreteCommands.EditCommand.*;
import ConcreteCommands.FileCommand.Close;
import ConcreteCommands.FileCommand.Load;
import ConcreteCommands.FileCommand.Open;
import ConcreteCommands.FileCommand.Save;
import ConcreteCommands.LogCommand.History;
import ConcreteCommands.StatisticCommand.Stats;
import ConcreteCommands.ViewCommand.*;
import Invoker.Editor;
import Observer.CommandLog;
import Receiver.Workspace;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AppendHead.class, name = "AppendHead"),
        @JsonSubTypes.Type(value = AppendTail.class, name = "AppendTail"),
        @JsonSubTypes.Type(value = Delete.class, name = "Delete"),
        @JsonSubTypes.Type(value = Insert.class, name = "Insert"),
        @JsonSubTypes.Type(value = Undo.class, name = "Undo"),
        @JsonSubTypes.Type(value = Redo.class, name = "Redo"),
        @JsonSubTypes.Type(value = Close.class, name = "Close"),
        @JsonSubTypes.Type(value = Load.class, name = "Load"),
        @JsonSubTypes.Type(value = Open.class, name = "Open"),
        @JsonSubTypes.Type(value = Save.class, name = "Save"),
        @JsonSubTypes.Type(value = History.class, name = "History"),
        @JsonSubTypes.Type(value = Stats.class, name = "Stats"),
        @JsonSubTypes.Type(value = DirTree.class, name = "DirTree"),
        @JsonSubTypes.Type(value = List.class, name = "List"),
        @JsonSubTypes.Type(value = ListTree.class, name = "ListTree"),
        @JsonSubTypes.Type(value = ListWorkspace.class, name = "ListWorkspace"),
        @JsonSubTypes.Type(value = Ls.class, name = "Ls")
})

public abstract class Command {
    protected Workspace workspace;
    protected Editor editor;
    protected CommandLog command_log;
    protected int command_id;
    public int getCommand_id() {
        return command_id;
    }
    public void setWorkspace(Workspace workspace){
        this.workspace = workspace;
    }

    public abstract boolean execute();
    public abstract boolean undo();

    // notify first, command execution later,
    // cause the content should be the original one
    public void notifyHistoryObserver(){
        workspace.file_holder.history.commandPush(this);
    }
    public Command(Workspace workspace){
        this.workspace = workspace;
    }
    public Command(CommandLog command_log){
        this.command_log = command_log;
    }
    public Command(Editor editor){
        this.editor = editor;
    }
    public Command(){}
}
