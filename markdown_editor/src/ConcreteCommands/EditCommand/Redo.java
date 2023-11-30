package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;

public class Redo extends Command {
    public Redo(Workspace workspace){
        super(workspace);
        command_id = 6;
    }
    @Override
    public boolean undo() {
        return true;
    }
    @Override
    public boolean execute() {
        return workspace.redoLastCommand();
    }
}
