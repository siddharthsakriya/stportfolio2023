package uk.ac.ed.inf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.handlers.OrderHandler;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class OrderHandlerUnitTests {

    private OrderHandler orderHandler;
    private Restaurant dominos;
    private Restaurant civs;
    private Restaurant mozza;
    private Restaurant papajohn;

    @Before
    public void setUp() {
        //Create an instance of order handler
        orderHandler = new OrderHandler();

        //Create dummy pizza data for each restaurant
        Pizza dominosPizza1 = new Pizza("Margherita", 500);
        Pizza dominosPizza2 = new Pizza("Pepperoni", 700);
        Pizza dominosPizza3 = new Pizza("Hawaiian", 600);
        Pizza[] dominosPizzas = {dominosPizza1, dominosPizza2, dominosPizza3};
        DayOfWeek[] dominosOpeningHours = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
        dominos = new Restaurant("Dominos", null, dominosOpeningHours ,dominosPizzas);

        Pizza civsPizza1 = new Pizza("Meat Feast", 800);
        Pizza civsPizza2 = new Pizza("Veggie", 600);
        Pizza civsPizza3 = new Pizza("Vegan", 900);
        Pizza[] civsPizzas = {civsPizza1, civsPizza2, civsPizza3};
        DayOfWeek[] civsOpeningHours = {DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
        civs = new Restaurant("Civs", null, civsOpeningHours, civsPizzas);

        Pizza mozzaPizza1 = new Pizza("Cheese", 600);
        Pizza mozzaPizza2 = new Pizza("Chicken", 700);
        Pizza mozzaPizza3 = new Pizza("BBQ", 600);
        Pizza[] mozzaPizzas = {mozzaPizza1, mozzaPizza2, mozzaPizza3};
        DayOfWeek[] mozzaOpeningHours = {DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
        mozza = new Restaurant("Mozza", null, mozzaOpeningHours, mozzaPizzas);

        Pizza papajohnPizza1 = new Pizza("Seafood", 400);
        Pizza papajohnPizza2 = new Pizza("Mushroom", 500);
        Pizza papajohnPizza3 = new Pizza("Pineapple", 600);
        Pizza[] papajohnPizzas = {papajohnPizza1, papajohnPizza2, papajohnPizza3};
        DayOfWeek[] papajohnOpeningHours = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
        papajohn = new Restaurant("Papa John", null, papajohnOpeningHours, papajohnPizzas);
    }

    //card number tests
    @Test
    public void cardNumberTooShort() {
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Margherita", 500)};
        CreditCardInformation cardInformation = new CreditCardInformation("772727",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1100, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cardNumberTooLong() {
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Margherita", 500)};
        CreditCardInformation cardInformation = new CreditCardInformation("7728383837747447727",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1100, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cardNumberNotInt() {
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Margherita", 500)};
        CreditCardInformation cardInformation = new CreditCardInformation("helloworlddddddd",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1100, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cardNumberEmpty() {
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Margherita", 500)};
        CreditCardInformation cardInformation = new CreditCardInformation("",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1100, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    //size tests
    @Test
    public void orderSizeTooLarge(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2800, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED, order.getOrderValidationCode());
    }

    @Test
    public void orderSizeOnBoundary(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2300, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

    @Test
    public void expiryDateBeforeOrder() {
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/11","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void expiryDateIncorrectFormat() {
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "2939/292920","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void expiryDateString() {
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "helloworld","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void expirySameAsOrder(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "10/23","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }
    @Test
    public void expirySameAsOrder2(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "10/23","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,31),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }


    @Test
    public void expiryEmpty(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,31),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvTooShort(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "10/25","1");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvTooLong(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "10/25","1");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvNotInt(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        //making cvv three characters long so we can check the second condition works
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "10/25","wow");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvEmpty(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600)};
        //making cvv three characters long so we can check the second condition works
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "10/25","");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void pizzaDoesntExist(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Margherita", 500), new Pizza("Ham & Pineapple", 600),};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2300, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, order.getOrderValidationCode());
    }

    @Test
    public void pizzaDoesntExist2(){
        Pizza pizzas[] = { new Pizza("Hawaiian", 600), new Pizza("Margherita", 500),
                new Pizza("Ham & Pineapple", 600), new Pizza("Mac&Cheese",700)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2500, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, order.getOrderValidationCode());
    }

    @Test
    public void pizzaFrom2Restaurants(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Margherita", 500), new Pizza("BBQ", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2300, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, order.getOrderValidationCode());
    }

    @Test
    public void orderTotalTooHigh(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Margherita", 500),};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2300, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.TOTAL_INCORRECT, order.getOrderValidationCode());
    }

    @Test
    public void orderTotalTooLow(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Margherita", 500),};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 100, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.TOTAL_INCORRECT, order.getOrderValidationCode());
    }

    @Test
    public void orderTotalNegativeVal(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Margherita", 500),};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, -3100, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.TOTAL_INCORRECT, order.getOrderValidationCode());
    }

    @Test
    public void pizzaFrom3Restaurants(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),
                new Pizza("Meat Feast", 800), new Pizza("BBQ", 600)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2600, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, order.getOrderValidationCode());
    }

    @Test
    public void restaurantClosedDominos(){
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Hawaiian", 600),};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");

        //set order date to a day when dominos is closed
        Order order = new Order("B183772", LocalDate.of(2024, 01,06),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1200, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        //check that the order is invalid and the validation code is restaurant closed
        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.RESTAURANT_CLOSED, order.getOrderValidationCode());
    }

    @Test
    public void restaurantClosedPapaJohns(){
        Pizza pizzas[] = {new Pizza("Mushroom", 500), new Pizza("Seafood", 400),};
        CreditCardInformation cardInformation = new CreditCardInformation("7727279182738471",
                "11/25","321");

        //set order date to a day when dominos is closed
        Order order = new Order("B183772", LocalDate.of(2024, 01,05),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1000, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        //check that the order is invalid and the validation code is restaurant closed
        Assert.assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.RESTAURANT_CLOSED, order.getOrderValidationCode());
    }

    @Test
    public void successCase() {
        Pizza pizzas[] = {new Pizza("Margherita", 500), new Pizza("Margherita", 500)};
        CreditCardInformation cardInformation = new CreditCardInformation("7727278877272788",
                "11/25","321");
        Order order = new Order("B183772", LocalDate.of(2023, 10,16),
                OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1100, pizzas , cardInformation);

        orderHandler.validateOrder(order, new Restaurant[]{dominos, civs, mozza, papajohn});

        Assert.assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, order.getOrderStatus());
        Assert.assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

}
