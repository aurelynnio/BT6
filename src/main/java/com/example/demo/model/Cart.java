package com.example.demo.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public void addItem(Product product, int quantity) {
        if (product == null || product.getId() == null || quantity <= 0) {
            return;
        }
        CartItem existing = items.get(product.getId());
        if (existing == null) {
            items.put(product.getId(), new CartItem(product, quantity));
            return;
        }
        existing.setQuantity(existing.getQuantity() + quantity);
    }

    public void updateItem(Long productId, int quantity) {
        if (productId == null || !items.containsKey(productId)) {
            return;
        }
        if (quantity <= 0) {
            items.remove(productId);
            return;
        }
        items.get(productId).setQuantity(quantity);
    }

    public void removeItem(Long productId) {
        if (productId != null) {
            items.remove(productId);
        }
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public int getTotalQuantity() {
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public double getTotalAmount() {
        return items.values().stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}
