package ConcreteCommands.ViewCommand;

import Interface.Command;
import Receiver.Workspace;

public class DirTree extends Command {
    String target_dir;
    public DirTree(Workspace workspace, String target_dir){
        super(workspace);
        command_id = 15;
        this.target_dir = target_dir;
    }
    @Override
    public boolean execute() {
        return workspace.showWholeTree(workspace.file_holder.searchTargetDirectory(target_dir));
    }
    @Override
    public boolean undo() {
        return true;
    }
}
