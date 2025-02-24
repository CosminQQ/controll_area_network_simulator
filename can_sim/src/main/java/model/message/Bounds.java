package model.message;

public class Bounds {
    private double upperBound;
    private double lowerBound;

    public Bounds(double lowerBound, double upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

}
