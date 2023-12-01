package ConcreteCommands.FileCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;
@JsonTypeName("Open")
public class Open extends Command {
    public List<Workspace> workspaceList = new ArrayList<>();
    public Open(Workspace edit_tools, List<Workspace> workspaceList) {
        super(edit_tools);
        command_id = 9;
        this.workspaceList.addAll(workspaceList);
    }
    public Open(){}
    @Override
    public boolean undo() {
        return true;
    }
    public boolean execute(){
        return false;

    }
}
