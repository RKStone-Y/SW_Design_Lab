package ConcreteCommands.FileCommand;

import Interface.Command;
import Invoker.Editor;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Load")
public class Load extends Command {
    public String file_path;
    public Load(Editor editor, String file_path) {
        super(editor);
        this.workspace =editor.workspace;
        command_id = 7;
        this.file_path = file_path;
    }
    public Load(){}
    @Override
    public boolean undo() {
        return true;
    }
    @Override
    public boolean execute(){
        Workspace new_workspace = new Workspace();
        new_workspace.file_holder.setFilePath(file_path);
        Workspace tmp = editor.searchWorkspace(file_path);
        if(tmp!=null){
            editor.workspace = tmp;
            System.out.println(file_path+ " Already been loaded");
            return false;
        }
        if(new_workspace.file_holder.openMarkDownFile()){
            editor.workspace = new_workspace;
            editor.workspace_list.add(new_workspace);
            return true;
        }
        else {
            return false;
        }
    }

}
