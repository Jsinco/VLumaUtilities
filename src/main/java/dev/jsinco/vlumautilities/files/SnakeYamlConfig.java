package dev.jsinco.vlumautilities.files;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;

public class SnakeYamlConfig extends AbstractFileManager {

    private final static DumperOptions options = new DumperOptions();
    static {
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setWidth(80);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
    }
    private final static Yaml yaml = new Yaml(options);


    public SnakeYamlConfig(String fileName) {
        this(new File(dataFolder, fileName));
    }

    public SnakeYamlConfig(File file) {
        super(file);
    }

    @Override
    public SnakeYamlConfig loadImaginaryFile() {
        if (isYaml(file)) {
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getName())) {
                data = yaml.load(inputStream);
            } catch (IOException e) {
                logger.error("Error loading imaginary file: " + e.getMessage());
            }
        }
        return this;
    }

    @Override
    public SnakeYamlConfig loadFile() {
        if (isYaml(file)) {
            try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                data = yaml.load(inputStream);
            } catch (IOException e) {
                logger.error("Error loading file: " + e.getMessage());
            }
        }
        return this;
    }

    @Override
    public SnakeYamlConfig generateFile() {
        if (!file.exists()) {
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getName())) {
                file.getParentFile().mkdirs();
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
        return loadFile();
    }

    @NotNull
    public String saveToString() {
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
        return writer.toString();
    }

    @Override
    public void save() {
        Preconditions.checkArgument(file != null, "File cannot be null");

        String data = saveToString();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            writer.write(data);
        } catch (IOException e) {
            logger.error("Error saving file: " + e.getMessage());
        }
    }

    public static boolean isYaml(File file) {
        return file.isFile() && (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml"));
    }
}
