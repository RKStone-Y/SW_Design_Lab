package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.EditTools;

public class Delete extends Command {
    public String target_content;
    public int line;
    public boolean flag;
    public Delete(EditTools edit_tools,String target_content){
        super(edit_tools);
        this.flag =true;
        this.target_content = target_content;
    }
    public Delete(EditTools edit_tools,int line){
        super(edit_tools);
        this.line = line;
        this.flag = false;
    }

    @Override
    public void execute() {
        notifyHistoryObserver();
        if(flag){
            edit_tools.deleteContentFromFile(target_content);
        }
        else{
            edit_tools.deleteContentFromFile(line-1);
        }
    }
}
