package ConcreteCommands.ViewCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ListTree")
public class ListTree extends Command {
    public ListTree(){}
    public ListTree(Workspace workspace){
        super(workspace);
        command_id = 14;
    }


    @Override
    public boolean execute() {
        return workspace.showWholeTree(workspace.file_holder.content);
    }
    @Override
    public boolean undo() {
        return true;
    }
}
