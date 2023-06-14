package pdi.operations.areaWise;

import java.util.List;

public class Laplacian extends Mask {
    private final int[] maskValues;

    public Laplacian(boolean invert, boolean diagonal) {
        super(3);
        maskValues = new int[9];
        maskValues[1] = 1;
        maskValues[3] = 1;
        maskValues[4] = -4;
        maskValues[5] = 1;
        maskValues[7] = 1;

        if (diagonal) {
            maskValues[0] = 1;
            maskValues[2] = 1;
            maskValues[6] = 1;
            maskValues[8] = 1;
        }

        if (invert) {
            for (int i = 0; i < 9; i++) {
                maskValues[i] = - maskValues[i];
            }
        }
    }

    @Override
    public int apply(List<Integer> values, int maxValue) {
        assert (values.size() == 9): "Quantidade de pixels não corresponde ao tamanho da máscara";
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += values.get(i) * maskValues[i];
        }
        return (short) sum;
    }
}
