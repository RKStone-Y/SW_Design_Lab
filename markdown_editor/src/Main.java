import Invoker.Editor;
import Receiver.Workspace;

public class Main {
    public static void main(String[] args) {
        Workspace workspace = new Workspace();
        Editor editor = new Editor();
        editor.parseCommand(workspace);
    }
}
