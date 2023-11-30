package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;

public class AppendTail extends Command {
    protected String new_content;
    public AppendTail(Workspace workspace, String new_content){
        super(workspace);
        command_id = 3;
        this.new_content = new_content;
    }
    @Override
    public boolean undo() {
        return workspace.deleteContentFromFile(new_content);
    }
    @Override
    public boolean execute() {
        notifyHistoryObserver();
        return workspace.addNewContentToFile(new_content);
    }
}
