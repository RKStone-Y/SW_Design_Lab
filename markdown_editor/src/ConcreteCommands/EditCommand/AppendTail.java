package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.EditTools;

public class AppendTail extends Command {
    protected String new_content;
    public AppendTail(EditTools edit_tools,String new_content){
        super(edit_tools);
        this.new_content = new_content;
    }
    @Override
    public void execute() {
        notifyHistoryObserver();
        edit_tools.addNewContentToFile(new_content);
    }
}
