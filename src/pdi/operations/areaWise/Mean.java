package pdi.operations.areaWise;

import java.util.ArrayList;
import java.util.List;

public class Mean extends Mask {
    public Mean(int size) {
        super(size);
    }

    @Override
    public short apply(List<Short> values, short maxValue) {
        int sum = 0;
        for (Short value : values) {
            sum += value;
        }
        return (short) (sum / values.size());
    }
}
