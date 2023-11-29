package betterbox.bettercombatlog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PluginLogger {

    private final String logFile;

    public PluginLogger(String logFile) {
        this.logFile = logFile;
        // Sprawdzenie, czy folder istnieje, jeśli nie - stworzenie go
        File file = new File(logFile);
        File folder = file.getParentFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void log(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
            out.println(timeStamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
