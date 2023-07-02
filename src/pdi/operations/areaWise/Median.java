package pdi.operations.areaWise;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Máscara da mediana</h1>
 * <p>
 * Utilizada para eliminar pontos claros/escuros de uma vizinhança.
 * Utiliza o elemento central do conjunto <b>ordenado</b> de pixels na
 * vizinhança.
 * </p>
 */
public class Median extends Mask {
    public Median(int size) {
        super(size);
    }

    @Override
    public int apply(List<Integer> values, int maxValue) {
        // Ordenando valores de entrada
        List<Integer> sorted = new ArrayList<>(values);
        sorted.sort(Integer::compare);
        // Retornando valor central
        return sorted.get(sorted.size() / 2);
    }
}
