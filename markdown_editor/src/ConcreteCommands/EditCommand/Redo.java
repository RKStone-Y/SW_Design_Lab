package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.EditTools;

public class Redo extends Command {
    public Redo(EditTools edit_tools){
        super(edit_tools);
    }

    @Override
    public void execute() {
        edit_tools.redoLastCommand();
    }
}
