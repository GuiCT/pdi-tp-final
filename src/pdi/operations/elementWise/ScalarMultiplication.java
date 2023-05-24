package pdi.operations.elementWise;

public class ScalarMultiplication implements ElementWiseOperation {
    private final double factor;

    public ScalarMultiplication(double factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("Fator deve ser maior ou igual a zero");
        }
        this.factor = factor;
    }

    @Override
    public short apply(short value, short maxValue) {
        return (short) Math.min(value * this.factor, maxValue);
    }
}
