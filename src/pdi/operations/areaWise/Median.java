package pdi.operations.areaWise;

import java.util.ArrayList;
import java.util.List;

public class Median extends Mask {
    public Median(int size) {
        super(size);
    }

    @Override
    public short apply(List<Short> values, short maxValue) {
        // Ordenando valores de entrada
        List<Short> sorted = new ArrayList<>(values);
        sorted.sort(Short::compare);
        return sorted.get(sorted.size() / 2);
    }
}
