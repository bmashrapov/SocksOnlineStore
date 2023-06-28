package me.mashrapov.socksonlinestore.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.mashrapov.socksonlinestore.model.*;
import me.mashrapov.socksonlinestore.services.OperationsFileService;
import me.mashrapov.socksonlinestore.services.SocksFilesService;
import me.mashrapov.socksonlinestore.services.SocksService;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SocksServiceImpl implements SocksService {
    private static Map<Socks, Integer> socksMap = new HashMap<>();
    private static Map<LocalDateTime, Operations> operationsMap = new HashMap<>();
    private final SocksFilesService socksFileService;
    private final OperationsFileService operationsFileService;

    public SocksServiceImpl(SocksFilesService socksFilesService, OperationsFileService operationsFileService) {
        this.socksFileService = socksFilesService;
        this.operationsFileService = operationsFileService;
    }

    @PostConstruct
    private void init() {
        try {
            String json = socksFileService.readFromFile();
            socksMap = new ObjectMapper().readValue(json, new TypeReference<HashMap<Socks, Integer>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addSocks(Socks socks, int quantity) {
        if (socks.getCottonPart() <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid cottonPart or quantity");
        }
        socksMap.merge(socks, quantity, Integer::sum);
        saveToFile();
//        saveToFileOperation(OperationType.ACCEPTANCE, socks,quantity);
    }

    @Override
    public void removeSocks(Socks socks, int quantity) {
        if (socks.getCottonPart() <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid cottonPart or quantity");
        }
        Integer currentQuantity = socksMap.get(socks);
        if (currentQuantity == null || currentQuantity < quantity) {
            throw new IllegalArgumentException("Not enough socks to remove");
        }
        socksMap.computeIfPresent(socks, (k, v) -> v - quantity);
        saveToFile();
//        saveToFileOperation(OperationType.REMOVE, socks,quantity);
    }

    @Override
    public int getSocksCount(Color color, Size size, int cottonPart) {
        if (cottonPart <= 0) {
            throw new IllegalArgumentException("Invalid cottonPart");
        }
        return socksMap.entrySet().stream()
                .filter(entry -> entry.getKey().getColor() == color)
                .filter(entry -> entry.getKey().getSize() == size)
                .filter(entry -> entry.getKey().getCottonPart() >= cottonPart)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    @Override
    public void sellSocks(Socks socks, int quantity) {
        if (socks.getCottonPart() <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid cottonPart or quantity");
        }
        Integer currentQuantity = socksMap.get(socks);
        if (currentQuantity == null || currentQuantity < quantity) {
            throw new IllegalArgumentException("Not enough socks to sell");
        }
        socksMap.computeIfPresent(socks, (k, v) -> v - quantity);
        saveToFile();
//        saveToFileOperation(OperationType.SELL, socks,quantity);
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksMap);
            socksFileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        String json = socksFileService.readFromFile();
        try {
            socksMap = new ObjectMapper().readValue(json, new TypeReference<HashMap<Socks, Integer>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private void saveToFileOperation(OperationType operationType, Socks socks, int quantity) {
        LocalDateTime now = LocalDateTime.now();
        Operations operation = new Operations(operationType, now, quantity, socks.getSize(), socks.getCottonPart(), socks.getColor());
        operationsMap.put(now, operation);
        try {
            String json = new ObjectMapper().writeValueAsString(operationsMap);
            operationsFileService.saveToFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
