package uk.ac.ed.inf.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.net.URL;

public class ILPRestClient {
    private static String BASE_URL;
    private static ObjectMapper mapper;

    public ILPRestClient(String url, ObjectMapper mapper) {
        this.BASE_URL = url;
        this.mapper = mapper;
    }

    /**
     * Get all restaurants
     * @return an array of restaurants
     */
    public Restaurant[] getRestaurants() {

        try {
            Restaurant[] restaurants = mapper.readValue(new URL(BASE_URL + "/restaurants"), Restaurant[].class);
            return restaurants;
        } catch (Exception e) {
            System.err.println("Unable to retrieve restaurants");
        }
        Restaurant[] restaurants = {};
        return restaurants;
    }

    /**
     * Get all orders on a specific date
     * @param date the date to get orders for
     * @return an array of orders for a specific date
     */
    public Order[] getOrdersByDate(String date) {

        mapper.registerModule(new JavaTimeModule());
        try {
            Order[] orders = mapper.readValue(new URL(BASE_URL + "/orders/" + date), Order[].class);
            return orders;
        } catch (Exception e) {
            System.err.println("Unable to retrieve orders by date: " + date);
        }
        Order[] orders = {};
        return orders;
    }

    /**
     * Get all no-fly zones
     * @return an array of no-fly zones
     */
    public NamedRegion[] getNoFlyZones(){
        try {

            NamedRegion[] noFlyZones = mapper.readValue(new URL(BASE_URL + "/noflyzones"), NamedRegion[].class);
            return noFlyZones;
        } catch (Exception e) {
            System.err.println("Unable to retrieve no-fly zones");
        }
        NamedRegion[] noFlyZones = {};
        return noFlyZones;
    }

    /**
     * Get the central area
     * @return the central area
     */
    public NamedRegion getCentralArea(){
        try {
            NamedRegion centralArea = mapper.readValue(new URL(BASE_URL + "/centralarea"), NamedRegion.class);
            return centralArea;
        } catch (Exception e) {
            System.err.println("Unable to retrieve central area");
        }
        return null;
    }

    /**
     * Get the is alive status
     * @return the is alive status
     */
    public boolean getIsAlive(){
        try {
            Boolean isAlive = mapper.readValue(new URL(BASE_URL + "/isAlive"), Boolean.class);
            return isAlive;
        } catch (Exception e) {
            System.err.println("Cannot reach the isAlive endpoint");
            return false;
        }
    }

}
