package ConcreteCommands.FileCommand;


import Interface.Command;
import Invoker.Editor;
import Receiver.Workspace;

public class Exit extends Command {
    public Exit(Editor editor){
        super(editor);
    }
    @Override
    public boolean undo() {
        return true;
    }
    public Exit(){}
    @Override
    public boolean execute() {
       return editor.exitEditor();
    }
}
