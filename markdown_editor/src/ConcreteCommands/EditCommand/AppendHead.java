package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.EditTools;


public class AppendHead extends Command {
    protected String new_content;

    public static final int FIRST_LINE = 0;
    public AppendHead(EditTools edit_tools, String new_content){
        super(edit_tools);
        this.new_content = new_content;
    }

    @Override
    public void execute() {
        notifyHistoryObserver();
        edit_tools.addNewContentToFile(FIRST_LINE,new_content);
    }
}
