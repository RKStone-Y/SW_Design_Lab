package Receiver;

import Receiver.ContentTree.TreeConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHolder {
    public List<String> content = new ArrayList<>();
    public CommandHistory history = new CommandHistory();
    protected String file_path = "";
    public int content_lines;

    public void clear(){
        file_path = "";
        content.clear();
        history.clear();
        content_lines = -1;

    }
    public String getFile_path(){
        return this.file_path;
    }
    public void setFilePath(String file_path){
        this.file_path = file_path;
    }

    public void setContent(List<String> new_content){
        this.content.clear();
        content.addAll(new_content);
    }

    public boolean openMarkDownFile(){
        // Called by the Command Load

//        if(!content.isEmpty()){
//            System.out.println("打开新文件将覆盖原文件，是否继续执行load：（Y/N）（请确定已保存源原文件）");
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                String  userInput = scanner.nextLine();
//                if (userInput.equals("yes")) {
//                    break;
//                } else if (userInput.equals("no")) {
//                    return;
//                } else {
//                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
//                }
//            }
//        }
        history.clear();
        if(!file_path.endsWith(".md")){
            System.err.println("请输入markdown文件：*.md");
            return false;
        }
        File file = new File(file_path);
        content = new ArrayList<>();
        if (!file.exists()) {
            try {
                boolean is_file_created = file.createNewFile();
                if(is_file_created){
                    System.out.println("成功创建新文件"+file_path);
                }
            }catch(IOException e){
                System.err.println("无法创建文件：" + file_path);
                return false;
            }
        }

        if (file.isFile()) {
            // 文件存在，尝试读取内容
            try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.add(line);
                }
                content_lines = content.size();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
                // 处理文件读取异常
            }
        } else {
            System.err.println(file_path + " 不是一个有效的文件路径。");
            return false;
        }
    }

    public boolean saveFile(){
        File file = new File(file_path);
        if(!file.exists()){
            System.err.println("文件不合法，无法保存：" + file_path);
            return false;
        }
        if (file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_path))) {
                // 在这里逐行写入Markdown内容
                for(String line: content){
                    writer.write(line);
                    writer.newLine();
                }
                // 可以根据需要编辑每行
            } catch (IOException e) {
                e.printStackTrace();
                // 处理文件写入异常
                return false;
            }
        }
        return true;
    }

    public List<String> searchTargetDirectory(String target_dir){
        List<String> result = new ArrayList<>();
        int level = 0;
        boolean isContent = false;
        boolean isMatched = false;
        for(String line: content){
            if(line.contains(target_dir) && !isMatched){
                level = TreeConstructor.countHeadingLevel(line);
                isMatched = true;
                result.add(line); // first line in Directory chose
                if(line.startsWith("*")) {
                    isContent = true;
                    break;
                }
                continue;
            }
            if(isMatched){
                if(line.startsWith("*")
                        || (line.startsWith("#")
                        && TreeConstructor.countHeadingLevel(line) > level)
                ){
                    result.add(line);
                }
                else break;
            }
        }
        if(!isMatched || isContent){
            System.out.println("\"" + target_dir + "\"" + " illegal directory");
        }
        return result;
    }

    public Memento createMemento(){
        return new Memento(this.content,this.history,this.file_path,this.content_lines);
    }
    public void restore(Memento memento){
        this.content.clear();
        this.content.addAll(memento.content());
        this.content_lines= memento.content_lines();
        this.history=memento.history();
        this.file_path= memento.file_path();
    }

}
