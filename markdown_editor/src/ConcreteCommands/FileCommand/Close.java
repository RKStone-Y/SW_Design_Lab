package ConcreteCommands.FileCommand;

import Interface.Command;
import Invoker.Editor;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("Close")
public class Close extends Command {
    public Close(Editor editor){
        super(editor);
        command_id = 10;
    }
    public Close(){}
    @Override
    public boolean undo() {
        return true;
    }
    @Override
    public boolean execute() {
        return editor.closeWorkspace();
    }
}
