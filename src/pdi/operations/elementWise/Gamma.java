package pdi.operations.elementWise;

public class Gamma implements ElementWiseOperation {
    private final double c;
    private final double gamma;

    public Gamma(double c, double gamma) {
        this.c = c;
        this.gamma = gamma;
    }

    @Override
    public int apply(int pixel, int maxValue) {
        // [0, p] -> [0, 1]
        double normalizedPixelValue = (double) pixel / maxValue;
        double calculatedNormalizedValue = c * Math.pow(normalizedPixelValue, gamma);
        // [0, 1] -> [0, p]
        return (int) (calculatedNormalizedValue * maxValue);
    }
}
