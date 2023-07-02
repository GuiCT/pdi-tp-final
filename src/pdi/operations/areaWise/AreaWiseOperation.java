package pdi.operations.areaWise;

import java.util.List;

/**
 * <h1>Filtragens espaciais</h1>
 * <p>
 * Interface utilizada para operações que, para alterar o valor de um pixel,
 * precisam conhecer o valor de vários outros pixels, geralmente vizinhos do
 * pixel alvo.
 * </p>
 */
public interface AreaWiseOperation {
    /**
     * Método que aplica a operação em um conjunto de valores.
     * 
     * @param values   Lista de valores a serem utilizados na operação.
     * @param maxValue Valor máximo que um pixel pode ter.
     * @return int Valor resultante da operação.
     */
    int apply(List<Integer> values, int maxValue);
}
