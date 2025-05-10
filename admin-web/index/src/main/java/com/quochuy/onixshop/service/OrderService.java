package com.quochuy.onixshop.service;

import com.quochuy.onixshop.model.OrderModel;
import com.quochuy.onixshop.model.UserModel;
import com.quochuy.onixshop.repository.OrderRepository;
import com.quochuy.onixshop.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách đơn hàng
    public List<OrderModel> getAllOrders() throws Exception {
        List<OrderModel> orders = orderRepository.getAllOrders().get();
        // Đảo ngược danh sách: đơn mới nhất lên đầu
        Collections.reverse(orders);
        List<UserModel> users = userRepository.getAllUsers().get();

        Map<String, UserModel> userMap = users.stream()
            .collect(Collectors.toMap(UserModel::getId, u -> u));

        for (OrderModel order : orders) {
            UserModel user = userMap.get(order.getUserId());
            if (user != null) {
                order.setUserFirstName(user.getFirstName());
                order.setUserLastName(user.getLastName());
            }
        }

        return orders;
    }

    //Cập nhật trạng thái đơn hàng
    public void updateOrderStatus(String orderId, String status) {
        orderRepository.updateOrderStatus(orderId, status);
    }

    // tìm kiếm đơn hàng và phân trang
    public List<OrderModel> getFilteredOrders(String search, String status, String dateFrom, String dateTo, int page, int size) throws Exception {
    List<OrderModel> all = getAllOrders();

    if (!search.isEmpty()) {
        all = all.stream().filter(o ->
            o.getUserId().toLowerCase().contains(search.toLowerCase()) ||
            (o.getUserFirstName() + " " + o.getUserLastName()).toLowerCase().contains(search.toLowerCase())
        ).collect(Collectors.toList());
    }

    if (!status.isEmpty()) {
        all = all.stream()
            .filter(o -> o.getOrderStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }

    // Parse ngày và lọc khoảng thời gian
    if (!dateFrom.isEmpty() || !dateTo.isEmpty()) {
        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfOrder = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 

        Date from = null, to = null;
        try {
            if (!dateFrom.isEmpty()) from = sdfInput.parse(dateFrom);
            if (!dateTo.isEmpty()) {
                // set to cuối ngày
                Date temp = sdfInput.parse(dateTo);
                to = new Date(temp.getTime() + (24 * 60 * 60 * 1000 - 1));
            }
        } catch (Exception e) {
            e.printStackTrace(); // parse fail
        }

        Date finalFrom = from;
        Date finalTo = to;
        all = all.stream().filter(o -> {
            try {
                Date orderDate = sdfOrder.parse(o.getOrderDate());
                return (finalFrom == null || !orderDate.before(finalFrom)) &&
                       (finalTo == null || !orderDate.after(finalTo));
            } catch (Exception e) {
                return false;
            }
        }).collect(Collectors.toList());
    }

    int from = Math.max(0, (page - 1) * size);
    int to = Math.min(from + size, all.size());
    return all.subList(from, to);
}

    public int getTotalPages(String search, String status, String dateFrom, String dateTo, int size) throws Exception {
        int total = getFilteredOrders(search, status, dateFrom, dateTo, 1, Integer.MAX_VALUE).size();
        return (int) Math.ceil((double) total / size);
    }

    public Map<String, Double> getMonthlyRevenueByYear(String year) throws Exception {
        List<OrderModel> orders = orderRepository.getAllOrders().get();
        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat displayFormat = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat keyDateFormat = new SimpleDateFormat("yyyy-MM");
    
        Map<Date, Double> sortedMap = new TreeMap<>();
    
        for (OrderModel order : orders) {
            // Bỏ qua nếu đơn không hợp lệ
            if (order.getOrderDate() == null || order.getTotalPrice() <= 0) continue;
    
            //
            if (!"Đã nhận".equalsIgnoreCase(order.getOrderStatus())) continue;
    
            try {
                Date fullDate = sdfInput.parse(order.getOrderDate());
                String orderYear = new SimpleDateFormat("yyyy").format(fullDate);
                if (year != null && !year.isEmpty() && !orderYear.equals(year)) continue;
    
                // Làm tròn về đầu tháng
                Date monthDate = keyDateFormat.parse(new SimpleDateFormat("yyyy-MM").format(fullDate));
                sortedMap.put(monthDate, sortedMap.getOrDefault(monthDate, 0.0) + order.getTotalPrice());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Date, Double> entry : sortedMap.entrySet()) {
            result.put(displayFormat.format(entry.getKey()), entry.getValue());
        }
        return result;
    }
    

    
}
