package pdi.operations.areaWise;

import java.util.List;
import java.util.stream.IntStream;

public class Laplacian extends Mask {
    private final int[] mask;

    public Laplacian(boolean extended) {
        super(3);
        if (extended) {
            mask = new int[]{
                    0, -1, 0,
                    -1, 4, -1,
                    0, -1, 0
            };
        } else {
            mask = new int[]{
                    -1, -1, -1,
                    -1, 8, -1,
                    -1, -1, -1
            };
        }
    }

    @Override
    public int apply(List<Integer> values, int maxValue) {
        return IntStream
                .range(0, 9)
                .reduce(0, (acc, i) -> acc + values.get(i) * mask[i]);
    }
}
