package pdi.operations;

public class Darken implements ElementWiseOperation {
    private final short subtrahend;

    public Darken(short subtrahend) {
        if (subtrahend <= 0)
            throw new IllegalArgumentException("Subtraendo deve ser maior que zero");
        this.subtrahend = subtrahend;
    }

    @Override
    public short apply(short value, short maxValue) {
        return (short) Math.max(value - this.subtrahend, 0);
    }
}
