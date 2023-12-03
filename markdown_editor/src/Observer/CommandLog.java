package Observer;

import Invoker.Editor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandLog {
    public String command;
    protected List<String> commandBuffer = new ArrayList<>();
    protected List<String> statsBuffer = new ArrayList<>();
    protected String logFileName;
    public List<String> currentFileName ;

    public List<Long> currentFileEditStartTime;

    public CommandLog() {
        currentFileName = new ArrayList<>();
        currentFileEditStartTime = new ArrayList<>();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        String sanitizedTimestamp = timestamp.replace(":", "_");
        String log_directory = "logs";
        logFileName = log_directory + File.separator + "command_" + sanitizedTimestamp + ".log";
        writeSessionStartLog(timestamp);
    }

    private void writeSessionStartLog(String timestamp) {
        commandBuffer.add("Session start at " + timestamp);
    }

    public void logCommand(String command) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        commandBuffer.add(timestamp + " " + command);
    }

    public void saveToLog() {
        for(String fileName: currentFileName){
            calculateWorkTime(fileName);
        }
        commandBuffer.addAll(statsBuffer);
        try (FileWriter logFile = new FileWriter(logFileName)) {
            for (String commandEntry : commandBuffer) {
                logFile.write(commandEntry + "\n");
            }
            logFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showTheHistory(int lines){
        int log_length =commandBuffer.size()-1;
        for(int i = log_length; i >= log_length-lines+1; i--){
            System.out.println(commandBuffer.get(i));
        }
        return true;
    }
    public boolean showTheHistory(){
        int log_length =commandBuffer.size()-1;
        for(int i = log_length; i >= 1; i--){
            System.out.println(commandBuffer.get(i));
        }
        return true;
    }

    public void handleLoadCommand(String file_name) {
        if(currentFileName != null&&currentFileName.contains(file_name)){
            return;
        }
        currentFileName.add(file_name);
        currentFileEditStartTime.add(System.currentTimeMillis());
    }
    public void calculateWorkTime(String fileName){
        int index = currentFileName.indexOf(fileName);
        if (index !=-1) {
            // 计算编辑时间并记录
            long editTimeMillis = System.currentTimeMillis() - currentFileEditStartTime.get(index);
            String formattedEditTime = formatEditTime(editTimeMillis);
            statsBuffer.add(currentFileName.get(index) + " " + formattedEditTime);
        }
    }
    public void endWorkRecord(String fileName){
        int index = currentFileName.indexOf(fileName);
        if(index !=-1){
            calculateWorkTime(fileName);
            currentFileName.remove(index);
            currentFileEditStartTime.remove(index);
        }
    }
    private String formatEditTime(long editTimeMillis) {
        long seconds = editTimeMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + "d " + (hours % 24) + "h " + (minutes % 60) + "min";
        } else if (hours > 0) {
            return hours + "h " + (minutes % 60) + "min";
        } else if (minutes > 0) {
            return minutes + "min " + (seconds % 60) + "s";
        } else {
            return seconds + "s";
        }
    }

    public boolean showStats(Editor editor, String types){
        List<String> tmp_stats =new ArrayList<>(statsBuffer);
        List<Long> editTimeMillis = new ArrayList<>();
        List<String> formattedEditTime = new ArrayList<>();
        if (currentFileName != null) {
            // 计算编辑时间并记录
            for(int i=0;i<currentFileName.size();i++) {
                editTimeMillis.add(System.currentTimeMillis() - currentFileEditStartTime.get(i));
                formattedEditTime.add(formatEditTime(editTimeMillis.get(i)));
                tmp_stats.add(currentFileName.get(i) + " " + formattedEditTime.get(i));
            }
        }

        if( types.equals("current") &&currentFileName != null ){
            int index = currentFileName.indexOf(editor.workspace.file_holder.getFile_path());
            System.out.println(currentFileName.get(index) + " " + formattedEditTime.get(index));
            return false;
        }
        else if(types.equals("all")){
//            if(currentFileName!=null) {
//                tmp_stats.add(currentFileName + " " + formattedEditTime);
//            }
            for(String line: tmp_stats){
                System.out.println(line);
            }
            return true;
        }
        else {
            System.out.println("No files have been edited");
            return false;
        }
    }
}
