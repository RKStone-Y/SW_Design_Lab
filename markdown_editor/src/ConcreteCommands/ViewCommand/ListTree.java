package ConcreteCommands.ViewCommand;

import Interface.Command;
import Receiver.EditTools;

public class ListTree extends Command {
    public ListTree(EditTools edit_tools){
        super(edit_tools);
    }


    @Override
    public void execute() {
        edit_tools.showWholeTree(edit_tools.file_holder.content);
    }
}
