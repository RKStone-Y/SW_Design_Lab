import Invoker.Editor;

public class Main {
    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.restore_workspace();
        editor.parseCommand();//程序入口
    }
}
