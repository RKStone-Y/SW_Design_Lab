import Invoker.Editor;
import Receiver.FileHolder;
import Receiver.Memento;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileHolderTest {
    FileHolder file_holder ;
    Editor editor;

    public String input = "";

    @BeforeEach
    public void initial(){
        file_holder = new FileHolder();
    }


    @Test
    void testClear() {
        editor = new Editor();
        input = "load md_file/1.md\n";
        input+= "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        editor.parseCommand();
        editor.workspace.file_holder.clear();
        assertEquals("",editor.workspace.file_holder.getFile_path());
        assertTrue(editor.workspace.file_holder.content.isEmpty());
        assertTrue(editor.workspace.file_holder.history.undo_list.isEmpty());
        assertTrue(editor.workspace.file_holder.history.command_history.isEmpty());
        assertEquals(-1,editor.workspace.file_holder.content_lines);
    }
    @Test
    void testOpenMarkDownFile() {
        file_holder.setFilePath("md_file/1.md");
        assertTrue(file_holder.openMarkDownFile());
        assertFalse(file_holder.content.isEmpty());

        FileHolder fileHolder1 = new FileHolder();
        fileHolder1.setFilePath("md/1.md");
        assertFalse(fileHolder1.openMarkDownFile());
    }

    @Test
    void testSaveFile(){
        file_holder.setFilePath("md_file/testSave.md");
        File file = new File("md_file/testSave.md");
        if(!file.exists() || file.delete()) {
            file_holder.openMarkDownFile();
            assertTrue(file_holder.content.isEmpty());
            for (int i = 0; i < 5; i++) {
                file_holder.content.add("this is " + i + "test");
            }
            assertTrue(file_holder.saveFile());

            FileHolder fileHolder = new FileHolder();
            fileHolder.setFilePath("md_file/testSave.md");
            fileHolder.openMarkDownFile();
            assertFalse(fileHolder.content.isEmpty());
        }
    }

    @Test
    void testSearchTargetDirectory(){
        List<String> part_content = new ArrayList<>();
        part_content.add("## 工具箱");
        part_content.add("### Adobe");
        part_content.add("* photoshop");
        file_holder.setFilePath("md_file/1.md");
        file_holder.openMarkDownFile();
        List<String> target_content = file_holder.searchTargetDirectory("## 工具箱");
        assertEquals(3,target_content.size());
        for(int i = 0; i < 3; i++){
            assertEquals(part_content.get(i), target_content.get(i));
        }
    }

    @Test
    void testCreateMemento(){
        //First delete workspace_memento/1.md.json in case that 1.md.json created by last use
        file_holder.setFilePath("md_file/4Test.md");
        file_holder.openMarkDownFile();
        File file = new File("workspace_memento/4Test.md.json");
        file_holder.createMemento(false,false);
        assertTrue(file.exists());
    }

    @Test
    void testRestore()  {
        file_holder.setFilePath("md_file/4Test.md");
        file_holder.openMarkDownFile();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("workspace_memento/4Test.md.json");
            if(file.exists()) {
                String jsonString = objectMapper.readValue(file, String.class);

                // turn json to file content
                Memento memento = objectMapper.readValue(jsonString, Memento.class);
                assertEquals(file_holder.getFile_path(), memento.getFile_path());
                assertEquals(file_holder.content, memento.getContent());
                assertEquals(file_holder.saved_content, memento.getSaved_content());
                assertEquals(file_holder.history.undo_list, memento.getHistory().undo_list);
                assertEquals(file_holder.history.command_history, memento.getHistory().command_history);
                assertEquals(file_holder.content_lines, memento.getContent_lines());
            }
            else {
                System.out.println("请先创建4test.md.json memento");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
