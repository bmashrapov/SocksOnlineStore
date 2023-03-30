package me.mashrapov.socksonlinestore.services;

import java.io.File;

public interface SocksFilesService {
    boolean saveToFile(String json);

    String readFromFile();

    File getDataFile();

    boolean cleanDataFile();
}
