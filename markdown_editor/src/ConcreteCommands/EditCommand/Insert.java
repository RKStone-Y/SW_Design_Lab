package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.EditTools;

public class Insert extends Command {
    protected int line;
    protected String new_content;
    public Insert(EditTools edit_tools,int line, String new_content){
        super(edit_tools);
        this.line = line;
        this.new_content = new_content;
    }
    public Insert(EditTools edit_tools,String new_content){
        super(edit_tools);
        this.line = -1;
        this.new_content = new_content;
    }
    @Override
    public void execute() {
        notifyHistoryObserver();
        if (line == -1) {
            edit_tools.addNewContentToFile(new_content);
        }
        else {
            edit_tools.addNewContentToFile(line-1, new_content);
        }
    }
}
