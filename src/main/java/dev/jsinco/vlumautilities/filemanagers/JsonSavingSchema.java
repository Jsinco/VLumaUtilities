package dev.jsinco.vlumautilities.filemanagers;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.LinkedHashMap;

public class JsonSavingSchema extends AbstractFileManager {

    private final static Gson gson = new Gson();


    public JsonSavingSchema(String fileName) {
        this(new File(dataFolder, fileName), false);
    }

    public JsonSavingSchema(String fileName, boolean isImaginary) {
        this(new File(dataFolder, fileName), isImaginary);
    }

    public JsonSavingSchema(File file, boolean isImaginary) {
        super(file);
        if (isImaginary) {
            loadImaginaryFile();
        } else {
            generateFile();
        }
    }


    @Override
    public JsonSavingSchema loadImaginaryFile() {
        if (isJson(file)) {
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getName())) {
                assert inputStream != null;
                data = gson.fromJson(new String(inputStream.readAllBytes(), Charsets.UTF_8), LinkedHashMap.class);
                if (data == null) {
                    data = new LinkedHashMap<>();
                }
            } catch (IOException e) {
                logger.error("Error loading imaginary file (Gson): " + e.getMessage());
            }
        }
        return this;
    }

    @Override
    public JsonSavingSchema loadFile() {
        if (isJson(file)) {
            try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                data = gson.fromJson(new String(inputStream.readAllBytes(), Charsets.UTF_8), LinkedHashMap.class);
                if (data == null) {
                    data = new LinkedHashMap<>();
                }
            } catch (IOException e) {
                logger.error("Error loading file (Gson): " + e.getMessage());
            }
        }
        return this;
    }

    @Override
    public JsonSavingSchema generateFile() {
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



    @Override
    public void save() {
        Preconditions.checkArgument(file != null, "File cannot be null");

        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(file))){
            jsonWriter.setIndent("    ");
            gson.toJson(this.data, LinkedHashMap.class, jsonWriter);
            jsonWriter.flush();
        } catch (IOException e) {
            logger.error("Error saving file (Gson): " + e.getMessage());
        }
    }


    public static boolean isJson(File file) {
        return file.isFile() && file.getName().endsWith(".json");
    }
}
