package ConcreteCommands.FileCommand;

import Interface.Command;
import Invoker.Editor;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;
@JsonTypeName("Open")
public class Open extends Command {

    String fileName;

    public Open(Editor editor, String fileName) {
        super(editor);
        this.fileName = fileName;
        command_id = 9;
    }

    public Open() {
    }

    @Override
    public boolean undo() {
        return true;
    }

    public boolean execute() {
        Workspace new_workspace;
        if (fileName.equals(editor.workspace.file_holder.getFile_path())) {
            System.out.println("Already been open");
            return false;
        } else {
            new_workspace = editor.searchWorkspace(fileName);
            if (new_workspace == null) {
                System.out.println("No such workspace");
                return false;
            }
            else{
                editor.workspace = new_workspace;
                return true;
            }
        }
    }
}
