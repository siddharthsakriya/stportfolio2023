package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.client.ILPRestClient;
import uk.ac.ed.inf.ilp.data.*;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;

import static org.mockito.Mockito.*;

public class RestUnitTests {
    private Restaurant dominos;
    private Restaurant civs;
    private Restaurant mozza;
    private Restaurant papajohn;

    private NamedRegion[] expectedNoFlyZones;


    //TODO: Potentially remove
    @Before
    public void setUp() {
        //Create an instance of order handler

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

    //Create dummy no fly zones
        //no of
        LngLat[] lngLats1 = {new LngLat(44.944425, -3.188396), new LngLat(55.944425, -3.184319),
                new LngLat(55.942617, -3.184319), new LngLat(55.942617, -3.188396)};

        LngLat[] lngLats2 = {new LngLat(55.944425, -3.188396), new LngLat(18.3773, -3.184319),
                new LngLat(55.942617, -3.184319), new LngLat(33.81828, -3.188396)};

        LngLat[] lngLats3 = {new LngLat(55.944425, -3.188396), new LngLat(55.944425, -3.184319),
                new LngLat(55.942617, -2.98334), new LngLat(44.19283, -3.188396)};

        LngLat[] lngLats4 = {new LngLat(55.944425, -3.188396), new LngLat(33.944425, -3.184319),
                new LngLat(55.942617, -8.37737), new LngLat(55.942617, -7.188396)};

        NamedRegion meadows = new NamedRegion("Meadows", lngLats1);
        NamedRegion tollcross = new NamedRegion("Tollcross", lngLats2);
        NamedRegion bruntsfield = new NamedRegion("Bruntsfield", lngLats3);
        NamedRegion marchmont = new NamedRegion("Marchmont", lngLats4);

        expectedNoFlyZones = new NamedRegion[]{meadows, tollcross, bruntsfield, marchmont};
    }

    @Test
    public void getRestaurantsSuccessTest() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        Restaurant[] expectedRestaurants = {dominos, civs, mozza, papajohn};
        when(mockedMapper.readValue(any(URL.class), eq(Restaurant[].class)))
                .thenReturn(expectedRestaurants);
        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        Restaurant[] actualRestaurants = client.getRestaurants();
        Assert.assertArrayEquals(expectedRestaurants, actualRestaurants);
        Assert.assertEquals(expectedRestaurants.length, actualRestaurants.length);
    }

    @Test
    public void testGetRestaurantsExceptionHandling() throws Exception {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(Restaurant[].class)))
                .thenThrow(new IOException("Failed to read data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        Restaurant[] actualRestaurants = client.getRestaurants();

        Assert.assertNotNull(actualRestaurants);
        Assert.assertEquals(0, actualRestaurants.length);
    }

    @Test
    public void testGetRestaurantsExceptionHandling2() throws Exception {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(Restaurant[].class)))
                .thenThrow(new JsonMappingException("Failed to read data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        Restaurant[] actualRestaurants = client.getRestaurants();

        Assert.assertNotNull(actualRestaurants);
        Assert.assertEquals(0, actualRestaurants.length);
    }

    @Test
    public void testGetOrdersByDateSuccess() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        Order[] expectedOrders = {new Order(), new Order(), new Order()};
        when(mockedMapper.readValue(any(URL.class), eq(Order[].class)))
                .thenReturn(expectedOrders);

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        Order[] actualOrders = client.getOrdersByDate("2020-01-01");

        Assert.assertEquals(expectedOrders, actualOrders);
        Assert.assertEquals(expectedOrders.length, actualOrders.length);
    }

    @Test
    public void testGetOrderByDateExceptionHandling() throws Exception {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(Order[].class)))
                .thenThrow(new IOException("Failed to read order data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        Order[] actualOrders = client.getOrdersByDate("2020-01-01");

        Assert.assertNotNull(actualOrders);
        Assert.assertEquals(0, actualOrders.length);
    }

    @Test
    public void testGetOrderByDateExceptionHandling2() throws Exception {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(Order[].class)))
                .thenThrow(new JsonMappingException("Failed to read order data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        Order[] actualOrders = client.getOrdersByDate("2020-01-01");

        Assert.assertNotNull(actualOrders);
        Assert.assertEquals(0, actualOrders.length);
    }

    @Test
    public void testGetNoFlyZonesSuccess() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(NamedRegion[].class)))
                .thenReturn(expectedNoFlyZones);

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        NamedRegion[] actualNoFlyZones = client.getNoFlyZones();

        Assert.assertEquals(expectedNoFlyZones, actualNoFlyZones);
        Assert.assertEquals(expectedNoFlyZones.length, actualNoFlyZones.length);
    }

    @Test
    public void testGetNoFlyZonesExceptionHandling() throws IOException{
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(NamedRegion[].class)))
                .thenThrow(new IOException("Failed to read no fly zone data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        NamedRegion[] actualNoFlyZones = client.getNoFlyZones();

        Assert.assertNotNull(actualNoFlyZones);
        Assert.assertEquals(0, actualNoFlyZones.length);
    }

    @Test
    public void testGetNoFlyZonesExceptionHandlin2() throws IOException{
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(NamedRegion[].class)))
                .thenThrow(new JsonMappingException("Failed to read no fly zone data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        NamedRegion[] actualNoFlyZones = client.getNoFlyZones();

        Assert.assertNotNull(actualNoFlyZones);
        Assert.assertEquals(0, actualNoFlyZones.length);
    }

    @Test
    public void testGetCentralAreaSuccess() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        LngLat[] lngLats = {new LngLat(-3.192473, 55.946233), new LngLat(-3.192473, 55.942617),
                new LngLat(-3.184319, 55.942617), new LngLat(-3.184319, 55.946233)};

        NamedRegion expectedCentralArea = new NamedRegion("CentralArea", lngLats);
        when(mockedMapper.readValue(any(URL.class), eq(NamedRegion.class)))
                .thenReturn(expectedCentralArea);

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        NamedRegion actualCentralArea = client.getCentralArea();

        Assert.assertEquals(expectedCentralArea, actualCentralArea);
        Assert.assertNotNull(actualCentralArea);
    }

    @Test
    public void testGetCentralAreaExceptionHandling() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(NamedRegion.class)))
                .thenThrow(new IOException("Failed to read central area data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        NamedRegion actualCentralArea = client.getCentralArea();

        Assert.assertNull(actualCentralArea);
    }

    @Test
    public void testGetCentralAreaExceptionHandling2() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(NamedRegion.class)))
                .thenThrow(new JsonMappingException("Failed to read central area data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        NamedRegion actualCentralArea = client.getCentralArea();

        Assert.assertNull(actualCentralArea);
    }

    @Test
    public void testGetIsAliveSuccessTrue() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(Boolean.class)))
                .thenReturn(true);

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        boolean actualIsAlive = client.getIsAlive();

        Assert.assertTrue(actualIsAlive);
    }

    @Test
    public void testGetIsAliveSuccessFalse() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(Boolean.class)))
                .thenReturn(false);

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        boolean actualIsAlive = client.getIsAlive();

        Assert.assertFalse(actualIsAlive);
    }

    @Test
    public void testGetIsAliveExceptionHandling() throws IOException {
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(URL.class), eq(Boolean.class)))
                .thenThrow(new IOException("Failed to read is alive data"));

        ILPRestClient client = new ILPRestClient("https://somerestserver.com", mockedMapper);
        boolean actualIsAlive = client.getIsAlive();

        Assert.assertFalse(actualIsAlive);
    }
}
