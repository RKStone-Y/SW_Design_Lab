package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;


public class AppendHead extends Command {
    protected String new_content;

    public static final int FIRST_LINE = 0;
    public AppendHead(Workspace workspace, String new_content){
        super(workspace);
        command_id = 2;
        this.new_content = new_content;
    }

    @Override
    public boolean undo() {
        return workspace.deleteContentFromFile(new_content);
    }

    @Override
    public boolean execute() {
        notifyHistoryObserver();
        return workspace.addNewContentToFile(FIRST_LINE,new_content);
    }
}
