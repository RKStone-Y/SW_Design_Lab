package Invoker;

import ConcreteCommands.EditCommand.*;
import ConcreteCommands.FileCommand.Load;
import ConcreteCommands.FileCommand.Save;
import ConcreteCommands.LogCommand.History;
import ConcreteCommands.StatisticCommand.Stats;
import ConcreteCommands.ViewCommand.DirTree;
import ConcreteCommands.ViewCommand.List;
import ConcreteCommands.ViewCommand.ListTree;
import Interface.Command;
import Observer.CommandLog;
import Receiver.EditTools;

import java.util.Scanner;

public class Editor {
    protected Command command;
    public Editor(){}
    public CommandLog command_log;
    public Editor(Command command){
        this.command = command;
    }
    public void setCommand(Command command){
        this.command = command;
    }
    public void executeCommand(){
        command.execute();
    }

    public void parseCommand(EditTools edit_tools){
        Scanner scanner = new Scanner(System.in);
        command_log = new CommandLog();

        while (true) {
            boolean skip_undo=false;
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();
            // 分割输入字符串，假设命令和参数之间用空格分隔
            String[] parts = input.split(" ",2);
            String command_name = parts[0];
            Command command;
            // 根据不同的命令执行不同的操作
            switch (command_name) {//todo
                case "load" -> {
                    if (parts.length == 2) {
                        String param = parts[1];
                        command = new Load(edit_tools, param);
                        command_log.handleLoadCommand(param);
                    } else {
                        System.out.println("Invalid load usage should be \"load <directory>\"");
                        continue;
                    }
                }
                case "save" -> {
                    skip_undo=true;
                    command = new Save(edit_tools);
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
                        command = new Insert(edit_tools, line, new_content.toString());
                    } else {
                        new_content.append(parts[1]);
                        command = new Insert(edit_tools, new_content.toString());
                    }
                }
                case "append-head" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid append-head usage. Please provide at least one parameters.");
                        continue;
                    }
                    command = new AppendHead(edit_tools, parts[1]);
                }
                case "append-tail" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid append-tail usage. Please provide at least one parameters.");
                        continue;
                    }
                    command = new AppendTail(edit_tools, parts[1]);
                }
                case "delete" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid delete usage. Please provide at least one parameters.");
                        continue;
                    }
                    if(parts[1].matches("-?\\d+")){
                        command = new Delete(edit_tools,Integer.parseInt(parts[1]));
                    }
                    else command = new Delete(edit_tools,parts[1]);
                }
                case "undo" -> {
                    skip_undo=true;
                    command = new Undo(edit_tools);
                }
                case "redo" -> {
                    skip_undo = true;
                    command = new Redo(edit_tools);
                }
                case "list" -> {
                    skip_undo=true;
                    command = new List(edit_tools);
                }
                case "list-tree" -> {
                    skip_undo=true;
                    command = new ListTree(edit_tools);
                }
                case "dir-tree" ->{
                    if(parts.length<2) {
                        System.out.println("Invalid dir-tree usage. Please provide at least one parameters.");
                        continue;
                    }
                    skip_undo=true;
                    command = new DirTree(edit_tools,parts[1]);
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
                case "quit" ->{
                    command_log.saveToLog();
                    return;
                }
                default -> {
                    System.out.println("Unknown command: " + command_name);
                    continue;
                }
            }
            if(!skip_undo){
                edit_tools.file_holder.history.undo_list.clear();
            }
            this.setCommand(command);
            this.executeCommand();
            command_log.logCommand(input);
        }
    }
}
