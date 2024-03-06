package dev.jsinco.vlumautilities.files;

import dev.jsinco.vlumautilities.filemanagers.JsonSavingSchema;

public class Saves {

    private static JsonSavingSchema file = new JsonSavingSchema("saves.json");


    public static JsonSavingSchema getFile() {
        return file;
    }

    public static void reload() {
        file = new JsonSavingSchema("saves.json");
    }

    public static void save() {
        file.save();
    }
}
