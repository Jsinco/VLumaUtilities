package dev.jsinco.vlumautilities;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
                config = yaml.load(inputStream);
            } catch (IOException e) {
                logger.error("Error loading file: " + e.getMessage());
            }
        }
    }

    public boolean generateFile() {
        if (!file.exists()) {
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getName())) {
                file.createNewFile();


                if (inputStream != null) {
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

        return file.isFile() && isYaml(file);
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
}
