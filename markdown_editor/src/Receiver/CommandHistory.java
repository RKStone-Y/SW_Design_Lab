package Receiver;

import Interface.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CommandHistory {
    public List<List<String>> content_history;
    public Stack<Command>  command_history;
    public Stack<Command>  undo_list;



    public CommandHistory(){
        content_history = new ArrayList<>();
        command_history = new Stack<>();
        undo_list = new Stack<>();
    }
    public boolean canUndo() {
        return !command_history.isEmpty();
    }



    // push means a command has been executed
    public void commandPush(Command command,List<String> content) {
        List<String> tmp_content = new ArrayList<>(content);
        command_history.push(command);
        content_history.add(tmp_content);
    }

    //pop means undo the command
    public boolean commandPop(List<String> content) {

        if(canUndo()){
            undo_list.push(command_history.pop());
            content.clear();
            content.addAll(content_history.get(content_history.size()-1));
            content_history.remove(content_history.size()-1);
            return true;
        }
        else {
            System.err.println("没有已执行指令，无法undo");
            return false;
        }
    }
    public void clearTheStack(){
        content_history.clear();
        command_history.clear();
        undo_list.clear();
    }


}
