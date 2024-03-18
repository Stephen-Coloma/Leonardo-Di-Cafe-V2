package server.model.analytics;

import shared.Beverage;
import shared.Food;
import shared.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyticsPageModel {
    private List<Order> orderList = new ArrayList<>();
    private HashMap<String, Food> foodList = new HashMap<>();
    private HashMap<String, Beverage> beverageList = new HashMap<>();
    private double sales;
    private int totalOrders;

    /**
     * Computes the sales made from a given date range
     *
     * @param startDate the starting date
     * @param endDate the ending date
     */
    public void computeSales(LocalDate startDate, LocalDate endDate) {
        totalOrders = 0;
        sales = 0;

        if (startDate == null && endDate == null) {
            for (Order order : orderList) {
                sales += order.getTotalPrice();
                totalOrders++;
            }
        } else {
            for (Order order : orderList) {
                LocalDate timeStamp = LocalDate.parse(order.getTimeStamp(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

                if (endDate == null) {
                    if (timeStamp.isEqual(startDate)) {
                        sales += order.getTotalPrice();
                        totalOrders++;
                    }
                } else {
                    assert startDate != null;
                    if (timeStamp.isAfter(startDate.minusDays(1)) && timeStamp.isBefore(endDate.plusDays(1))) {
                        sales += order.getTotalPrice();
                        totalOrders++;
                    }
                }
            }
        }
    } // end of computeSales

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void setFoodList(HashMap<String, Food> foodList) {
        this.foodList = foodList;
    }

    public void setBeverageList(HashMap<String, Beverage> beverageList) {
        this.beverageList = beverageList;
    }

    public HashMap<String, Food> getFoodList() {
        return foodList;
    }

    public HashMap<String, Beverage> getBeverageList() {
        return beverageList;
    }

    public double getSales() {
        return sales;
    }

    public int getTotalOrders() {
        return totalOrders;
    }
} // end of AnalyticsPageModel
