package Receiver;

import java.util.ArrayList;
import java.util.List;

public record Memento(List<String> content, CommandHistory history, String file_path, int content_lines) {
    public Memento(List<String> content, CommandHistory history, String file_path, int content_lines) {
        this.content = new ArrayList<>(content);
        this.history = new CommandHistory(history);
        this.file_path = file_path;
        this.content_lines = content_lines;
    }
}
