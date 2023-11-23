package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.EditTools;

public class Undo extends Command {

    public Undo(EditTools edit_tools){
        super(edit_tools);
    }

    @Override
    public void execute() {//todo save should make some change
        edit_tools.withdrawCommand();
    }
}
