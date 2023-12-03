package Invoker;

import ConcreteCommands.EditCommand.*;
import ConcreteCommands.FileCommand.*;
import ConcreteCommands.LogCommand.History;
import ConcreteCommands.StatisticCommand.Stats;
import ConcreteCommands.ViewCommand.*;
import Interface.Command;
import Observer.CommandLog;
import Receiver.Memento;
import Receiver.Workspace;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class Editor {
    protected Command command;
    public Editor(){
        workspace = new Workspace();
        command_log = new CommandLog();
    }
    public CommandLog command_log;
    public Workspace workspace = new Workspace();
    public java.util.List<Workspace> workspace_list  = new ArrayList<>();
    public Editor(Command command){
        this.command = command;
    }
    public void setCommand(Command command){
        this.command = command;
    }
    public boolean executeCommand(){
        return command.execute();
    }
    public Workspace searchWorkspace(String filePath){
        for(Workspace tmp: workspace_list){
            if(filePath.equals(tmp.file_holder.getFile_path())){
                return tmp;
            }
        }
        return null;
    }

    public static boolean showDirectory(String path,String current_file,int level){
        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files != null) {
            for (int i=0;i<files.length ;i++) {
                File file = files[i];
                String output;
                if(i==files.length-1) {
                    output = "    ".repeat(level)+"└──" + file.getName();
                }
                else{
                    output = "    ".repeat(level)+"├──" + file.getName();
                }
                if(file.getName().equals(current_file)){
                    output += "  *";
                }
                System.out.println(output);
                if (file.isDirectory()) {
                    showDirectory(file.getPath(),current_file,level+1);
                }
            }
            return true;
        }
        else{
            System.out.println("Empty Workspace");
            return false;
        }

    }
    public void deleteMemento(){
        try {
            String directoryPath = "workspace_memento";
            Stream<Path> files = Files.list(Paths.get(directoryPath));

            files.forEach(file -> {
                try {
                    Files.delete(file);
//                    System.out.println("Deleted file: " + file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            files.close();
//            System.out.println("All files in the directory have been deleted.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean exitEditor(){
        boolean existUnsavedFile = false;
        for(Workspace tmp: workspace_list){
            if(!tmp.isSaved){
                existUnsavedFile = true;
                break;
            }
        }
        if(existUnsavedFile){
            System.out.println("Do you want to save the unsaved workspace [Y\\N] ?");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String userInput = scanner.nextLine();
                if (userInput.equals("y")||userInput.equals("Y")) {
                    for(Workspace tmp_workspace: workspace_list){
                        tmp_workspace.file_holder.saveFile();
                        tmp_workspace.isSaved = true;
                    }
                    break;
                } else if (userInput.equals("n")||userInput.equals("N")) {
                    for(Workspace tmp: workspace_list){
                        if(!tmp.isSaved){
                            tmp.file_holder.setSavedContent();
                            tmp.isSaved = true;
                        }
                    }
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        }
        deleteMemento();
        if(!workspace_list.isEmpty() ){
            for(Workspace tmp_workspace: workspace_list){
                tmp_workspace.file_holder.createMemento(tmp_workspace.isSaved,tmp_workspace.active);
            }
        }
        return true;
    }
    public void restoreWorkspace(){
        ObjectMapper objectMapper = new ObjectMapper();

        String directoryPath ="workspace_memento";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    try {
                        // read json
                        String jsonString = objectMapper.readValue(file, String.class);

                        // turn json to file content
                        Memento memento = objectMapper.readValue(jsonString, Memento.class);
                        Workspace origin_workspace = new Workspace();
                        origin_workspace.file_holder.restore(memento);
                        origin_workspace.isSaved = memento.isSaved();
                        origin_workspace.active = memento.isActive();
                        if(origin_workspace.active){
                            this.workspace = origin_workspace;
                        }
                        command_log.handleLoadCommand(origin_workspace.file_holder.getFile_path());
                        workspace_list.add(origin_workspace);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    jsonFiles.add(file);
                }
            }
        }

    }
    public boolean closeWorkspace(){
        if(workspace_list.isEmpty()){
            System.err.println("未开启任何文件");
            return false;
        }
        if(!workspace.isSaved){
            System.out.println("Do you want to save the current workspace [Y\\N] ?");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String  userInput = scanner.nextLine();
                if (userInput.equals("y")||userInput.equals("Y")) {
                    workspace.file_holder.saveFile();
                    break;
                } else if (userInput.equals("n")||userInput.equals("N")) {
                    workspace.file_holder.clear();
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        }
        workspace_list.remove(workspace);
        return true;
    }
    public void parseCommand(){
        Scanner scanner = new Scanner(System.in);
        boolean ExeSuccess;
        while (true) {
            boolean skip_undo = false;
            String closeFilePath = "";
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();
            // 分割输入字符串，假设命令和参数之间用空格分隔
            String[] parts = input.split(" ",2);
            String command_name = parts[0];
            Command command;
            // 根据不同的命令执行不同的操作
            switch (command_name) {//todo
                case "load" -> {
                    skip_undo=true;
                    if (parts.length == 2) {
                        String param = parts[1];
                        command = new Load(this, param);
                    } else {
                        System.out.println("Invalid load usage should be \"load <directory>\"");
                        continue;
                    }
                }
                case "open" ->{
                    skip_undo=true;
                    if (parts.length == 2) {
                        String fileName = parts[1];
                        command = new Open(this,fileName);
                    } else {
                        System.out.println("Invalid load usage should be \"load <directory>\"");
                        continue;
                    }
                }
                case "close" -> {
                    skip_undo=true;
                    command = new Close(this);
                    closeFilePath=workspace.file_holder.getFile_path();
                }
                case "save" -> {
                    skip_undo=true;
//                    workspace.isSaved = true;
                    command = new Save(workspace);
                }
                case "insert" -> {
                    if(parts.length<2) {
                        System.out.println("Invalid insert usage. Please provide at least one parameters.");
                        continue;
                    }
                    int line;
                    StringBuilder new_content = new StringBuilder();

                    String[] arguments = parts[1].split(" ",2);
                    if (arguments[0].matches("-?\\d+")) {
                        line = Integer.parseInt(arguments[0]);
                        new_content.append(arguments[1]);
                        command = new Insert(workspace, line, new_content.toString());
                    } else {
                        new_content.append(parts[1]);
                        command = new Insert(workspace, new_content.toString());
                    }
                }
                case "append-head" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid append-head usage. Please provide at least one parameters.");
                        continue;
                    }
                    command = new AppendHead(workspace, parts[1]);
                }
                case "append-tail" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid append-tail usage. Please provide at least one parameters.");
                        continue;
                    }
                    command = new AppendTail(workspace, parts[1]);
                }
                case "delete" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid delete usage. Please provide at least one parameters.");
                        continue;
                    }
                    if(parts[1].matches("-?\\d+")){
                        command = new Delete(workspace,Integer.parseInt(parts[1]));
                    }
                    else command = new Delete(workspace,parts[1]);
                }
                case "undo" -> {
                    skip_undo=true;
                    command = new Undo(workspace);
                }
                case "redo" -> {
                    skip_undo = true;
                    command = new Redo(workspace);
                }
                case "list" -> {
                    skip_undo=true;
                    command = new List(workspace);
                }
                case "list-tree" -> {
                    skip_undo=true;
                    command = new ListTree(workspace);
                }
                case "dir-tree" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid dir-tree usage. Please provide at least one parameters.");
                        continue;
                    }
                    skip_undo=true;
                    command = new DirTree(workspace,parts[1]);
                }
                case "list-workspace" -> {
                    skip_undo=true;
                    command = new ListWorkspace(workspace,workspace_list);
                }
                case "ls" -> {
                    skip_undo=true;
                    command = new Ls(workspace);
                }
                case "history" ->{
                    skip_undo=true;
                    if(parts.length<2) {
                        command = new History(command_log);
                    }
                    else if(parts.length == 2) {
                        String[] args = parts[1].split(" ", 2);
                        if (args.length == 1 && args[0].matches("-?\\d+")) {
                            command = new History(command_log, Integer.parseInt(args[0]));
                        }
                        else{
                            System.out.println("Invalid history usage: history [number]");
                            continue;
                        }
                    }
                    else{
                        System.out.println("Invalid history usage: history [number]");
                        continue;
                    }
                }
                case "stats" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid Stats usage. Please provide at least one parameters.");
                        continue;
                    }
                    skip_undo=true;
                    String args;
                    if(parts[1].equals("all")){
                        args ="all";
                    }
                    else if(parts[1].equals("current")){
                        args = "current";
                    }
                    else {
                        System.out.println("Invalid Stats usage: stats all/current");
                        continue;
                    }
                    command = new Stats(this,args);
                }
                case "exit" ->{

                    for(Workspace tmp:workspace_list){//不然可能出现多个active
                        tmp.active = false;
                    }
                    workspace.active = true;
                    command = new Exit(this);
                }
                default -> {
                    System.out.println("Unknown command: " + command_name);
                    continue;
                }
            }
            if(!skip_undo){
                workspace.file_holder.history.undo_list.clear();
            }
            this.setCommand(command);
            ExeSuccess = this.executeCommand();
            if(ExeSuccess){
                command_log.logCommand(input);
                switch (command_name) {//todo
                    case "load","open" -> command_log.handleLoadCommand(workspace.file_holder.getFile_path());//stat module
                    case "insert","append-head","append-tail","delete","undo","redo" -> workspace.isSaved = false;
                    case "close" -> {
                        command_log.endWorkRecord(closeFilePath);
                        if(!workspace_list.isEmpty()) {
                            workspace = workspace_list.get(workspace_list.size() - 1);
                        }
                        else{
                            workspace = new Workspace();
                        }
                    }
                    case "save" -> workspace.isSaved = true;
                    case "exit" -> {
                        command_log.saveToLog();
                        return;
                    }
                }
            }
        }
    }
}
