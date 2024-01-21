package uk.ac.ed.inf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.handlers.LngLatHandler;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

public class LngLatHandlingUnitTests {
    LngLatHandler lngLatHandler;
    NamedRegion region;

    @Before
    public void setUp() {
        lngLatHandler = new LngLatHandler();
        LngLat[] vertices = {new LngLat(1,0), new LngLat(0.5,-0.5), new LngLat(1.5,-2.4),
                new LngLat(2,0), new LngLat(2,1), new LngLat(1.5,3), new LngLat(0,2)};
        region = new NamedRegion("test", vertices);
    }

    @Test
    public void isInRegionNormal(){
        LngLat point = new LngLat(1,2);
        Assert.assertTrue(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void isInRegionNormal2(){
        LngLat point = new LngLat(1.5,-1);
        Assert.assertTrue(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void isInRegionVertex(){
        LngLat point = new LngLat(0.5,-0.5);
        Assert.assertTrue(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void isInRegionVertex2(){
        LngLat point = new LngLat(1.5,3);
        Assert.assertTrue(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void isInRegionOnLine(){
        LngLat point = new LngLat(2,0.5);
        Assert.assertTrue(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void isInRegionOnLine2(){
        LngLat point = new LngLat(0.5,1);
        Assert.assertTrue(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void isInRegionOutside(){
        LngLat point = new LngLat(5,1);
        Assert.assertFalse(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void isInRegionOutside2(){
        LngLat point = new LngLat(-2,-1);
        Assert.assertFalse(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void testIsInRegionTop(){
        LngLat position = new LngLat(-3.185292,55.942618);
        LngLat[] polyCoords = {new LngLat(-3.186844,  55.946242),
                new LngLat(-3.186844,  55.942618),
                new LngLat(-3.185242,  55.942618),
                new LngLat(-3.185242,  55.946242)};
        NamedRegion region = new NamedRegion("testRegion", polyCoords);
        boolean result = lngLatHandler.isInRegion(position, region);
        Assert.assertTrue(result);
    }

    @Test
    public void isInRegionOutsideEdgeCase(){
        LngLat point = new LngLat(2.0001,0.2);
        Assert.assertFalse(lngLatHandler.isInRegion(point, region));
    }

    @Test
    public void testDistanceTo(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat endPosition = new LngLat(4.848484,5.92992);
        double result = lngLatHandler.distanceTo(startPosition, endPosition);
        Assert.assertEquals(5.379331689456113, result, 0.000000000001);
    }

    @Test
    public void testDistanceTo2(){
        LngLat startPosition = new LngLat(-2.3,3.3);
        LngLat endPosition = new LngLat(4.1,-7.6);
        double result = lngLatHandler.distanceTo(startPosition, endPosition);
        Assert.assertEquals(12.640015822775, result, 0.0000000000001);
    }

    @Test
    public void testDistanceToSame(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat endPosition = new LngLat(1.68687,1.5777474);
        double result = lngLatHandler.distanceTo(startPosition, endPosition);
        Assert.assertEquals(0, result, 0.0000000000001);
    }

    @Test
    public void testNextPosition(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat result = lngLatHandler.nextPosition(startPosition, 0);
        Assert.assertEquals(1.68687 + SystemConstants.DRONE_MOVE_DISTANCE, result.lng(), 0.0000000000001);
        Assert.assertEquals(1.5777474, result.lat(), 0.0000000000001);
    }

    @Test
    public void testNextPosition2(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat result = lngLatHandler.nextPosition(startPosition, 90);
        Assert.assertEquals(1.68687, result.lng(), 0.0000000000001);
        Assert.assertEquals(1.5777474 + SystemConstants.DRONE_MOVE_DISTANCE, result.lat(), 0.0000000000001);
    }

    @Test
    public void testNextPosition3(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat result = lngLatHandler.nextPosition(startPosition, 67.5);
        Assert.assertEquals(1.686927402514855, result.lng(), 0.0000000000001);
        Assert.assertEquals(1.577885981929877, result.lat(), 0.0000000000001);
    }

    @Test
    public void testNextPosition4(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat result = lngLatHandler.nextPosition(startPosition, 202.5);
        System.out.println(result);
        Assert.assertEquals(1.686731418070123, result.lng(), 0.0000000000001);
        Assert.assertEquals(1.577689997485145, result.lat(), 0.0000000000001);
    }

    @Test
    public void testNextPositionInvalidAngle(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat result = lngLatHandler.nextPosition(startPosition, 20);
        Assert.assertEquals(1.68687, result.lng(), 0.0000000000001);
        Assert.assertEquals(1.5777474, result.lat(), 0.0000000000001);
    }

    @Test
    public void testNextPositionInvalidAngle2(){
        LngLat startPosition = new LngLat(1.8348484,1.5777474);
        LngLat result = lngLatHandler.nextPosition(startPosition, 382.5);
        Assert.assertEquals(1.8348484, result.lng(), 0.0000000000001);
        Assert.assertEquals(1.5777474, result.lat(), 0.0000000000001);
    }

    @Test
    public void testNextPositionInvalidAngle3(){
        LngLat startPosition = new LngLat(1.8348484,1.5777474);
        LngLat result = lngLatHandler.nextPosition(startPosition, -22.5);
        Assert.assertEquals(1.8348484, result.lng(), 0.0000000000001);
        Assert.assertEquals(1.5777474, result.lat(), 0.0000000000001);
    }

    @Test
    public void isCloseToSamePosition(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat endPosition = new LngLat(1.68687,1.5777474);
        Assert.assertTrue(lngLatHandler.isCloseTo(startPosition, endPosition));
    }

    @Test
    public void isCloseToSamePosition2(){
        LngLat startPosition = new LngLat(1.68687,1.5777474);
        LngLat endPosition = new LngLat(1.68697,1.5777874);
        Assert.assertTrue(lngLatHandler.isCloseTo(startPosition, endPosition));
    }

    @Test
    public void isCloseToSamePosition3(){
        LngLat startPosition = new LngLat(4.68687,71.5777474);
        LngLat endPosition = new LngLat(1.68697,1.5777874);
        Assert.assertFalse(lngLatHandler.isCloseTo(startPosition, endPosition));
    }
}
