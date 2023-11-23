package ConcreteCommands.ViewCommand;

import Interface.Command;
import Invoker.Editor;
import Receiver.EditTools;

import java.security.DigestException;

public class DirTree extends Command {
    String target_dir;
    public DirTree(EditTools edit_tools,String target_dir){
        super(edit_tools);
        this.target_dir = target_dir;
    }
    @Override
    public void execute() {
        edit_tools.showWholeTree(edit_tools.file_holder.searchTargetDirectory(target_dir));
    }
}
