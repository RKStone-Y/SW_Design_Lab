package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Undo")
public class Undo extends Command {

    public Undo(Workspace workspace){
        super(workspace);
        command_id = 5;
    }
    public Undo(){}
    @Override
    public boolean undo() {
        return true;
    }
    @Override
    public boolean execute() {
//        return workspace.withdrawCommand();
        return workspace.file_holder.history.commandPop(workspace);
    }
}
