package uk.ac.ed.inf.model;

public class Move {
    private String orderNo;
    private double fromLongitude;
    private double fromLatitude;
    private double angle;
    private double toLongitude;
    private double toLatitude;

    public Move(String orderNo, double fromLongitude, double fromLatitude, double toLongitude, double toLatitude, double angle){
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
    }

    public Move(){
    }

    public String getOrderNo() {
        return orderNo;
    }

    public double getFromLongitude() {
        return fromLongitude;
    }

    public double getFromLatitude() {
        return fromLatitude;
    }

    public double getToLongitude() {
        return toLongitude;
    }

    public double getToLatitude() {
        return toLatitude;
    }

    public double getAngle() {
        return angle;
    }

}
