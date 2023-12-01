package ConcreteCommands.FileCommand;


import Interface.Command;
import Invoker.Editor;

public class Exit extends Command {
    public Exit(Editor editor){
        this.editor=editor;
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
