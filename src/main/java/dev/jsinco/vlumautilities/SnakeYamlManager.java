package dev.jsinco.vlumautilities;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class SnakeYamlManager {

    private final static Yaml yaml = new Yaml();
    private final static File dataFolder = VLumaUtilities.getDataFolder().toFile();
    private final static Logger logger = VLumaUtilities.getLogger();

    private final File file;
    private Map<String, Object> config;


    public SnakeYamlManager(String fileName) {
        this(new File(dataFolder, fileName));
    }

    public SnakeYamlManager(File file) {
        this.file = file;

        if (generateFile()) {
            try (InputStream inputStream = Files.newInputStream(file.toPath())) {

            } catch (IOException e) {
                logger.error("Error loading file: " + e.getMessage());
            }
        }
    }


    public SnakeYamlManager saveFile() {
        if (isYaml(file)) {
            try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {
                fil
            } catch (IOException e) {
                logger.error("Error saving file: " + e.getMessage());
            }
        }
        return this;
    }


    public SnakeYamlManager loadFile() {
        if (isYaml(file)) {
            try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                config = yaml.load(inputStream);
            } catch (IOException e) {
                logger.error("Error loading file: " + e.getMessage());
            }
        }
        return this;
    }

    @Nullable
    public SnakeYamlManager generateFile() {
        if (!file.exists()) {
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getName())) {
                file.createNewFile();


                if (inputStream != null) {
                    config = yaml.load(inputStream);
                    OutputStream outputStream = Files.newOutputStream(file.toPath());
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    outputStream.close();
                }

            } catch (IOException e) {
                logger.error("Error creating file: " + e.getMessage());
            }
        }
        if (isYaml(file)) {
            return this;
        }
        return null;
    }

    public void testYaml() {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("test.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
    }


    public static boolean isYaml(File file) {
        return file.isFile() && (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml"));
    }

    public void save() {
        Preconditions.checkArgument(file != null, "File cannot be null");

        com.google.common.io.Files.createParentDirs(file);

        String data = saveToString();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);

        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }
}
