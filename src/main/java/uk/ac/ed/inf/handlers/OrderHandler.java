package uk.ac.ed.inf.handlers;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * This class handles the validation of an order
 */
public class OrderHandler implements OrderValidation {

    /**
     *
     * @param orderToValidate
     * @param definedRestaurants
     * @return orderToValidate if it is valid, null otherwise
     */
    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants){
        if (!isCardNumInvalid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            return orderToValidate;
        }
        if (!isSizeValid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            return orderToValidate;
        }
        if (!isExpiryValid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            return orderToValidate;
        }
        if (!isCVVValid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
            return orderToValidate;
        }
        if (isPizzaValid(orderToValidate, definedRestaurants) == 1){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
            return orderToValidate;
        }
        if (!isTotalCostValid(orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            return orderToValidate;
        }
        Restaurant orderRestaurant = getOrderRestaurant(orderToValidate, definedRestaurants);
        if(orderRestaurant == null){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
            return orderToValidate;
        }
        if(isRestrauntOpen(orderRestaurant, orderToValidate)){
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
            return orderToValidate;
        }
        orderToValidate.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        return orderToValidate;
    }

    /**
     *
     * @param order
     * @return true if card number is valid, false otherwise
     */
    private Boolean isCardNumInvalid(Order order){
        String cardNum = order.getCreditCardInformation().getCreditCardNumber();
        if (cardNum.length() != 16 || !isNum(cardNum)){
            return false;
        }
        return true;
    }

    /**
     *
     * @param order
     * @return true if size is valid, false otherwise
     */
    private Boolean isSizeValid(Order order){
        Pizza[] pizzas = order.getPizzasInOrder();
        if (pizzas.length > SystemConstants.MAX_PIZZAS_PER_ORDER){
            return false;
        }
        return true;
    }

    /**
     *
     * @param order
     * @return true if expiry date is valid, false otherwise
     */
    private boolean isExpiryValid(Order order) {
        String expiryDate = order.getCreditCardInformation().getCreditCardExpiry();

        LocalDate orderDate = order.getOrderDate();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/yy");

        try {
            YearMonth expiry = YearMonth.parse(expiryDate, dateFormat);
            return orderDate.isBefore(expiry.atEndOfMonth()) || orderDate.isEqual(expiry.atEndOfMonth());

        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @param order
     * @return true if CVV is valid, false otherwise
     */
    private Boolean isCVVValid(Order order){
        String cvv = order.getCreditCardInformation().getCvv();
        if (cvv.length() != 3 || !isNum(cvv)){
            return false;
        }
        return true;
    }

    /**
     *
     * @param order
     * @param restaurants
     * @return 0 if pizza is valid, 1 if pizza is not defined, 2 if pizza is defined but price is incorrect
     */
    private int isPizzaValid(Order order, Restaurant[] restaurants){
        HashMap<String, Integer> orderPizzaMap = new HashMap<>();
        HashMap<String, Integer> restaurantPizzaMap = new HashMap<>();
        for (Restaurant restaurant: restaurants){
            for (Pizza pizza: restaurant.menu()){
                restaurantPizzaMap.put(pizza.name(), pizza.priceInPence());
            }
        }
        for(Pizza pizza: order.getPizzasInOrder()){
            orderPizzaMap.put(pizza.name(), pizza.priceInPence());
        }
        for(String pizzaName: orderPizzaMap.keySet()){
            if(!restaurantPizzaMap.containsKey(pizzaName)){
                return 1;
            }
        }
        return 0;
    }

    /**
     *
     * @param order
     * @return true if total cost is valid, false otherwise
     */
    private Boolean isTotalCostValid(Order order){
        Pizza[] pizzas = order.getPizzasInOrder();
        double correctTotal = 0;
        for(Pizza pizza : pizzas){
            correctTotal += pizza.priceInPence();
        }
        return (correctTotal+SystemConstants.ORDER_CHARGE_IN_PENCE) == order.getPriceTotalInPence();
    }

    /**
     *
     * @param order
     * @param restaurants
     * @return restaurant if order is valid, null otherwise
     */
    public Restaurant getOrderRestaurant(Order order, Restaurant[] restaurants){
        HashSet<Pizza> pizzas = new HashSet<>();
        pizzas.addAll(List.of(order.getPizzasInOrder()));
        for (Restaurant restaurant : restaurants) {
            if (Arrays.asList(restaurant.menu()).containsAll(pizzas)){;
                return restaurant;
            }
        }
        return null;
    }



    /**
     *
     * @param restaurant
     * @param orderToValidate
     * @return true if restaurant is open, false otherwise
     */
    private Boolean isRestrauntOpen(Restaurant restaurant, Order orderToValidate){
        DayOfWeek dayOfWeek = orderToValidate.getOrderDate().getDayOfWeek();
        if(!Arrays.asList(restaurant.openingDays()).contains(dayOfWeek)){
            return true;
        }
        return false;
    }

    /**
     *
     * @param num
     * @return true if num is a number, false otherwise
     */
    private Boolean isNum(String num){
        try {
            Long.parseLong(num);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
