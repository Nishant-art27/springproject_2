package com.example.e_commerce1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.e_commerce1.model.CartItem;
import com.example.e_commerce1.repository.CartRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartItem addToCart(CartItem cartItem) {
        List<CartItem> existingItems = cartRepository.findByUserId(cartItem.getUserId());
        
        for(CartItem existing : existingItems) {
            if(existing.getProductId().equals(cartItem.getProductId())) {
                existing.setQuantity(existing.getQuantity() + cartItem.getQuantity());
                return cartRepository.save(existing);
            }
        }
        
        return cartRepository.save(cartItem);
    }

    public List<CartItem> getCartItemsByUser(String userId) {
        return cartRepository.findByUserId(userId);
    }

    public void removeCartItem(String cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    public void clearCartByUser(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
