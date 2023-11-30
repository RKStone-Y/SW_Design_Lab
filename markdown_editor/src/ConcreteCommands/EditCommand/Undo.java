package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;

public class Undo extends Command {

    public Undo(Workspace workspace){
        super(workspace);
        command_id = 5;
    }
    @Override
    public boolean undo() {
        return true;
    }
    @Override
    public boolean execute() {
//        return workspace.withdrawCommand();
        return workspace.file_holder.history.commandPop();
    }
}
