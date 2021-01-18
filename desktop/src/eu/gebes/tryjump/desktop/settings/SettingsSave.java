package eu.gebes.tryjump.desktop.settings;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;


@FieldDefaults(level = AccessLevel.PUBLIC)
public class SettingsSave {
    private final String[] settings = new String[6];
    private static final String home = System.getProperty("user.home");
    private static final File FILE_DIRECTORY = new File( home + "\\AppData\\Roaming\\.tryjump\\");
    private static final File FILE_NAME = new File(FILE_DIRECTORY + "\\settings.txt");

    @SneakyThrows
    @PostConstruct
    public String[] load() {

        try {
            BufferedReader in  = new BufferedReader(new FileReader(FILE_NAME));

            int count=0;
            while ((settings[count] = in.readLine()) != null) {
                count++;
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println(FILE_NAME);
            FILE_DIRECTORY.mkdirs();
            List<String> lines = Arrays.asList("1920", "1080", "70", "0", "false");
            Files.write(Paths.get(String.valueOf(FILE_NAME)),
                    lines
                    ,StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            load();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing stream", e);
        }

        return settings;
    }

    public synchronized void save(String[] settings) {
        try {
            BufferedWriter outputWriter = null;
            outputWriter = new BufferedWriter(new FileWriter(FILE_NAME));
            for(int i=0;i<settings.length;i++){
                outputWriter.write(settings[i]);
                outputWriter.newLine();
            }

            outputWriter.flush();
            outputWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing stream", e);
        }
    }

    public String[] getSettings() {
        return settings;
    }
}