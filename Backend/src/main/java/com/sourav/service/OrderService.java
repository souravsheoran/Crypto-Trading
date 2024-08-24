package com.sourav.service;

import com.sourav.domain.OrderType;
import com.sourav.modal.Coin;
import com.sourav.modal.Order;
import com.sourav.modal.OrderItem;
import com.sourav.modal.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
