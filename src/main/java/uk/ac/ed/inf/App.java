package uk.ac.ed.inf;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.handlers.DeliveryHandler;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class App 
{
    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.err.println("Please provide 2 arguments: date and url");
            System.exit(1);
        }

        String date = args[0];
        String url = args[1];

        ILPRestClient ilpRestClient = new ILPRestClient(url, new ObjectMapper());

        if (!isISO8601Date(date)) {
            System.err.println("Please provide a valid date in the ISO8601 format yyyy-MM-dd");
            System.exit(1);
        }

        if (!isURL(url) || !ilpRestClient.getIsAlive()) {
            System.err.println("input URL is not valid: " + url);
            System.exit(1);
        }

        DeliveryHandler deliveryHandler = new DeliveryHandler(ilpRestClient);
        deliveryHandler.deliverOrders(date);

    }

    /**
     * Checks if a given string is a valid ISO8601 date
     * @param date
     * @return boolean
     */
    public static boolean isISO8601Date(String date) {
        try {
            DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if a given string is a valid URL
     * @param url
     * @return boolean
     */
    public static boolean isURL(String url){
        try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
