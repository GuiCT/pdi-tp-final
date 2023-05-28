package pdi.operations.areaWise;

import java.util.ArrayList;
import java.util.List;

public class Median extends Mask {
    public Median(int size) {
        super(size);
    }

    @Override
    public int apply(List<Integer> values, int maxValue) {
        // Ordenando valores de entrada
        List<Integer> sorted = new ArrayList<>(values);
        sorted.sort(Integer::compare);
        return sorted.get(sorted.size() / 2);
    }
}
