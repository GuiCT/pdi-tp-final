package pdi.operations.elementWise;

public class ScalarMultiplication implements ElementWiseOperation {
    private final double factor;

    public ScalarMultiplication(double factor) {
        assert factor > 0 : "Fator deve ser maior a zero";
        this.factor = factor;
    }

    @Override
    public int apply(int value, int maxValue) {
        return (int) Math.min(value * this.factor, maxValue);
    }
}
