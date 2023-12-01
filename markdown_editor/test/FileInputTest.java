import Invoker.Editor;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileInputTest {

    @Test
    void testFileInput() throws IOException {
        // 读取测试文件
        for(int i = 1 ;i <= 5; i++) {
            String inputFile = "test/Test_input/test"+i+".txt";
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            StringBuilder inputStringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                inputStringBuilder.append(line).append("\n");
            }

            // 设置输入流，将读取的内容作为输入
            System.setIn(new ByteArrayInputStream(inputStringBuilder.toString().getBytes()));

            // 创建编辑器和编辑工具
            Editor editor = new Editor();
            editor.parseCommand();

            // 进行断言以验证程序的输出或状态
            String line_output = null,line_result = null;
            String outputFile = "md_file/test"+ i +".txt"; // 文件路径
            File file_output = new File(outputFile);
            String resultFile = "test"+ i + "Result";
            File file_result = new File(resultFile);
            if(file_result.exists() && file_output.exists()) {
                try {
                    // 执行你的测试并将结果写入文件
                    BufferedReader outputReader = new BufferedReader(new FileReader(new File(outputFile)));
                    BufferedReader resultReader = new BufferedReader(new FileReader(new File(resultFile)));
                    while ( ((line_output = outputReader.readLine()) != null ) && ((line_result = resultReader.readLine()) != null) ) {
                        assertEquals(line_result, line_output);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
