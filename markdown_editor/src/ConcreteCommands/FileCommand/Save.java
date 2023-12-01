package ConcreteCommands.FileCommand;

import Interface.Command;
import Receiver.Workspace;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("Save")
public class Save extends Command {
    // 保存Markdown内容回到文件
    public Save(){}
    public Save(Workspace workspace){
        super(workspace);
        command_id = 8;
    }

    @Override
    public boolean undo() {
        return true;
    }
    @Override
    public boolean execute() {
        return workspace.file_holder.saveFile();
    }


}
