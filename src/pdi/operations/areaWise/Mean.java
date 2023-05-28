package pdi.operations.areaWise;

import java.util.List;

public class Mean extends Mask {
    public Mean(int size) {
        super(size);
    }

    @Override
    public int apply(List<Integer> values, int maxValue) {
        int sum = values.stream().reduce(0, Integer::sum);
        return (sum / values.size());
    }
}
