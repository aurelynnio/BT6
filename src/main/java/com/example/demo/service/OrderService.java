package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Account;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    public OrderService(OrderRepository orderRepository, AccountRepository accountRepository) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Order checkout(Cart cart, String loginName) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng đang trống");
        }
        Account account = accountRepository.findByLoginName(loginName)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản đặt hàng"));

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalAmount());
        order.setAccount(account);

        for (CartItem item : cart.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setProduct(item.getProduct());
            detail.setPrice(item.getProduct().getPrice());
            detail.setQuantity(item.getQuantity());
            detail.setSubtotal(item.getSubtotal());
            order.addDetail(detail);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public List<Order> getAllOrders() {
        return orderRepository.findAllWithDetails();
    }

    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
    }
}
