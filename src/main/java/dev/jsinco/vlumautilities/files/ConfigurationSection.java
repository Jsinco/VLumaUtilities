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


    public ConfigurationSection getConfigurationSection(String key) {
        data.computeIfAbsent(key, k -> new LinkedHashMap<String, Object>());
        return new ConfigurationSection((LinkedHashMap<String, Object>) data.get(key));
    }

    private Object getLastFromSection(String path) {
        List<String> keys = new ArrayList<>(List.of(path.split("\\.")));
        String lastKey = keys.remove(keys.size() - 1);
        ConfigurationSection section = this;
        for (String key : keys) {
            section = section.getConfigurationSection(key);
        }
        return section.data.get(lastKey);
    }

    private void setLastInSection(String path, Object value) {
        List<String> keys = new ArrayList<>(List.of(path.split("\\.")));
        String lastKey = keys.remove(keys.size() - 1);
        ConfigurationSection section = this;
        for (String key : keys) {
            section = section.getConfigurationSection(key);
        }
        section.data.put(lastKey, value);
    }


    public String getString(String path) {
        return (String) getLastFromSection(path);
    }

    public int getInt(String path) {
        return (int) getLastFromSection(path);
    }

    public boolean getBoolean(String path) {
        return (boolean) getLastFromSection(path);
    }

    public double getDouble(String path) {
        return (double) getLastFromSection(path);
    }

    public long getLong(String path) {
        return (long) getLastFromSection(path);
    }

    public float getFloat(String path) {
        return (float) getLastFromSection(path);
    }

    public byte getByte(String path) {
        return (byte) getLastFromSection(path);
    }

    public short getShort(String path) {
        return (short) getLastFromSection(path);
    }

    // TODO: Lists, configuration sections
    public List<String> getStringList(String path) {
        if (data.get(path) instanceof List<?>) {
            return (List<String>) data.get(path);
        }
        return Collections.emptyList();
    }

    public Object get(String path) {
        return getLastFromSection(path);
    }

    public void set(String path, Object value) {
        setLastInSection(path, value);
    }
}
