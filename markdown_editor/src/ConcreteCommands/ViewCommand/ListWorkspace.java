package ConcreteCommands.ViewCommand;

import Interface.Command;
import Receiver.Workspace;

import java.util.ArrayList;
import java.util.List;

public class ListWorkspace extends Command {
    List<Workspace> workspaceList;
    public ListWorkspace(Workspace workspace,List<Workspace> workspaceList){
        super(workspace);
        command_id = 16;
        this.workspaceList = workspaceList;
    }
    @Override
    public boolean execute() {
        if(workspaceList.isEmpty()) {
            System.out.println("list-workspace: No Open Workspace");
            return false;
        }
        for(Workspace tmp: workspaceList){
            String file_Name = tmp.file_holder.getFile_path();
            if(workspace.file_holder.getFile_path().equals(file_Name)){
                file_Name = "->" + file_Name;
            }
            else{
                file_Name = "  " + file_Name;
            }
            if(!tmp.isSaved){
                file_Name = file_Name + " *";
            }
            System.out.println(file_Name);
        }
        return true;
    }
    @Override
    public boolean undo() {
        return true;
    }
}
