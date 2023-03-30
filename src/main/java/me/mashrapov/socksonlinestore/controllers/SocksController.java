package me.mashrapov.socksonlinestore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.mashrapov.socksonlinestore.model.Color;
import me.mashrapov.socksonlinestore.model.Size;
import me.mashrapov.socksonlinestore.model.Socks;
import me.mashrapov.socksonlinestore.services.SocksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/socks")
public class SocksController {
    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping
    @Operation(summary = "Add socks to stock", description = "Here you can add socks to stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socks added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> addSocks(@RequestParam("color") Color color,
                                      @RequestParam("size") Size size,
                                      @RequestParam("cottonPart") int cottonPart,
                                      @RequestParam("quantity") int quantity) {
        Socks socks = new Socks(color, size, cottonPart);
        socksService.addSocks(socks, quantity);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(summary = "Sell socks from stock", description = "Here you can sell socks from stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socks sold successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> sellSocks(@RequestParam("color") Color color,
                                       @RequestParam("size") Size size,
                                       @RequestParam("cottonPart") int cottonPart,
                                       @RequestParam("quantity") int quantity) {
        Socks socks = new Socks(color, size, cottonPart);
        socksService.sellSocks(socks, quantity);
        return ResponseEntity.ok("Sell socks from stock in quantity:" + quantity);
    }

    @GetMapping
    @Operation(summary = "Stock availability check", description = "Here you can check stock availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getSocksCount(@RequestParam("color") Color color,
                                                @RequestParam("size") Size size,
                                                @RequestParam(value = "cottonmin", required = false) Integer cottonMin,
                                                @RequestParam(value = "cottonmax", required = false) Integer cottonMax) {
        int count;
        if (cottonMin != null) {
            count = socksService.getSocksCount(color, size, cottonMin);
        } else if (cottonMax != null) {
            count = socksService.getSocksCount(color, size, cottonMax);
        } else {
            throw new IllegalArgumentException("Either cottonmin or cottonmax parameter should be provided");
        }
        return ResponseEntity.ok("Socks count: " + count);
    }

    @DeleteMapping
    @Operation(summary = "Remove socks from stock", description = "Here you can remove socks from stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socks removed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> removeSocks(@RequestParam("color") Color color,
                                         @RequestParam("size") Size size,
                                         @RequestParam("cottonPart") int cottonPart,
                                         @RequestParam("quantity") int quantity) {
        Socks socks = new Socks(color, size, cottonPart);
        socksService.removeSocks(socks, quantity);
        return ResponseEntity.ok("Remove socks from stock in quantity: " + quantity);
    }
}
