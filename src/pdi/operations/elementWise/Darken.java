package pdi.operations.elementWise;

public class Darken implements ElementWiseOperation {
    private final int subtrahend;

    public Darken(int subtrahend) {
        assert subtrahend > 0 : "Subtraendo deve ser maior que zero";
        this.subtrahend = subtrahend;
    }

    @Override
    public int apply(int value, int maxValue) {
        return Math.max(value - this.subtrahend, 0);
    }
}
