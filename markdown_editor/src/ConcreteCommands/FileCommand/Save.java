package ConcreteCommands.FileCommand;

import Interface.Command;
import Receiver.EditTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Save extends Command {
    // 保存Markdown内容回到文件
    public Save(EditTools edit_tools){
        super(edit_tools);
    }
    public List<String> getOriginalFileContent(){
        List<String> tmp_content = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(edit_tools.file_holder.getFile_path()))) {
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

    // Save has a unique undo and redo,
    // which depends on the origin_content in file_holder,
    // so it should be dealt alone;
    @Override
    public void execute() {
//        notifyHistoryObserver(getOriginalFileContent());
        notifyHistoryObserver();
        edit_tools.file_holder.saveFile();

    }


}
