
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Logger {
    LinkedList<File> logFiles;
    String name;
    int counter = 1;
    static int uid = 0;

    public Logger(String name){
        this.name = name;
        Logger.uid++;
    }

    public File create(){
        File newFile = new File(Logger.uid + this.name + this.counter + ".txt");
        this.counter++;
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            System.err.println(e);
        }
        logFiles.add(newFile);
        return newFile;
    }

    public void addCurrent(String string){
        File cur = logFiles.getLast();
        try (FileWriter write = new FileWriter(cur)) {
            write.write(string);
            write.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
