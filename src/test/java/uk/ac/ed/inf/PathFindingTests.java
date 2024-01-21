package uk.ac.ed.inf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.handlers.LngLatHandler;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.model.Move;
import uk.ac.ed.inf.pathfinding.PathFindingAlgorithm;

import java.util.List;

public class PathFindingTests {

    NamedRegion centralArea;
    NamedRegion[] noFlyZones;
    LngLatHandler lngLatHandler = new LngLatHandler();

    @Before
    public void setUp() {
        LngLat[] centralAreaVertices = {new LngLat( -3.192473, 55.946233), new LngLat(-3.192473, 55.942617),
                new LngLat( -3.184319,55.942617), new LngLat( -3.184319,55.946233),
                new LngLat( -3.192473,55.946233)};
        centralArea = new NamedRegion("central", centralAreaVertices);

        LngLat[] noFlyZone1Vertices = {new LngLat(-3.187732739801845, 55.94461394445193), new LngLat(-3.187732739801845, 55.944148331661864),
                new LngLat(-3.1869397778490907, 55.944148331661864), new LngLat( -3.1869397778490907, 55.94461394445193),
                new LngLat(-3.187732739801845, 55.94461394445193)};

        NamedRegion noFlyZone1 = new NamedRegion("no-fly-zone-1", noFlyZone1Vertices);
        noFlyZones = new NamedRegion[]{noFlyZone1};
    }

    @Test
    public void testInsideCentralArea() {
        LngLat point = new LngLat( -3.188272079973018, 55.94386018523326);
        List<Move> path = new PathFindingAlgorithm().findPath(point, noFlyZones, centralArea, new Order());
        boolean validPath = isPathValid(path, true);
        Assert.assertFalse(path.size() > 0);
        Assert.assertFalse(validPath);
    }

    @Test
    public void testOutsideCentralArea() {
        LngLat point = new LngLat( -3.193331038834941, 55.944515874526644);
        List<Move> path = new PathFindingAlgorithm().findPath(point, noFlyZones, centralArea, new Order());
        boolean validPath = isPathValid(path, true);
        Assert.assertTrue(path.size() > 0);
        Assert.assertTrue(validPath);
    }

    public boolean isPathValid(List<Move> path, boolean inCentral) {
        for (Move move: path) {
            LngLat pos = new LngLat(move.getFromLongitude(), move.getFromLatitude());
            if (lngLatHandler.isInRegion(pos, noFlyZones[0])) {
                return false;
            }
            if (move.getAngle() == 999.0){
                break;
            }
        }

        for (Move move: path){
            if (!lngLatHandler.isInRegion(new LngLat(move.getFromLongitude(), move.getToLatitude()), centralArea)){
                inCentral = false;
            }

            LngLat pos = new LngLat(move.getFromLongitude(), move.getFromLatitude());
            if(!inCentral && lngLatHandler.isInRegion(pos, centralArea)){
                return false;
            }

            if (move.getAngle() == 999.0){
                break;
            }
        }
        return true;
    }
}
