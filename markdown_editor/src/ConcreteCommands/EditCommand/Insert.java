package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;

public class Insert extends Command {
    protected int line;
    protected String new_content;
    public Insert(Workspace workspace, int line, String new_content){
        super(workspace);
        command_id = 1;
        this.line = line;
        this.new_content = new_content;
    }
    public Insert(Workspace workspace, String new_content){
        super(workspace);
        command_id = 1;
        this.line = -1;
        this.new_content = new_content;
    }
    @Override
    public boolean undo() {
        return workspace.deleteContentFromFile(new_content);
    }
    @Override
    public boolean execute() {
        notifyHistoryObserver();
        if (line == -1) {
            return workspace.addNewContentToFile(new_content);
        }
        else {
            return workspace.addNewContentToFile(line-1, new_content);
        }
    }
}
