import Invoker.Editor;
import Receiver.Workspace;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class EditorTest {
    Editor editor;

    public String input = "";

    private final PrintStream originalSystemOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @BeforeEach
    public void setUp() {//在测试开始之前替换了标准输出
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {//在测试结束后恢复了标准输出
        System.setIn(System.in);
        System.setOut(originalSystemOut);
    }

    @BeforeEach
    public void initial(){
        editor = new Editor();
    }

    @Test
    void testDeleteMemento(){
        input = "load md_file/1.md\n";
        input+= "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        editor.parseCommand();
        String directoryPath = "workspace_memento";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                assertEquals(file.getName(),"1.md.json");
            }
        }
        editor.deleteMemento();
        File directoryAfterDelete = new File(directoryPath);
        File[] deletedFiles = directoryAfterDelete.listFiles();
        assertArrayEquals(new File[0], deletedFiles);
    }
    @Test
    void testExitEditor() {
        Workspace workspace1 = new Workspace();
        workspace1.isSaved=false;

        Workspace workspace2 = new Workspace();
        workspace2.isSaved=false ;

        editor.workspace_list.add(workspace1);
        editor.workspace_list.add(workspace2);

        input = "n\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        editor.exitEditor();
        assertTrue(outputStreamCaptor.toString().contains("Do you want to save the unsaved workspace [Y\\N]"));


    }
    @Test
    void testRestoreWorkspace() {
        Editor editor1 = new Editor();
        input = "load md_file/1.md\n";
        input+= "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        editor.parseCommand();
        editor1.restoreWorkspace();
        assertEquals(editor1.workspace.file_holder.getFile_path(), editor.workspace.file_holder.getFile_path());
        assertEquals(editor1.workspace.file_holder.content, editor.workspace.file_holder.content);
    }
    @Test
    void testCloseWorkspace(){
        input = "load md_file/1.md\n";
        input+= "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        editor.parseCommand();
        editor.closeWorkspace();
        assertTrue(editor.workspace_list.isEmpty());
    }

    @Test
    void testParseCommand() {
        input = "load testDirectory.md\n";
        input+= "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        editor.parseCommand();
        assertEquals("testDirectory.md", editor.workspace.file_holder.getFile_path());
    }

    @Test
    void testParseCommandUnknownCommand() {
        // Arrange
        input = "unknownCommand \n";
        input+= "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try {
            editor.parseCommand();
        } finally {
            System.setIn(System.in);
        }
        assertTrue(outputStreamCaptor.toString().contains("Unknown command"));
    }
}
