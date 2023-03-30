package me.mashrapov.socksonlinestore.model;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Operations {
    private final OperationType operationType;
    private final LocalDateTime dateTime;
    private final int quantity;
    private final Size size;
    private final int cottonPart;
    private final Color color;

    public Operations(OperationType operationType, LocalDateTime dateTime, int quantity, Size size, int cottonPart, Color color) {
        this.operationType = operationType;
        this.dateTime = dateTime;
        this.quantity = quantity;
        this.size = size;
        this.cottonPart = cottonPart;
        this.color = color;
    }
}
