package Receiver;

import Interface.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CommandHistory {
//    public List<List<String>> content_history;
    public Stack<Command>  command_history;
    public Stack<Command>  undo_list;

    public void clear(){
//        content_history.clear();
        command_history.clear();
        undo_list.clear();
    }

    public CommandHistory(){
//        content_history = new ArrayList<>();
        command_history = new Stack<>();
        undo_list = new Stack<>();
    }
    public CommandHistory(CommandHistory history){
//        this.content_history = new ArrayList<>();
//        for (List<String> content : history.content_history) {
//            this.content_history.add(new ArrayList<>(content));
//        }

        this.command_history = new Stack<>();
        this.command_history.addAll(history.command_history);

        this.undo_list = new Stack<>();
        this.undo_list.addAll(history.undo_list);
    }
    public boolean canUndo() {
        return !command_history.isEmpty();
    }



    // push means a command has been executed
    public void commandPush(Command command,List<String> content) {
        command_history.push(command);
    }

    //pop means undo the command
    public boolean commandPop() {
        if(canUndo()){
            Command command = command_history.pop();
            undo_list.push(command);
            return command.undo();
        }
        else {
            System.err.println("没有已执行指令，无法undo");
            return false;
        }
    }



}
