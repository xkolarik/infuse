package br.com.order.controller;

import br.com.order.entity.Order;
import br.com.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{controlNumber}")
    public ResponseEntity<List<Order>> getOrdersByControlNumber(@PathVariable String controlNumber) {
        return ResponseEntity.ok(orderService.getOrdersByControlNumber(controlNumber));
    }

}