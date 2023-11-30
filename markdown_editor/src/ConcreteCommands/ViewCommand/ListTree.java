package ConcreteCommands.ViewCommand;

import Interface.Command;
import Receiver.Workspace;

public class ListTree extends Command {
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
