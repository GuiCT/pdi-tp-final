package pdi.operations.areaWise;

import java.util.List;

/**
 * <h1>Máscara da Laplaciana</h1>
 * <p>
 * Operador Laplaciano é a soma de todas as derivadas de segunda ordem.
 * Em uma imagem com duas dimensões, o operador laplaciano é a soma das
 * derivadas
 * parciais de segunda ordem em x e y.
 * </p>
 * <p>
 * Por utilizar a soma das derivadas de segunda ordem, o operador laplaciano
 * é utilizado para a detecção de bordas, visto que são pontos onde há uma
 * variação da variação.
 * </p>
 * <br>
 * <p>
 * Essa classe permite a implementação do operador laplaciano incluindo ou não
 * as diagonais, e permitindo a inversão dos sinais envolvidos. Portanto, as
 * máscaras
 * disponíveis são:
 * </p>
 * <ul>
 * <li>Sem diagonais e com sinal negativo no centro</li>
 * <li>Sem diagonais e com sinal positivo no centro (negativo no entorno)</li>
 * <li>Com diagonais e com sinal negativo no centro</li>
 * <li>Com diagonais e com sinal positivo no centro (negativo no entorno)</li>
 * </ul>
 * </p>
 */
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

        // Se a máscara deve incluir as diagonais, então os valores
        // das mesmas devem ser 1.
        if (diagonal) {
            maskValues[0] = 1;
            maskValues[2] = 1;
            maskValues[6] = 1;
            maskValues[8] = 1;
        }

        // Se a máscara deve ter o sinal invertido, então todos os valores
        // devem ser multiplicados por -1.
        if (invert) {
            for (int i = 0; i < 9; i++) {
                maskValues[i] = -maskValues[i];
            }
        }
    }

    @Override
    public int apply(List<Integer> values, int maxValue) {
        assert (values.size() == 9) : "Quantidade de pixels não corresponde ao tamanho da máscara";
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += values.get(i) * maskValues[i];
        }
        return sum;
    }
}
