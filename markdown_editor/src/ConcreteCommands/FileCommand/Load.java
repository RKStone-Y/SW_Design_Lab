package ConcreteCommands.FileCommand;

import Interface.Command;
import Receiver.Workspace;

public class Load extends Command {
    String file_path;
    public Load(Workspace workspace, String file_path) {
        super(workspace);
        command_id = 7;
        this.file_path = file_path;
    }
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
