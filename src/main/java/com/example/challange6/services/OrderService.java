package com.example.challange6.services;

import com.example.challange6.dto.order.request.CreateOrderRequestDTO;
import com.example.challange6.dto.order.response.CreateOrderResponseDTO;
import com.example.challange6.dto.order.response.GetOrderResponseDTO;
import com.example.challange6.dto.order.response.OrderDetailResponseDTO;
import com.example.challange6.models.OrderDetail;
import com.example.challange6.models.Orders;
import com.example.challange6.models.Products;
import com.example.challange6.models.Users;
import com.example.challange6.repository.OrderDetailRepository;
import com.example.challange6.repository.OrderRepository;
import com.example.challange6.repository.ProductRepository;
import com.example.challange6.repository.UserRepository;
import com.example.challange6.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public CreateOrderResponseDTO createOrder(UUID userId, CreateOrderRequestDTO createOrderRequestDTO) {
        Users user = userRepository.findById(userId).orElse(null);

        if (user != null ) {

            Orders order = new Orders();
            order.setUser(user);
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
            Date orderTime = DateUtil.convertToDate(localDateTime);
            order.setOrder_time(orderTime);
            order.setDestinationAddress(createOrderRequestDTO.getDestinationAddress());
            order.setCompleted(true);
            Orders savedOrder = orderRepository.save(order);
            for (CreateOrderRequestDTO.OrderDetailDTO orderDetailDTO : createOrderRequestDTO.getOrderDetails()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(savedOrder);

                // Check if the product exists before setting its ID
                Products product = productRepository.findById(orderDetailDTO.getProductId()).orElse(null);
                if (product != null) {
                    orderDetail.setProduct(product);

                    orderDetail.setQuantity(orderDetailDTO.getQuantity());
                    orderDetail.setTotalPrice(orderDetailDTO.getQuantity() * product.getPrice()); // set Total Price

                    orderDetailRepository.save(orderDetail);
                } else {
                    return null;
                }
            }

            // Add the saved order to the user's list of orders
            user.getOrders().add(savedOrder);
            userRepository.save(user);

            // Create a response DTO
            CreateOrderResponseDTO responseDTO = new CreateOrderResponseDTO();
            responseDTO.setId(savedOrder.getId());
            responseDTO.setOrder_time(savedOrder.getOrder_time());
            responseDTO.setDestinationAddress(savedOrder.getDestinationAddress());
            responseDTO.setCompleted(true);

            return responseDTO;
        }
        return null;
    }

    // GET ORDER
    public List<GetOrderResponseDTO> getAllOrders() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapOrderToDTO)
                .collect(Collectors.toList());
    }

    private GetOrderResponseDTO mapOrderToDTO(Orders order) {
        GetOrderResponseDTO responseDTO = new GetOrderResponseDTO();
        responseDTO.setId(order.getId());
        responseDTO.setOrderTime(order.getOrder_time());
        responseDTO.setDestinationAddress(order.getDestinationAddress());
        responseDTO.setOrderDetails(mapOrderDetailsToDTO(order.getOrderDetails()));
        responseDTO.setCompleted(order.getCompleted());
        return responseDTO;
    }

    // Get order by ID || Return to InvoiceService
    public List<Orders> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    private List<OrderDetailResponseDTO> mapOrderDetailsToDTO(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> {
                    OrderDetailResponseDTO detailDTO = new OrderDetailResponseDTO();
                    detailDTO.setId(detail.getId());

                    // Check if the Product is not null before getting its ID
                    if (detail.getProduct() != null) {
                        detailDTO.setProductId(detail.getProduct().getId());
                    }
                    detailDTO.setQuantity(detail.getQuantity());
                    detailDTO.setTotalPrice(detail.getTotalPrice());
                    return detailDTO;
                })
                .collect(Collectors.toList());
    }
}
