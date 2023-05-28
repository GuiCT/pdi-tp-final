package pdi.operations.elementWise;

public class Invert implements ElementWiseOperation {
    @Override
    public int apply(int value, int maxValue) {
        return maxValue - value;
    }
}
