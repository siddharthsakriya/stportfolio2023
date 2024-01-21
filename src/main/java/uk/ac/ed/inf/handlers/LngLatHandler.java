package uk.ac.ed.inf.handlers;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

public class LngLatHandler implements LngLatHandling{

    /**
     * @param startPosition
     * @param endPosition
     * @return euclidean distance between the two positions
     */
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        double startPositionLng = startPosition.lng();
        double startPositionLat = startPosition.lat();
        double endPositionLng = endPosition.lng();
        double endPositionLat = endPosition.lat();

        double lngDiff = endPositionLng - startPositionLng;
        double ltdDiff = endPositionLat - startPositionLat;

        double lngSquared = Math.pow(lngDiff,2);
        double ltdSquared = Math.pow(ltdDiff,2);

        double distance = Math.sqrt(lngSquared + ltdSquared);
        return distance;
    }

    /**
     * @param startPosition
     * @param otherPosition
     * @return true if the other position is within the close distance of the start position
     */
    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        return distanceTo(startPosition, otherPosition) < SystemConstants.DRONE_IS_CLOSE_DISTANCE;
    }

    /**
     * Adapted from algorithm shown here to account for edge cases: https://www.youtube.com/watch?v=RSXM9bgqxJM
     * @param position
     * @param region
     * @return true if the position is in the region
     */
    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        double xp = position.lng();
        double yp = position.lat();
        int count = 0;
        LngLat[] vertices = region.vertices();

        for(LngLat lnglat : vertices){
            if(lnglat.equals(position)){
                return true;
            }
        }

        for(int i = 0; i < vertices.length; i++) {

            double x1 = vertices[i].lng();
            double y1 = vertices[i].lat();
            double x2 = vertices[(i + 1) % vertices.length].lng();
            double y2 = vertices[(i + 1) % vertices.length].lat();
            double x0 = x1 + (((yp - y1) / (y2 - y1))*(x2 - x1));

            boolean condition1 = (yp < y1) != (yp < y2);
            boolean condition2 = xp < x0;

            if ((yp == y1 && yp == y2 && ((xp < x1) != (xp < x2)))) {
                return true;
            }

            if(condition1){
                if(condition2) {
                    count++;
                }
                else if(xp == x0){
                    return true;
                }
            }
        }
        return count % 2 == 1;
    }

    /**
     * @param startPosition
     * @param angle
     * @return the next position given the start position and the angle
     */
    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        if (angle % 22.5 != 0 || angle < 0 || angle > 360){
            return startPosition;
        }

        double lng = startPosition.lng();
        double lat = startPosition.lat();

        lng = lng + (SystemConstants.DRONE_MOVE_DISTANCE * Math.cos(Math.toRadians(angle)));
        lat = lat + (SystemConstants.DRONE_MOVE_DISTANCE * Math.sin(Math.toRadians(angle)));

        return new LngLat(lng, lat);
    }
}
