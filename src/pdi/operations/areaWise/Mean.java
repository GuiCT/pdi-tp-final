package pdi.operations.areaWise;

import java.util.List;

/**
 * <h1>Máscara da média</h1>
 * <p>
 * Utilizada para prover o efeito de "borrar" uma imagem.
 * Média aritmética dos valores presentes em uma vizinhança.
 * Isto é, a soma de todos os valores dividio pelo número de valores presentes.
 * </p>
 */
public class Mean extends Mask {
    public Mean(int size) {
        super(size);
    }

    @Override
    public int apply(List<Integer> values, int maxValue) {
        // Reduce -> Inicia com um valor (0) e aplica uma função (soma)
        // para cada elemento presente na lista de valores.
        int sum = values.stream().reduce(0, Integer::sum);
        // Média aritmética:
        // Divide a soma de todos os valores pelo número de valores.
        return (sum / values.size());
    }
}
