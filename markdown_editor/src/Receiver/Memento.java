package Receiver;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Memento {
    private final List<String> content;
    private final boolean saved;
    private final boolean active;
    private final CommandHistory history;
    private final String file_path;
    private final int content_lines;

    public int getContent_lines() {
        return content_lines;
    }

    public String getFile_path() {
        return file_path;
    }

    public CommandHistory getHistory() {
        return history;
    }

    public List<String> getContent() {
        return content;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isSaved() {
        return saved;
    }

    public Memento() {
        this(new ArrayList<>(), new CommandHistory(), "", 0,false,false);
    }
    public Memento(List<String> content, CommandHistory history, String file_path, int content_lines,boolean isSaved,boolean active) {
        this.saved = isSaved;
        this.active = active;
        this.content = new ArrayList<>(content);
        this.history = new CommandHistory(history);
        this.file_path = file_path;
        this.content_lines = content_lines;
    }
    public void writeToJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将对象转换为 JSON 字符串
            String jsonString = objectMapper.writeValueAsString(this);

            // 将 JSON 字符串写入文件
            objectMapper.writeValue(new File(filePath), jsonString);

//            System.out.println("Workspace "+ filePath.replace("workspace_memento/","") + " has been reserved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
