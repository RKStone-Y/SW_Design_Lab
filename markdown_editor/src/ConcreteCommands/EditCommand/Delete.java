package ConcreteCommands.EditCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Delete")
public class Delete extends Command {
    public String target_content;
    public int line;
    public String  removed_content;
    public boolean flag;
    public Delete(){}
    public Delete(Workspace workspace, String target_content){
        super(workspace);
        command_id = 4;
        this.flag =true;
        this.target_content = target_content;
    }
    public Delete(Workspace workspace, int line){
        super(workspace);
        command_id = 4;
        this.line = line-1;
        this.flag = false;
    }
    @Override
    public boolean undo() {
        if(!removed_content.isEmpty()){
            return workspace.addNewContentToFile(line,removed_content);
        }
        return false;
    }
    @Override
    public boolean execute() {
        notifyHistoryObserver();
        if(flag){
            for(int i = 0; i < workspace.file_holder.content.size();i++ ){
                String content_line = workspace.file_holder.content.get(i);
                if(content_line.contains(target_content)){
                    removed_content = content_line;
                    this.line = i;
                    break;
                }
            }
            return workspace.deleteContentFromFile(target_content);

        }
        else{
            if(line>workspace.file_holder.content.size()-1){
                System.err.println("delete: line out of bounds");
                return false;
            }
            else {
                this.removed_content = workspace.file_holder.content.get(line);
                return workspace.deleteContentFromFile(line);
            }
        }
    }
}
