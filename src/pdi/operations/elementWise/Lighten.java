package pdi.operations.elementWise;

public class Lighten implements ElementWiseOperation {
    private final short summand;

    public Lighten(short summand) {
        if (summand <= 0) {
            throw new IllegalArgumentException("Valor somado deve ser maior que zero");
        }
        this.summand = summand;
    }

    @Override
    public short apply(short value, short maxValue) {
        return (short) Math.min(value + this.summand, maxValue);
    }
}
