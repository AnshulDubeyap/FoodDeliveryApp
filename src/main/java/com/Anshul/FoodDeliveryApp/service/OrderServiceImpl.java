package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.entity.OrderEntity;
import com.Anshul.FoodDeliveryApp.io.OrderRequest;
import com.Anshul.FoodDeliveryApp.io.OrderResponse;
import com.Anshul.FoodDeliveryApp.repository.CartRepository;
import com.Anshul.FoodDeliveryApp.repository.OrderRepository;
import com.Anshul.FoodDeliveryApp.service.AuthenticationFacade;
import com.Anshul.FoodDeliveryApp.service.CartService;
import com.Anshul.FoodDeliveryApp.service.OrderService;
import com.Anshul.FoodDeliveryApp.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepo;

	// for getting the logged-in user id
	@Autowired
	private UserService userService;

	@Autowired
	private CartRepository cartRepo;

	@Value("${razorpay.api.key}")
	private String RAZORPAY_KEY;

	@Value("${razorpay.api.secret}")
	private String RAZORPAY_SECRET;

	@Override
	public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
		// Convert request to entity
		OrderEntity newOrder = convertToEntity(request);

		// Save entity to the database
		newOrder = orderRepo.save(newOrder);

		// Create the Razorpay order
		RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);

		// Convert rupees to paise (integer)
		int amountInPaise = (int) Math.round(newOrder.getAmount() * 100);

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amountInPaise);
		orderRequest.put("currency", "INR");
		orderRequest.put("payment_capture", 1);

		Order razorpayOrder = razorpayClient.orders.create(orderRequest);

		// set the order id
		newOrder.setRazorpayOrderId(razorpayOrder.get("id").toString());

		// set the user id
		newOrder.setUserId(userService.findByUserId());

		// save the order
		orderRepo.save(newOrder);

		// convert entity to response
		return convertToResponse(newOrder);

	}

	@Override
	public void verifyPayment(Map<String, String> paymentData, String status) throws RazorpayException {

		// get the razorpay order id
		String razorpayOrderId = paymentData.get("razorpay_order_id");

		// get the order from the database
		 OrderEntity existingOrder = orderRepo.findByRazorpayOrderId(razorpayOrderId)
				 .orElseThrow(()-> new RuntimeException("Order not found"));

		 // update the status
		 existingOrder.setPaymentStatus(status);
		 existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
		 existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));

		 // update the order
		 orderRepo.save(existingOrder);

		 // delete the item from the cart
		if("paid".equalsIgnoreCase(status)){
			cartRepo.deleteByUserId(existingOrder.getUserId());
		}
	}

	@Override
	public List<OrderResponse> getUserOrders() {

		// get the user id
		String userId = userService.findByUserId();

		// get the order list by userId
		List<OrderEntity> orderList = orderRepo.findByUserId(userId);

		// convert into OrderResponse
		return orderList.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());

	}

	@Override
	public void removeOrder(String orderId) {
		orderRepo.deleteById(orderId);

	}

	@Override
	public List<OrderResponse> getAllOrders() {
		List<OrderEntity> orderList = orderRepo.findAll();
		return orderList.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
	}

	@Override
	public void updateOrderStatus(String orderId, String status) {

		OrderEntity order = orderRepo.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		order.setOrderStatus(status);
		orderRepo.save(order);
	}

	private OrderResponse convertToResponse(OrderEntity newOrder) {
		return OrderResponse.builder()
				.userId(newOrder.getUserId())
				.id(newOrder.getId())
				.razorpayOrderId(newOrder.getRazorpayOrderId())
				.paymentStatus(newOrder.getPaymentStatus())
				.orderStatus(newOrder.getOrderStatus())
				.amount(newOrder.getAmount())
				.userAddress(newOrder.getUserAddress())
				.email(newOrder.getEmail())
				.phoneNumber(newOrder.getPhoneNumber())
				.orderedItems(newOrder.getOrderedItems())
				.build();
	}

	private OrderEntity convertToEntity(OrderRequest request) {
		return OrderEntity.builder()
				.orderedItems(request.getOrderedItems())
				.amount(request.getAmount())
				.userAddress(request.getUserAddress())
				.email(request.getEmail())
				.phoneNumber(request.getPhoneNumber())
				.orderStatus(request.getOrderStatus())
				.build();
	}
}