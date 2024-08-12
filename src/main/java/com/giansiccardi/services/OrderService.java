package com.giansiccardi.services;

import com.giansiccardi.enums.OrderStauts;
import com.giansiccardi.enums.OrderType;
import com.giansiccardi.models.*;
import com.giansiccardi.repository.OrderItemRepository;
import com.giansiccardi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final OrderItemRepository orderItemRepository;
    private final AssetServices assetServices;

    public Order createOrder(Customer customer, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setOrderStauts(OrderStauts.PENDING);
        return orderRepository.save(order);

    }

    public Order getOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(() -> new Exception("orden no encontrada"));
    }


    private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice) {

        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, Customer customer) throws Exception {
        if (quantity <= 0) {
            throw new Exception("La cantidad debe ser mayor a 0");
        }

        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(customer, orderItem, OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order, customer);
        order.setOrderStauts(OrderStauts.SUCCES);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);

        //asset
        Asset oldAsset= assetServices.findAssetByCustomerIdAndConId(order.getCustomer().getId(),
                order.getOrderItem().getCoin().getId());
        if(oldAsset==null){
        assetServices.crearAsset(customer,orderItem.getCoin(),orderItem.getQuantity());
        }else{
            assetServices.updateAsset(oldAsset.getId(), quantity);
        }

        return savedOrder;
    }

    @Transactional
    public Order sellAsset(Coin coin, double quantity, Customer customer) throws Exception {
        if (quantity <= 0) {
            throw new Exception("La cantidad debe ser mayor a 0");
        }
        double sellPrice = coin.getCurrentPrice();
        Asset assetToSell=assetServices.findAssetByCustomerIdAndConId(customer.getId(), coin.getId());

        double buyPrice = assetToSell.getBuyPrice();

        if(assetToSell!=null){

            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);




        Order order = createOrder(customer, orderItem, OrderType.SELL);
        orderItem.setOrder(order);
        if (assetToSell.getQuantity() >= quantity) {
            order.setOrderStauts(OrderStauts.SUCCES);
            order.setOrderType(OrderType.SELL);
            Order savedOrder = orderRepository.save(order);
            walletService.payOrderPayment(order, customer);
            Asset updateAsset=assetServices.updateAsset(assetToSell.getId(),-quantity);
            if (updateAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                assetServices.deleteAsset(updateAsset.getId());
            }
            return savedOrder;
        } else throw new Exception("Saldo insufciente para comprar");
        }
throw new Exception("Asset no encontrado");
    }

    public List<Order> getAllOrdersOfCustomer(Long customerId, OrderType orderType, String assetSymbol) {

        return orderRepository.findByCustomerId(customerId);
    }

    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, Customer customer) throws Exception {
        if (orderType == OrderType.BUY) {
            return buyAsset(coin, quantity, customer);
        } else if (orderType.equals(OrderType.SELL)){
            return sellAsset(coin, quantity, customer);
    }
throw  new Exception("Tipo de orden invalido");
}


}
