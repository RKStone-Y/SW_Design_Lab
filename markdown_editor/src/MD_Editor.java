import Invoker.Editor;

public class MD_Editor {
    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.restoreWorkspace();
        editor.parseCommand();//main part of MD_Editor
    }
}
