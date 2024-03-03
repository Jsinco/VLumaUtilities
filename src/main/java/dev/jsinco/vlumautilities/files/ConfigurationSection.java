package dev.jsinco.vlumautilities.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class ConfigurationSection {

    protected LinkedHashMap<String, Object> data;

    public ConfigurationSection() {
    }

    public ConfigurationSection(LinkedHashMap<String, Object> data) {
        this.data = data;
    }

    public List<String> getKeys() {
        return new ArrayList<>(data.keySet());
    }

    public boolean contains(String key) {
        return data.containsKey(key);
    }

    public Object get(String key) {
        return data.get(key);
    }

    //
    public ConfigurationSection getConfigurationSection(String key) {
        return new ConfigurationSection((LinkedHashMap<String, Object>) data.get(key));
    }

    private ConfigurationSection getFromLastSection(String path) {
        String[] keys = path.split("\\.");
        ConfigurationSection section = this;
        for (String key : keys) {
            section = section.getConfigurationSection(key);
        }
        return section;
    }


    public String getString(String path) {
        return (String) getFromLastSection(path).get(path);
    }
    public int getInt(String path) {
        //return (int) getSection(path).get(path);
        return 0;
    }
    public boolean getBoolean(String path) {
        return (boolean) data.get(path);
    }
    public double getDouble(String path) {
        return (double) data.get(path);
    }
    public long getLong(String path) {
        return (long) data.get(path);
    }
    public float getFloat(String path) {
        return (float) data.get(path);
    }
    public byte getByte(String path) {
        return (byte) data.get(path);
    }
    public short getShort(String path) {
        return (short) data.get(path);
    }
    // TODO: Lists, configuration sections
    public List<String> getStringList(String path) {
        if (data.get(path) instanceof List<?>) {
            return (List<String>) data.get(path);
        }
        return Collections.emptyList();
    }
    public Object getObject(String path) {
        return data.get(path);
    }

    public void setString(String path, String value) {
        data.put(path, value);
    }
    public void setInt(String path, int value) {
        data.put(path, value);
    }
    public void setBoolean(String path, boolean value) {
        data.put(path, value);
    }
    public void setDouble(String path, double value) {
        data.put(path, value);
    }
    public void setLong(String path, long value) {
        data.put(path, value);
    }
    public void setFloat(String path, float value) {
        data.put(path, value);
    }
    public void setByte(String path, byte value) {
        data.put(path, value);
    }
    public void setShort(String path, short value) {
        data.put(path, value);
    }
    public void setObject(String path, Object value) {
        data.put(path, value);
    }
}
