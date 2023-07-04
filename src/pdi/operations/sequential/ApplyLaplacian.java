package pdi.operations.sequential;

import pdi.Channel;
import pdi.operations.areaWise.Laplacian;

/**
 * <h1>Aplicação do Laplaciano</h1>
 * <p>
 * Aplica o operador Laplaciano em determinado canal.
 * </p>
 */
public class ApplyLaplacian {
    /**
     * Canal a ser aplicado o operador Laplaciano.
     */
    private final Channel channel;
    /**
     * Operador Laplaciano utilizado.
     */
    private final Laplacian laplacian;

    /**
     * Cria uma operação de filtro Laplaciano com os parâmetros
     * 
     * @param channel   Canal a ser filtrado
     * @param laplacian Filtro Laplaciano a ser utilizado
     */
    public ApplyLaplacian(Channel channel, Laplacian laplacian) {
        this.channel = channel;
        this.laplacian = laplacian;
    }

    /**
     * Aplica um filtro Laplaciano em um determinado canal.
     * @return Canal filtrado
     */
    public Channel apply() {
        // Máscara gerada pela aplicação do filtro Laplaciano no canal original
        Channel mask = this.channel.maskOperation(this.laplacian);
        // Resultado final: g = f + laplacian(f)
        return this.channel.plus(mask);
    }
}
