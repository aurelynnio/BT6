package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    public static final String CART_SESSION_KEY = "cart";

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Product product, int quantity) {
        getCart(session).addItem(product, quantity);
    }

    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        getCart(session).updateItem(productId, quantity);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        getCart(session).removeItem(productId);
    }

    public void clearCart(HttpSession session) {
        getCart(session).clear();
    }
}
