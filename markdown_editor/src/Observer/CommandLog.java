package Observer;

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
    public String currentFileName;

    public long currentFileEditStartTime;

    public CommandLog() {
        String timestamp = new SimpleDateFormat("yyyyMMSS_HH_mm_ss").format(new Date());
        String log_directory = "logs";
        logFileName = log_directory + File.separator + "command_" + timestamp + ".log";
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
        handleLoadCommand(null);
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
        if (currentFileName != null) {
            // 计算编辑时间并记录
            long editTimeMillis = System.currentTimeMillis() - currentFileEditStartTime;
            String formattedEditTime = formatEditTime(editTimeMillis);
            statsBuffer.add(currentFileName + " " + formattedEditTime);
        }
        currentFileName = file_name;
        currentFileEditStartTime = System.currentTimeMillis();
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

    public boolean showStats(String types){
        List<String> tmp_stats =new ArrayList<>(statsBuffer);
        long editTimeMillis;
        String formattedEditTime = null;
        if (currentFileName != null) {
            // 计算编辑时间并记录
            editTimeMillis = System.currentTimeMillis() - currentFileEditStartTime;
            formattedEditTime = formatEditTime(editTimeMillis);
            tmp_stats.add(currentFileName + " " + formattedEditTime);
        }

        if( types.equals("current") &&currentFileName != null ){
            System.out.println(currentFileName + " " + formattedEditTime);
            return false;
        }
        else if(types.equals("all")){
            if(currentFileName!=null) {
                tmp_stats.add(currentFileName + " " + formattedEditTime);
            }
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
