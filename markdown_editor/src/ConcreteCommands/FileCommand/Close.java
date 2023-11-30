package ConcreteCommands.FileCommand;

import Interface.Command;
import Receiver.Workspace;

import java.util.List;
public class Close extends Command {
    List<Workspace> workspaceList;
    public Close(Workspace workspace,List<Workspace> workspaceList){
        super(workspace);
        command_id = 10;
        this.workspaceList = workspaceList;
    }
    @Override
    public boolean undo() {
        return true;
    }
    @Override
    public boolean execute() {
        return workspace.closeWorkspace(workspaceList);
    }
}
