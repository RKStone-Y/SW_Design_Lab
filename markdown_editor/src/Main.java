import Invoker.Editor;
import Receiver.EditTools;

public class Main {
    public static void main(String[] args) {
        EditTools edit_tools = new EditTools();
        Editor editor = new Editor();
        editor.parseCommand(edit_tools);
    }
}
