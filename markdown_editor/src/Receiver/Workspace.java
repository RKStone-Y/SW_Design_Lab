package Receiver;

import Interface.Command;
import Receiver.ContentTree.MarkdownFileTree;
import Receiver.ContentTree.TreeConstructor;
import Receiver.ContentTree.Visitor.TreeVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Workspace {
    public FileHolder file_holder;
    public boolean isSaved = false;
    public Workspace(){
        file_holder = new FileHolder();
    }
    public boolean closeWorkspace(List<Workspace> workspaceList){
        if(workspaceList.isEmpty()){
            System.err.println("未开启任何文件");
            return false;
        }
        if(!isSaved){
            System.out.println("该文件未保存，是否需要保存？（Y/N）");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String  userInput = scanner.nextLine();
                if (userInput.equals("y")||userInput.equals("Y")) {
                    file_holder.saveFile();
                    break;
                } else if (userInput.equals("n")||userInput.equals("N")) {
                    file_holder.clear();
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        }
        workspaceList.remove(this);
        return true;
    }
    // insert
    public boolean addNewContentToFile(int line , String new_content){ // line is index from 1 to n
        File file = new File(file_holder.file_path);
        if(!file.exists()){
            System.err.println("文件不存在，请先打开文件");
            return false;
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
        return true;
    }
    public boolean addNewContentToFile(String new_content){ // line is index from 1 to n
        File file = new File(file_holder.file_path);
        if(!file.exists()){
            System.err.println("文件不存在，请先打开文件");
            return false;
        }
        else{
            file_holder.content.add(new_content);
            file_holder.content_lines++;
            return true;
        }
    }

    // delete
    public boolean deleteContentFromFile(int line){
        File file = new File(file_holder.file_path);
        if(!file.exists()){
            System.err.println("文件不存在，请先打开文件");
            return false;
        }
        if(line <= file_holder.content_lines){
            file_holder.content.remove(line);
            return true;
        }
        else {
           System.out.println("超过文件行数，当前文件行数为：" + file_holder.content_lines);
           return false;
        }

    }
    public boolean deleteContentFromFile(String target_content){
        File file = new File(file_holder.file_path);
        if(!file.exists()){
            System.err.println("文件不存在，请先打开文件");
            return false;
        }
        file_holder.content_lines = file_holder.content.size();
        file_holder.content.removeIf(line -> line.contains(target_content));

        if(file_holder.content.size() == file_holder.content_lines){
            System.err.println("无包含当前字符串的行：" + target_content);
            return false;
        }
        else {
            file_holder.content_lines = file_holder.content.size();
            return true;
        }
    }

    // undo
//    public boolean withdrawCommand(){
//        List<String> tmp_content = new ArrayList<>();
//        boolean can_command_pop = file_holder.history.commandPop(tmp_content);
//        if(can_command_pop){
//            file_holder.setContent(tmp_content);
//            return true;
//        }
//        else{
//            System.out.println("withdrawCommand: Can not Pop\n");
//            return false;
//        }
//    }

    //redo
    public boolean redoLastCommand(){
        if(!file_holder.history.undo_list.isEmpty()) {
            Command command = file_holder.history.undo_list.pop();
            return command.execute();
        }
        else {
            System.err.println("Please Undo First");
            return false;
        }
    }

    // list
    public boolean showContent(){
        if(file_holder.content.isEmpty()){
            System.err.println("showContent: empty content");
            return false;
        }
        for(String line: file_holder.content){
            System.out.println(line);
        }
        return true;
    }
    public boolean showWholeTree(List<String> target_content){
        if(target_content.isEmpty()){
            System.err.println("showWholeTree: empty content");
            return false;
        }
        TreeVisitor visitor = new TreeVisitor();
        TreeConstructor treeConstructor = new TreeConstructor();
        MarkdownFileTree markdownFileTree = treeConstructor.parseMarkdownFile(target_content);
        markdownFileTree.accept(visitor);
        return true;
    }

}
