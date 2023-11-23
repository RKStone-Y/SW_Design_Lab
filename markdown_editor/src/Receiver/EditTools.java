package Receiver;

import Interface.Command;
import Receiver.ContentTree.MarkdownFileTree;
import Receiver.ContentTree.TreeConstructor;
import Receiver.ContentTree.Visitor.TreeVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditTools {
    public FileHolder file_holder;
    public EditTools(){
        file_holder = new FileHolder();
    }

    public void addNewContentToFile(int line , String new_content){ // line is index from 1 to n
        File file = new File(file_holder.file_path);
        if(!file.exists()){
            System.err.println("文件不存在，请先打开文件");
            return ;
        }
        if(file.exists()){
            file_holder.content_lines = file_holder.content.size();
            if(line <= file_holder.content_lines-1) {
                file_holder.content.add(line, new_content);
            }
            else {
                file_holder.content.add(new_content);
            }
            file_holder.content_lines++;
        }
    }
    public void addNewContentToFile(String new_content){ // line is index from 1 to n
        File file = new File(file_holder.file_path);
        if(!file.exists()){
            System.err.println("文件不存在，请先打开文件");
        }
        else{
            file_holder.content.add(new_content);
            file_holder.content_lines++;
        }
    }

    public void deleteContentFromFile(int line){
        File file = new File(file_holder.file_path);
        if(!file.exists()){
            System.err.println("文件不存在，请先打开文件");
            return;
        }
        if(line <= file_holder.content_lines){
            file_holder.content.remove(line);
        }
        else {
           System.out.println("超过文件行数，当前文件行数为：" + file_holder.content_lines);
        }

    }
    public void deleteContentFromFile(String target_content){
        File file = new File(file_holder.file_path);
        if(!file.exists()){
            System.err.println("文件不存在，请先打开文件");
            return;
        }
        file_holder.content_lines = file_holder.content.size();
        file_holder.content.removeIf(line -> line.contains(target_content));

        if(file_holder.content.size() == file_holder.content_lines){
            System.err.println("无包含当前字符串的行：" + target_content);
        }
        else {
            file_holder.content_lines = file_holder.content.size();
        }
    }

    public void withdrawCommand(){
        List<String> tmp_content = new ArrayList<>();
        boolean can_command_pop = file_holder.history.commandPop(tmp_content);
//        System.out.println("undo list size is : "+file_holder.history.undo_list.size());
        if(can_command_pop){
            file_holder.setContent(tmp_content);
        }
        else{
            System.out.println("it can not pop\n");
        }
    }

    public void redoLastCommand(){
        if(!file_holder.history.undo_list.isEmpty()) {
            Command command = file_holder.history.undo_list.pop();
            command.execute();
        }
        else System.err.println("Please Undo First");
    }
    public void showContent(){
        for(String line: file_holder.content){
            System.out.println(line);
        }
    }
    public void showWholeTree(List<String> target_content){
        TreeVisitor visitor = new TreeVisitor();
        TreeConstructor treeConstructor = new TreeConstructor();
        MarkdownFileTree markdownFileTree = treeConstructor.parseMarkdownFile(target_content);
        markdownFileTree.accept(visitor);
    }

}
