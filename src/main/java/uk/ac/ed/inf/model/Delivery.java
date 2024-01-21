package uk.ac.ed.inf.model;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;

public class Delivery {
    private final String orderNo;
    private final OrderStatus orderStatus;
    private final OrderValidationCode orderValidationCode;
    private final int costInPence;

    public Delivery(String orderNo, OrderStatus orderStatus, OrderValidationCode orderValidationCode, int costInPence) {
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
        this.costInPence = costInPence;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderValidationCode getOrderValidationCode() {
        return orderValidationCode;
    }

    public int getCostInPence() {
        return costInPence;
    }

}
