package ConcreteCommands.ViewCommand;

import Interface.Command;
import Invoker.Editor;
import Receiver.Workspace;

public class Ls extends Command {
    public Ls(Workspace workspace){
        super(workspace);
        command_id = 17;
    }
    @Override
    public boolean execute() {
        return Editor.showDirectory("md_file",0);
    }
    @Override
    public boolean undo() {
        return true;
    }
}
