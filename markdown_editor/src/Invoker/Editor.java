package Invoker;

import ConcreteCommands.EditCommand.*;
import ConcreteCommands.FileCommand.Close;
import ConcreteCommands.FileCommand.Load;
import ConcreteCommands.FileCommand.Open;
import ConcreteCommands.FileCommand.Save;
import ConcreteCommands.LogCommand.History;
import ConcreteCommands.StatisticCommand.Stats;
import ConcreteCommands.ViewCommand.*;
import Interface.Command;
import Observer.CommandLog;
import Receiver.Workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Editor {
    protected Command command;
    public Editor(){}
    public CommandLog command_log;
    public Workspace current_workspace = new Workspace();
    java.util.List<Workspace> workspace_list  = new ArrayList<>();
    public Editor(Command command){
        this.command = command;
    }
    public void setCommand(Command command){
        this.command = command;
    }
    public boolean executeCommand(){
        return command.execute();
    }
    private Workspace searchWorkspace(String filePath){
        for(Workspace tmp: workspace_list){
            if(filePath.equals(tmp.file_holder.getFile_path())){
                return tmp;
            }
        }
        return null;
    }

    public static boolean showDirectory(String path,int level){
        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files != null) {
            for (int i=0;i<files.length ;i++) {
                File file = files[i];
                if(i==files.length-1) {
                    System.out.println("    ".repeat(level)+"└──" + file.getName());
                }
                else{
                    System.out.println("    ".repeat(level)+"├──" + file.getName());
                }
                if (file.isDirectory()) {
                    showDirectory(file.getPath(),level+1);
                }
            }
            return true;
        }
        else{
            System.out.println("Empty Workspace");
            return false;
        }

    }

    public void parseCommand(Workspace workspace){
        Scanner scanner = new Scanner(System.in);
        command_log = new CommandLog();
        boolean ExeSuccess;
        while (true) {
            boolean skip_undo = false;
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
                        if(param.equals(workspace.file_holder.getFile_path())){
                            System.out.println("Already been loaded");
                            continue;
                        }
                        else {
                            //save the current command, in case to wrong command
                            current_workspace = workspace;

                            //create new workspace
                            Workspace new_workspace = new Workspace();
                            workspace = new_workspace;
                            workspace_list.add(new_workspace);

                            //generate command
                            command = new Load(workspace, param);
                        }
                    } else {
                        System.out.println("Invalid load usage should be \"load <directory>\"");
                        continue;
                    }
                }
                case "open" ->{
                    skip_undo=true;
                    current_workspace = workspace;
                    if (parts.length == 2) {
                        String fileName = parts[1];
                        if(fileName.equals(workspace.file_holder.getFile_path())){
                            System.out.println("Already been open");
                            continue;
                        }
                        else {
                            workspace = searchWorkspace(fileName);
                            if(workspace == null){
                                workspace = current_workspace;
                                System.out.println("No such workspace");
                                continue;
                            }
                            command = new Open(workspace,workspace_list);
                        }
                    } else {
                        System.out.println("Invalid load usage should be \"load <directory>\"");
                        continue;
                    }
                }
                case "close" -> {
                    skip_undo=true;
                    command = new Close(workspace,workspace_list);
                }
                case "save" -> {
                    skip_undo=true;
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
                    command = new Stats(command_log,args);
                }
                case "exit" ->{//todo: save the editing workspace
                    command_log.saveToLog();
                    return;
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
                    case "load" -> {
                        command_log.handleLoadCommand(workspace.file_holder.getFile_path());//stat module
                    }
                    case "insert","append-head","append-tail","delete","undo","redo" -> {
                        workspace.isSaved = false;
                    }
                    case "close" -> {
                        if(!workspace_list.isEmpty()) {
                            workspace = workspace_list.get(workspace_list.size() - 1);
                        }
                        else{
                            workspace = new Workspace();
                        }
                    }
                    case "save" -> {
                        workspace.isSaved = true;
                    }
                }
            }
            else{
                if(command_name.equals("load")) {
                    workspace_list.remove(workspace_list.size() - 1);
                    workspace = current_workspace;
                }
            }

        }
    }
}
