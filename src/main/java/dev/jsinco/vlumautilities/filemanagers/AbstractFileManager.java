package dev.jsinco.vlumautilities.filemanagers;

import dev.jsinco.vlumautilities.VLumaUtilities;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;

public abstract class AbstractFileManager extends ConfigurationSection {

    protected final static File dataFolder = VLumaUtilities.getDataFolder().toFile();
    protected final static Logger logger = VLumaUtilities.getLogger();

    protected final File file;


    public AbstractFileManager(File file) {
        this.file = file;
    }

    @Nullable
    public abstract AbstractFileManager generateFile();

    public abstract AbstractFileManager loadFile();

    public abstract AbstractFileManager loadImaginaryFile();

    public abstract void save();

}
