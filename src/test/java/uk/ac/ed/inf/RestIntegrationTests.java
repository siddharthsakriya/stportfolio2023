package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.util.Random;

//unit tests for my rest client :)
public class RestIntegrationTests {

    ObjectMapper mapper = new ObjectMapper();

    //Getting the restaurants
    @Test
    public void testGetRest(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net", mapper);
        Restaurant[] restaurants = restController.getRestaurants();
        Assert.assertTrue(restaurants.length > 0);
    }

    //Getting the orders for randomly generated dates for september 2023
    @RepeatedTest(5)
    public void testGetOrderFromDateSep(){
        Random random = new Random();
        int day = random.nextInt(1, 30);
        String date = "";
        if(day < 10){
            String dayString = "0" + String.valueOf(day);
            date = "2023-09-" + dayString;
        }
        else {
            date = "2023-09-" + String.valueOf(day);
        }

        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net", mapper);
        Order[] orders = restController.getOrdersByDate(date);

        Assert.assertTrue(orders.length > 0);
    }

    @RepeatedTest(5)
    public void testGetOrderFromDateOct(){
        Random random = new Random();
        int day = random.nextInt(1, 31);
        String date = "";
        if(day < 10){
            String dayString = "0" + String.valueOf(day);
            date = "2023-10-" + dayString;
        }
        else {
            date = "2023-10-" + String.valueOf(day);
        }

        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net", mapper);
        Order[] orders = restController.getOrdersByDate(date);

        Assert.assertTrue(orders.length > 0);
    }

    @RepeatedTest(5)
    public void testGetOrderFromDateNov(){
        Random random = new Random();
        int day = random.nextInt(1, 30);
        String date = "";
        if(day < 10){
            String dayString = "0" + String.valueOf(day);
            date = "2023-11-" + dayString;
        }
        else {
            date = "2023-11-" + String.valueOf(day);
        }

        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net", mapper);
        Order[] orders = restController.getOrdersByDate(date);

        Assert.assertTrue(orders.length > 0);
    }

    @RepeatedTest(5)
    public void testGetOrderFromDateDec(){
        Random random = new Random();
        int day = random.nextInt(1, 31);
        String date = "";
        if(day < 10){
            String dayString = "0" + String.valueOf(day);
            date = "2023-12-" + dayString;
        }
        else {
            date = "2023-12-" + String.valueOf(day);
        }

        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net", mapper);
        Order[] orders = restController.getOrdersByDate(date);

        Assert.assertTrue(orders.length > 0);
    }

    //getting the no-fly-zones
    @Test
    public void testGetNoFlyZones(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net", mapper);
        NamedRegion[] noFlyZones = restController.getNoFlyZones();
        Assert.assertTrue(restController.getNoFlyZones().length > 0);
    }

    //getting the central area
    @Test
    public void testGetCentralArea(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net", mapper);
        Assert.assertTrue(restController.getCentralArea() != null);
    }

    //retrieving order with erroneous date
    @Test
    public void testErrorOrders(){
        ILPRestClient restController = new ILPRestClient("https://ilp-rest.azurewebsites.net", mapper);
        Order[] orders = restController.getOrdersByDate("jdjdj-10-27");
        Assert.assertTrue(orders.length == 0);
    }

    //retrieving order with erroneous link
    @Test
    public void testErrorOrders2(){
        ILPRestClient restController = new ILPRestClient("https://ilp-djnjdjn.azurewebsites.net", mapper);
        Order[] orders = restController.getOrdersByDate("2023-11-12");
        Assert.assertTrue(orders.length == 0);
    }

    //retrieving no-fly-zones with erroneous link
    @Test
    public void testErrorNoFlyZones(){
        ILPRestClient restController = new ILPRestClient("https://ilp-redhdhbdst.azurewebsites.net", mapper);
        NamedRegion[] noFlyZones = restController.getNoFlyZones();
        Assert.assertTrue(noFlyZones.length == 0);
    }

    //retrieving restaurants with erroneous link
    @Test
    public void testErrorRestaurant(){
        ILPRestClient restController = new ILPRestClient("https://ilp-uduhdsudusuh.azurewebsites.net", mapper);
        Restaurant[] restaurants = restController.getRestaurants();
        Assert.assertTrue(restaurants.length == 0);
    }

    //retrieving central area with erroneous link
    @Test
    public void  testErrorCentralArea(){
        ILPRestClient restController = new ILPRestClient("https://ilp-revjjst.azurewebsites.net/", mapper);
        NamedRegion centralArea = restController.getCentralArea();
        Assert.assertTrue(centralArea == null);
    }
}