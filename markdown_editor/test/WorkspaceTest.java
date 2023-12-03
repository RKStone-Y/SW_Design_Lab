import ConcreteCommands.EditCommand.Insert;
import Interface.Command;
import Receiver.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceTest {
    Workspace workspace;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @BeforeEach
    void initial(){
        workspace = new Workspace();
    }
    @Test
    void testAddNewContentToFile(){
        workspace.file_holder.setFilePath("md_file/workspaceTest.md");
        workspace.file_holder.openMarkDownFile();
        workspace.addNewContentToFile(1,"# add test");
        workspace.addNewContentToFile("* add without line num");
        assertEquals("# add test",workspace.file_holder.content.get(0));
        assertEquals("* add without line num",workspace.file_holder.content.get(1));
    }

    @Test
    void testDeleteContentFromFile(){
        workspace.file_holder.setFilePath("md_file/deleteTest.md");
        workspace.file_holder.openMarkDownFile();
        workspace.addNewContentToFile(1,"# delete test");
        workspace.addNewContentToFile("## second title");
        workspace.addNewContentToFile("* delete with content");

        workspace.deleteContentFromFile("content");
        assertEquals(2,workspace.file_holder.content.size());
        assertEquals("# delete test",workspace.file_holder.content.get(0));

        workspace.deleteContentFromFile(0);
        assertEquals(1,workspace.file_holder.content.size());
        assertEquals("## second title",workspace.file_holder.content.get(0));
    }

    @Test
    void testShowContent(){
        System.setOut(new PrintStream(outputStreamCaptor));
        workspace.file_holder.setFilePath("md_file/1.md");
        workspace.file_holder.openMarkDownFile();
        if(workspace.showContent()){
            for(String line: workspace.file_holder.content){
                assertTrue(outputStreamCaptor.toString().trim().contains(line));
            }
        }
        System.setOut(System.out);
    }
    @Test
    void testShowWholeTree(){
        System.setOut(new PrintStream(outputStreamCaptor));
        workspace.file_holder.setFilePath("md_file/testTree.md");
        workspace.file_holder.openMarkDownFile();

        List<String> result = new ArrayList<>();
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

        if(workspace.showWholeTree(workspace.file_holder.content)){
            for(String line: result){
                assertTrue(outputStreamCaptor.toString().trim().contains(line));
            }
        }
        System.setOut(System.out);
    }


    @Test
    void testCommandPushAndPop(){
        workspace.file_holder.setFilePath("md_file/1.md");
        workspace.file_holder.openMarkDownFile();
        assertFalse(workspace.file_holder.history.commandPop(workspace));
        Command command = new Insert(workspace,"* Undo Junit test");
        if(command.execute()){
            assertFalse(workspace.file_holder.history.command_history.isEmpty());
            if(workspace.file_holder.history.commandPop(workspace)){
                assertFalse(workspace.file_holder.history.undo_list.isEmpty());
                assertNotEquals("* Undo Junit test",workspace.file_holder.content.get(workspace.file_holder.content.size()-1));
            }
        }
    }

    @Test
    void testRedoLastCommand(){
        workspace.file_holder.setFilePath("md_file/1.md");
        workspace.file_holder.openMarkDownFile();
        assertFalse( workspace.redoLastCommand());
        Command command = new Insert(workspace,"* Undo Junit test");
        if(command.execute()){
            assertFalse(workspace.file_holder.history.command_history.isEmpty());
            if(workspace.file_holder.history.commandPop(workspace)){
                assertFalse(workspace.file_holder.history.undo_list.isEmpty());
                assertNotEquals("* Undo Junit test",workspace.file_holder.content.get(workspace.file_holder.content.size()-1));
                workspace.redoLastCommand();
                assertEquals("* Undo Junit test",workspace.file_holder.content.get(workspace.file_holder.content.size()-1));
            }
        }
    }
}
