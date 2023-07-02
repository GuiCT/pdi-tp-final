package pdi;

import pdi.operations.areaWise.Mask;
import pdi.operations.elementWise.ElementWiseOperation;

/**
 * <h1>Imagem PGM</h1>
 * <br>
 * <p>
 * Classe contém apenas um canal de uma imagem PGM, e a maioria das
 * responsabilidades são delegadas para a classe {@link pdi.Channel Channel}.
 * </p>
 */
public class PGM {
    /**
     * Canal de uma imagem PGM.
     */
    private final Channel grayChannel;

    /**
     * Cria uma imagem PGM com o canal passado como parâmetro.
     * 
     * @param grayChannel Canal de uma imagem PGM
     */
    public PGM(Channel grayChannel) {
        this.grayChannel = grayChannel;
    }

    /**
     * @return Largura da imagem
     */
    public int getWidth() {
        return this.grayChannel.width;
    }

    /**
     * @return Altura da imagem
     */
    public int getHeight() {
        return this.grayChannel.height;
    }

    /**
     * @return Valor máximo que um <i>pixel</i> pode assumir
     */
    public int getMaxValue() {
        return this.grayChannel.maxValue;
    }

    /**
     * @return Canal de uma imagem PGM
     * @apiNote Esse método é utilizado apenas para permitir que a classe
     *          {@link pdi.operations.elementWise.HistogramEqualization
     *          HistogramEqualization} possa acessar o canal de uma imagem PGM e
     *          obter seu histograma. Não é recomendado utilizar esse método para
     *          acessar o canal de uma imagem PGM e realizar operações.
     */
    public Channel getChannel() {
        return this.grayChannel;
    }

    /**
     * @return Valor de um <i>pixel</i> na posição [i, j]
     */
    public int get(int i, int j) {
        return this.grayChannel.get(i, j);
    }

    /**
     * Altera o valor de um <i>pixel</i> na posição [i, j]
     */
    public void set(int i, int j, int value) {
        this.grayChannel.set(i, j, value);
    }

    /**
     * Cria uma cópia da imagem PGM
     * 
     * @return Cópia da imagem PGM
     */
    public PGM copyPGM() {
        Channel newGrayChannel = this.grayChannel.copyChannel();
        return new PGM(newGrayChannel);
    }

    /**
     * Aplica uma operação {@link pdi.operations.elementWise.ElementWiseOperation
     * elementWise} na imagem PGM
     * 
     * @param operation Operação <i>elementWise</i> a ser aplicada
     * @return Imagem PGM com a operação aplicada
     */
    public PGM elementWiseOperation(ElementWiseOperation operation) {
        Channel newChannel = this.grayChannel.elementWiseOperation(operation);

        return new PGM(newChannel);
    }

    /**
     * Aplica uma operação {@link pdi.operations.areaWise.Mask com máscara} na
     * imagem PGM
     * 
     * @param operation Operação <i>Mask</i> a ser aplicada
     * @return Imagem PGM com a operação aplicada
     */
    public PGM maskOperation(Mask operation) {
        Channel newChannel = this.grayChannel.maskOperation(operation);

        return new PGM(newChannel);
    }

    /**
     * Soma duas imagens PGM
     * 
     * @param addend Imagem PGM a ser somada
     * @return Cópia de PGM, resultante da soma
     */
    public PGM plus(PGM addend) {
        return new PGM(this.grayChannel.plus(addend.grayChannel));
    }

    /**
     * Subtrai duas imagens PGM
     * 
     * @param subtrahend Imagem PGM a ser subtraída
     * @return Cópia de PGM, resultante da subtração
     */
    public PGM minus(PGM subtrahend) {
        return new PGM(this.grayChannel.minus(subtrahend.grayChannel));
    }

    /**
     * Extrai um canal de uma imagem PGM
     * <p>
     * Diferente do método {@link pdi.PGM#getChannel() getChannel()}, esse método
     * retorna uma cópia do canal, e não o canal original.
     * </p>
     * 
     * @return Canal de uma imagem PGM
     */
    public Channel extractChannel() {
        return this.grayChannel.copyChannel();
    }
}
