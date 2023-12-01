package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("AppendTail")
public class AppendTail extends Command {
    public String new_content;
    public AppendTail(){}
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
