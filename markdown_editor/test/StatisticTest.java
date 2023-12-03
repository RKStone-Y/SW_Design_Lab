import ConcreteCommands.StatisticCommand.Stats;
import Interface.Command;
import Invoker.Editor;
import Observer.CommandLog;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StatisticTest {
    public Editor editor = new Editor();
    public Command command;
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
        command = new Stats(editor,"current");
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
}
