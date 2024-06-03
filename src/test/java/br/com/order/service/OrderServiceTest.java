package br.com.order.service;

import br.com.order.entity.Customer;
import br.com.order.entity.Order;
import br.com.order.repository.CustomerRepository;
import br.com.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService();
        orderService.setOrderRepository(orderRepository);
        orderService.setCustomerRepository(customerRepository);
    }

    @Test
    void createOrder_WhenControlNumberAlreadyExists_ShouldThrowException() {
        // Arrange
        Order existingOrder = new Order();
        existingOrder.setControlNumber("CN123456");
        when(orderRepository.findByControlNumber(existingOrder.getControlNumber())).thenReturn(Optional.of(existingOrder));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(existingOrder));
    }

    @Test
    void createOrder_WhenCustomerDoesNotExist_ShouldThrowException() {
        // Arrange
        Order order = new Order();
        order.setControlNumber("CN123456");

        // Definindo um cliente inválido (que não existe no repositório)
        Customer customer = new Customer();
        customer.setId(1L); // Defina um ID que não exista no repositório de clientes
        order.setCustomer(customer);

        when(orderRepository.findByControlNumber(order.getControlNumber())).thenReturn(Optional.empty());
        when(customerRepository.findById(order.getCustomer().getId())).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NullPointerException.class, () -> orderService.createOrder(order));
    }



    @Test
    void createOrder_WhenValidOrder_ShouldCreateOrder() {
        // Arrange
        Order order = new Order();
        order.setControlNumber("CN123456");

        // Definindo um valor válido para unitPrice
        order.setUnitPrice(BigDecimal.valueOf(25.99)); // Defina um valor adequado aqui

        // Criando e definindo um cliente válido
        Customer customer = new Customer();
        customer.setId(1L);
        order.setCustomer(customer);

        when(orderRepository.findByControlNumber(order.getControlNumber())).thenReturn(Optional.empty());
        when(customerRepository.findById(order.getCustomer().getId())).thenReturn(Optional.of(new Customer()));
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order createdOrder = orderService.createOrder(order);

        // Assert
        assertNotNull(createdOrder);
        assertEquals(order.getControlNumber(), createdOrder.getControlNumber());
    }


}