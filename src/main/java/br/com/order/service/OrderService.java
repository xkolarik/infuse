package br.com.order.service;

import br.com.order.entity.Customer;
import br.com.order.entity.Order;
import br.com.order.repository.CustomerRepository;
import br.com.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Order createOrder(Order order) {
        // Verifica se o pedido com o número de controle já existe
        Optional<Order> existingOrder = orderRepository.findByControlNumber(order.getControlNumber());
        if (existingOrder.isPresent()) {
            throw new IllegalArgumentException("Order with control number already exists.");
        }

        // Gera um número de controle aleatório se não for fornecido pelo cliente
        if (order.getControlNumber() == null || order.getControlNumber().isEmpty()) {
            order.setControlNumber(generateUniqueControlNumber());
        }

        // Preenche a data de cadastro com a data atual se não for fornecida
        if (order.getRegistrationDate() == null) {
            order.setRegistrationDate(LocalDateTime.now());
        }

        // Preenche a quantidade com 1 se não for fornecida
        if (order.getQuantity() == null) {
            order.setQuantity(1);
        }

        // Calcula o valor total do pedido
        BigDecimal totalPrice = calculateTotalPrice(order.getUnitPrice(), order.getQuantity());
        order.setTotalPrice(totalPrice);

        // Verifica se o cliente associado ao pedido existe no banco de dados
        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Customer must be provided and persisted before creating the order.");
        }
        Optional<Customer> existingCustomer = customerRepository.findById(order.getCustomer().getId());
        if (existingCustomer.isEmpty()) {
            throw new IllegalArgumentException("Customer with provided ID does not exist.");
        }

        // Salva o pedido no banco de dados
        return orderRepository.save(order);
    }

    private String generateUniqueControlNumber() {
        String controlNumber;
        Random random = new Random();
        do {
            controlNumber = "CN" + random.nextInt(1000000);
        } while (orderRepository.findByControlNumber(controlNumber).isPresent());
        return controlNumber;
    }

    private BigDecimal calculateTotalPrice(BigDecimal unitPrice, int quantity) {
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        if (quantity >= 10) {
            totalPrice = applyDiscount(totalPrice, 10); // Aplica 10% de desconto para quantidade maior ou igual a 10
        } else if (quantity > 5) {
            totalPrice = applyDiscount(totalPrice, 5); // Aplica 5% de desconto para quantidade maior que 5
        }
        return totalPrice;
    }

    private BigDecimal applyDiscount(BigDecimal price, int discountPercentage) {
        BigDecimal discountRate = BigDecimal.ONE.subtract(BigDecimal.valueOf(discountPercentage).divide(BigDecimal.valueOf(100)));
        return price.multiply(discountRate);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByControlNumber(String controlNumber) {
        return orderRepository.findByControlNumber(controlNumber)
                .map(List::of)
                .orElseGet(List::of);
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

}
