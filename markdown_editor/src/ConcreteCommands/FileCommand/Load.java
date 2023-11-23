package ConcreteCommands.FileCommand;

import Interface.Command;
import Receiver.EditTools;

public class Load extends Command {
    String file_path;
    public Load(EditTools edit_tools, String file_path) {
        super(edit_tools);
        this.file_path = file_path;
    }
    public void execute(){
        edit_tools.file_holder.setFilePath(file_path);
        edit_tools.file_holder.openMarkDownFile();
        notifyHistoryObserver();
    }

}
