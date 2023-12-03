import ConcreteCommands.EditCommand.*;
import ConcreteCommands.FileCommand.Close;
import ConcreteCommands.FileCommand.Exit;
import ConcreteCommands.FileCommand.Load;
import ConcreteCommands.FileCommand.Open;
import ConcreteCommands.LogCommand.History;
import ConcreteCommands.StatisticCommand.Stats;
import ConcreteCommands.ViewCommand.List;
import ConcreteCommands.ViewCommand.ListTree;
import ConcreteCommands.ViewCommand.ListWorkspace;
import ConcreteCommands.ViewCommand.Ls;
import Interface.Command;
import Invoker.Editor;
import Receiver.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {
    Editor editor;
    Workspace workspace;
    Command command;
    @BeforeEach
    void initial(){
        workspace = new Workspace();
        editor = new Editor();
        File file = new File("md_file/commandTest.md");
        if(file.exists()){
            if(file.delete()){
                System.out.println("delete old commandTest.md");
            }
        }
    }
    @Test
    public void testNotifyHistoryObserver(){
        command = new Insert(workspace,"line");
        command.notifyHistoryObserver();
        assertFalse(workspace.file_holder.history.command_history.isEmpty());
    }
    @Test
    void testAppendHead() {
        workspace.file_holder.setFilePath("md_file/commandTest.md");
        workspace.file_holder.openMarkDownFile();
        command = new AppendHead(workspace,"# Command Test");
        if(command.execute()){
            assertEquals("# Command Test",workspace.file_holder.content.get(0));
        }

    }
    @Test
    void testAppendTail() {
        workspace.file_holder.setFilePath("md_file/commandTest.md");
        workspace.file_holder.openMarkDownFile();
        command = new AppendTail(workspace,"# Command Test");
        command.execute();
        command = new AppendTail(workspace,"* tail Test");
        if(command.execute()){
            assertEquals("* tail Test",workspace.file_holder.content.get(1));
        }
    }
    @Test
    void testDelete() {
        workspace.file_holder.setFilePath("md_file/commandTest.md");
        workspace.file_holder.openMarkDownFile();
        command = new AppendTail(workspace,"# Command Test");
        if(command.execute()){
            System.out.println("append \"# Command Test\"");
        }
        command = new AppendTail(workspace,"* delete Test");
        if(command.execute()){
            System.out.println("append \"* delete Test\"");
        }
        command = new Delete(workspace,"delete");
        if(command.execute()) {
            assertEquals(1, workspace.file_holder.content.size());
        }
    }
    @Test
    void testInsert() {
        workspace.file_holder.setFilePath("md_file/commandTest.md");
        workspace.file_holder.openMarkDownFile();
        command = new Insert(workspace,"## Command Test");
        if(command.execute()){
           assertEquals("## Command Test",workspace.file_holder.content.get(0));
        }
        command = new Insert(workspace,1,"# Insert Test");
        if(command.execute()){
            assertEquals("# Insert Test",workspace.file_holder.content.get(0));
        }

    }
    @Test
    void testRedo() {
        workspace.file_holder.setFilePath("md_file/1.md");
        workspace.file_holder.openMarkDownFile();
        command = new Insert(workspace,"* Undo Junit test");
        if(command.execute()){
            assertFalse(workspace.file_holder.history.command_history.isEmpty());
            if(workspace.file_holder.history.commandPop(workspace)){
                assertFalse(workspace.file_holder.history.undo_list.isEmpty());
                assertNotEquals("* Undo Junit test", workspace.file_holder.content.get(workspace.file_holder.content.size() - 1));
                command = new Redo(workspace);
                if(command.execute()) {
                    workspace.redoLastCommand();
                    assertEquals("* Undo Junit test", workspace.file_holder.content.get(workspace.file_holder.content.size() - 1));
                }
            }
        }
    }
    @Test
    void testUndo() {
        workspace.file_holder.setFilePath("md_file/1.md");
        workspace.file_holder.openMarkDownFile();
        command = new Insert(workspace,"* Undo Junit test");
        if(command.execute()){
            assertFalse(workspace.file_holder.history.command_history.isEmpty());
            command = new Undo(workspace);
            if(command.execute()){
                assertFalse(workspace.file_holder.history.undo_list.isEmpty());
                assertNotEquals("* Undo Junit test", workspace.file_holder.content.get(workspace.file_holder.content.size() - 1));
            }
        }
    }
    @Test
    void testLoad() {
        command = new Load(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
        }
    }

    @Test
    void testOpen() {
        command = new Load(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
        }
        command = new Load(editor,"md_file/2.md");
        if(command.execute()) {
            assertEquals(2, editor.workspace_list.size());
        }
        assertEquals("md_file/2.md", editor.workspace.file_holder.getFile_path());
        command = new Open(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals("md_file/1.md", editor.workspace.file_holder.getFile_path());
        }
    }

    @Test
    void testClose() {
        command = new Load(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
        }
        command = new Close(editor);
        if(command.execute()){
            assertEquals(0,editor.workspace_list.size());
        }

    }

    @Test
    void testSave() {
        command = new Load(editor,"md_file/commandTest.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
            assertTrue( editor.workspace.file_holder.content.isEmpty());
        }
        command = new Insert(editor.workspace,"* Command save test");
        if(command.execute()) {
            int size2 = editor.workspace.file_holder.content.size();
            assertEquals("* Command save test", editor.workspace.file_holder.content.get(size2-1));
        }
        editor.workspace.file_holder.saveFile();
        command = new Load(editor,"md_file/commandTest.md");
        if(command.execute()) {
            int size3 = editor.workspace.file_holder.content.size();
            assertEquals("* Command save test", editor.workspace.file_holder.content.get(size3-1));
        }
    }

    @Test
    void testExit() {
        command = new Load(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
        }

        command = new Exit(editor);
        if(command.execute()) {
            File directory = new File("workspace_memento");
            File[] files = directory.listFiles();
            assert files != null;
            assertEquals(1,files.length);
        }
    }



    @Test
    void testHistory() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        String input;
        input = "load md_file/1.md\n";
        input += "load md_file/2.md\n";
        input += "open md_file/1.md\n";
        input+= "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        editor.parseCommand();
        command = new History(editor.command_log);
        if(command.execute()){
            assertTrue(outputStreamCaptor.toString().trim().contains("load md_file/1.md"));
            assertTrue(outputStreamCaptor.toString().trim().contains("load md_file/2.md"));
            assertTrue(outputStreamCaptor.toString().trim().contains("open md_file/1.md"));
            assertTrue(outputStreamCaptor.toString().trim().contains("exit"));
        }
    }
    @Test
    void testStats() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        String input;
        input = "load md_file/1.md\n";
        input += "load md_file/2.md\n";
        input += "open md_file/1.md\n";
        input+= "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        editor.parseCommand();
        command = new Stats(editor,"all");
        if(command.execute()){
            assertTrue(outputStreamCaptor.toString().contains("md_file/"));
            String[] outputLines = outputStreamCaptor.toString().split("\\n");
            Pattern pattern = Pattern.compile("\\s(\\d+)");
            for (String line : outputLines) {
                if(line.startsWith("md_file/")) {
                    Matcher matcher = pattern.matcher(line);
                    assertTrue(matcher.find(), "No number found after space in: " + line);
                }
            }
        }
    }
    @Test
    void testList() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        command = new Load(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
        }
        command = new List(editor.workspace);
        if(command.execute()){
            for(String line: editor.workspace.file_holder.content){
                assertTrue(outputStreamCaptor.toString().contains(line));
            }
        }

    }
    @Test
    void testListTree() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        java.util.List<String> result = new ArrayList<>();
        result.add("├── new title1");
        result.add("├── Enter test");
        result.add("├── Enter test");
        result.add("    └── ·123");
        result.add("├── This is the Append_head Test");
        result.add("├── This is Test");
        result.add("    ├── ·remark: undo is successful");
        result.add("    ├── Test1");
        result.add("        ├── ·hello world !");
        result.add("        └── test2");
        result.add("    └── 工具箱");
        result.add("        └── Adobe");
        result.add("            └── ·photoshop");
        result.add("└── insert_with_no_line test");
        command = new Load(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
        }
        command = new ListTree(editor.workspace);
        if(command.execute()){
            for(String line : result){
                assertTrue(outputStreamCaptor.toString().trim().contains(line));
            }
        }

    }
    @Test
    void testListWorkspace() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        command = new Load(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
        }
        command = new Load(editor,"md_file/2.md");
        if(command.execute()) {
            assertEquals(2, editor.workspace_list.size());
        }
        command = new ListWorkspace(editor.workspace,editor.workspace_list);
        if(command.execute()) {

            assertTrue(outputStreamCaptor.toString().contains("md_file/1.md"));
            assertTrue(outputStreamCaptor.toString().contains("->md_file/2.md"));
        }
    }
    @Test
    void testLs() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        command = new Load(editor,"md_file/1.md");
        if(command.execute()) {
            assertEquals(1, editor.workspace_list.size());
        }
        command = new Ls(editor.workspace);
        if(command.execute()) {
            assertTrue(outputStreamCaptor.toString().contains("├──1.md  *"));
        }

    }
}
