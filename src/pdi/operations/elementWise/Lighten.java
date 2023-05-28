package pdi.operations.elementWise;

public class Lighten implements ElementWiseOperation {
    private final int summand;

    public Lighten(int summand) {
        assert summand > 0 : "Valor somado deve ser maior que zero";
        this.summand = summand;
    }

    @Override
    public int apply(int value, int maxValue) {
        return Math.min(value + this.summand, maxValue);
    }
}
