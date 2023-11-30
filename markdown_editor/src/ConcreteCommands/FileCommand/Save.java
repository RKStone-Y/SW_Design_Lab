package ConcreteCommands.FileCommand;

import Interface.Command;
import Receiver.Workspace;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Save extends Command {
    // 保存Markdown内容回到文件
    public Save(Workspace workspace){
        super(workspace);
        command_id = 8;
    }
    public List<String> getOriginalFileContent(){
        List<String> tmp_content = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(workspace.file_holder.getFile_path()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tmp_content.add(line);
            }
            return tmp_content;
        } catch (IOException e) {
            e.printStackTrace();
            // 处理文件读取异常
            return null;
        }
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
