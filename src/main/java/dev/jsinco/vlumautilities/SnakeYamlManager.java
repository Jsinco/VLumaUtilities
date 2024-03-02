package dev.jsinco.vlumautilities;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class SnakeYamlManager {

    private final static DumperOptions options = new DumperOptions();
    static {
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setWidth(80);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
    }
    private final static Yaml yaml = new Yaml(options);
    private final static File dataFolder = VLumaUtilities.getDataFolder().toFile();
    private final static Logger logger = VLumaUtilities.getLogger();


    private final File file;
    private Map<String, Object> config;

    public SnakeYamlManager(String fileName) {
        this(new File(dataFolder, fileName));
    }

    public SnakeYamlManager(File file) {
        this.file = file;
    }

    public SnakeYamlManager loadImaginaryFile() {
        if (isYaml(file)) {
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getName())) {
                config = yaml.load(inputStream);
            } catch (IOException e) {
                logger.error("Error loading imaginary file: " + e.getMessage());
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
        if (isYaml(file)) {
            try {
                config = yaml.load(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                return null;
            }
            return this;
        }
        return null;
    }

    public @NotNull String saveToString() {
        StringWriter writer = new StringWriter();
        yaml.dump(config, writer);
        return writer.toString();
    }

    public void save() {
        Preconditions.checkArgument(file != null, "File cannot be null");

        String data = saveToString();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            writer.write(data);
        } catch (IOException e) {
            logger.error("Error saving file: " + e.getMessage());
        }
    }


    public String getString(String path) {
        return (String) config.get(path);
    }
    public int getInt(String path) {
        return (int) config.get(path);
    }
    public boolean getBoolean(String path) {
        return (boolean) config.get(path);
    }
    public double getDouble(String path) {
        return (double) config.get(path);
    }
    public long getLong(String path) {
        return (long) config.get(path);
    }
    public float getFloat(String path) {
        return (float) config.get(path);
    }
    public byte getByte(String path) {
        return (byte) config.get(path);
    }
    public short getShort(String path) {
        return (short) config.get(path);
    }
    public Object getObject(String path) {
        return config.get(path);
    }

    public void setString(String path, String value) {
        config.put(path, value);
    }
    public void setInt(String path, int value) {
        config.put(path, value);
    }
    public void setBoolean(String path, boolean value) {
        config.put(path, value);
    }
    public void setDouble(String path, double value) {
        config.put(path, value);
    }
    public void setLong(String path, long value) {
        config.put(path, value);
    }
    public void setFloat(String path, float value) {
        config.put(path, value);
    }
    public void setByte(String path, byte value) {
        config.put(path, value);
    }
    public void setShort(String path, short value) {
        config.put(path, value);
    }
    public void setObject(String path, Object value) {
        config.put(path, value);
    }



    public static boolean isYaml(File file) {
        return file.isFile() && (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml"));
    }
}
