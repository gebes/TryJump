package eu.gebes.tryjump.desktop.map;

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
public class MapManagment {
    private String[] maps;
    private final String home = System.getProperty("user.home");
    private final File FILE_DIRECTORY = new File( home + "\\AppData\\Roaming\\.tryjump\\");
    private final File FILE_NAME = new File(FILE_DIRECTORY + "\\maps.txt");

    @SneakyThrows
    @PostConstruct
    public String[] load() {

        try {
            BufferedReader in  = new BufferedReader(new FileReader(FILE_NAME));

            String[] tmp = new String[100];
            int count=0;
            while ((tmp[count] = in.readLine()) != null) {
                count++;
            }

            maps = new String[count];
            for(int i =0;i<count;i++){
                maps[i]=tmp[i];
            }
            in.close();
        } catch (FileNotFoundException e) {
            FILE_DIRECTORY.mkdirs();
            List<String> lines = Arrays.asList("Jump1", "Jump2", "Jump3");
            Files.write(Paths.get(String.valueOf(FILE_NAME)),
                    lines
                    ,StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            load();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing stream", e);
        }

        return maps;
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
        return maps;
    }
}