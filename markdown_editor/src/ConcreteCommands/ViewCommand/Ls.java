package ConcreteCommands.ViewCommand;

import Interface.Command;
import Invoker.Editor;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Ls")
public class Ls extends Command {
    public Ls(){}
    public Ls(Workspace workspace){
        super(workspace);
        command_id = 17;
    }
    private String getFileName(){
        String origin_name =workspace.file_holder.getFile_path();
        int lastSlashIndex = origin_name.lastIndexOf("/");

        // 如果存在斜杠，则返回斜杠之后的内容；否则返回原始字符串
        if (lastSlashIndex != -1 && lastSlashIndex < origin_name.length() - 1) {
            return origin_name.substring(lastSlashIndex + 1);
        } else {
            return origin_name;
        }
    }
    @Override
    public boolean execute() {
        return Editor.showDirectory("md_file",getFileName(),0);
    }
    @Override
    public boolean undo() {
        return true;
    }
}
