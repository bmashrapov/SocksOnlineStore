package me.mashrapov.socksonlinestore.services;

import java.io.File;

public interface OperationsFileService {
    boolean saveToFile(String json);

    String readFromFile();

    File getDataFile();

    boolean cleanDataFile();
}
