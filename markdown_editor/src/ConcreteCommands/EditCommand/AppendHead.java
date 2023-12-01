package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("AppendHead")
public class AppendHead extends Command {
    public String new_content;

    public static final int FIRST_LINE = 0;
    public AppendHead(Workspace workspace, String new_content){
        super(workspace);
        command_id = 2;
        this.new_content = new_content;
    }
    public AppendHead(){}

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
