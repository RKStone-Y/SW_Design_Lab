package ConcreteCommands.ViewCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("List")
public class List extends Command {
    public List(){}
    public List(Workspace workspace){
        super(workspace);
        command_id = 13;
    }
    @Override
    public boolean execute() {
        return workspace.showContent();
    }
    @Override
    public boolean undo() {
        return true;
    }
}
