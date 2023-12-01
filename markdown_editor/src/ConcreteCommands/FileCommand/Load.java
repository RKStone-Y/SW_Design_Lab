package ConcreteCommands.FileCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Load")
public class Load extends Command {
    public String file_path;
    public Load(Workspace workspace, String file_path) {
        super(workspace);
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
        workspace.file_holder.setFilePath(file_path);
        return workspace.file_holder.openMarkDownFile();

    }

}
