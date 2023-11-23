package ConcreteCommands.ViewCommand;

import Interface.Command;
import Receiver.EditTools;

public class List extends Command {
    public List(EditTools edit_tools){
        super(edit_tools);
    }
    @Override
    public void execute() {
        edit_tools.showContent();
    }
}
