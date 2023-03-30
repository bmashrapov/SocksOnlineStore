package me.mashrapov.socksonlinestore.services;

import me.mashrapov.socksonlinestore.model.Color;
import me.mashrapov.socksonlinestore.model.Size;
import me.mashrapov.socksonlinestore.model.Socks;


public interface SocksService {
    void addSocks(Socks socks, int quantity);

    void removeSocks(Socks socks, int quantity);

    int getSocksCount(Color color, Size size, int cottonPart);

    void sellSocks(Socks socks, int quantity);
}

