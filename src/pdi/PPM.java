package pdi;

import pdi.operations.areaWise.Mask;
import pdi.operations.elementWise.ElementWiseOperation;

/**
 * <h1>Imagem PPM</h1>
 * <br>
 * <p>
 * Classe contém três canais (vermelho, verde e azul) de uma imagem PPM, e a
 * maioria das
 * responsabilidades são delegadas para a classe {@link pdi.Channel Channel}.
 * </p>
 */
public class PPM {
    /**
     * Canal vermelho de uma imagem PPM.
     */
    private final Channel redChannel;
    /**
     * Canal verde de uma imagem PPM.
     */
    private final Channel greenChannel;
    /**
     * Canal azul de uma imagem PPM.
     */
    private final Channel blueChannel;

    /**
     * Cria uma imagem PPM com os canais passados como parâmetro.
     * 
     * @param redChannel   Canal vermelho de uma imagem PPM
     * @param greenChannel Canal verde de uma imagem PPM
     * @param blueChannel  Canal azul de uma imagem PPM
     */
    public PPM(Channel redChannel, Channel greenChannel, Channel blueChannel) {
        this.redChannel = redChannel;
        this.greenChannel = greenChannel;
        this.blueChannel = blueChannel;
    }

    /**
     * @return Largura da imagem
     */
    public int getWidth() {
        return this.redChannel.width;
    }

    /**
     * @return Altura da imagem
     */
    public int getHeight() {
        return this.redChannel.height;
    }

    /**
     * @return Valor máximo que um <i>pixel</i> pode assumir
     */
    public int getMaxValue() {
        return this.redChannel.maxValue;
    }

    /**
     * @return Valor do <i>pixel</i> do canal vermelho na posição [i, j]
     */
    public int getRed(int i, int j) {
        return this.redChannel.get(i, j);
    }

    /**
     * @return Valor do <i>pixel</i> do canal verde na posição [i, j]
     */
    public int getGreen(int i, int j) {
        return this.greenChannel.get(i, j);
    }

    /**
     * @return Valor do <i>pixel</i> do canal azul na posição [i, j]
     */
    public int getBlue(int i, int j) {
        return this.blueChannel.get(i, j);
    }

    /**
     * Altera o valor do <i>pixel</i> do canal vermelho na posição [i, j]
     */
    public void setRed(int i, int j, int value) {
        this.redChannel.set(i, j, value);
    }

    /**
     * Altera o valor do <i>pixel</i> do canal verde na posição [i, j]
     */
    public void setGreen(int i, int j, int value) {
        this.greenChannel.set(i, j, value);
    }

    /**
     * Altera o valor do <i>pixel</i> do canal azul na posição [i, j]
     */
    public void setBlue(int i, int j, int value) {
        this.blueChannel.set(i, j, value);
    }

    /**
     * @return Uma cópia da imagem PPM, copiando os três canais.
     */
    public PPM copyPPM() {
        Channel newRedChannel = this.redChannel.copyChannel();
        Channel newGreenChannel = this.greenChannel.copyChannel();
        Channel newBlueChannel = this.blueChannel.copyChannel();
        return new PPM(newRedChannel, newGreenChannel, newBlueChannel);
    }

    /**
     * Aplica uma operação {@link pdi.operations.elementWise.ElementWiseOperation
     * elementWise} em cada canal da imagem PPM.
     * 
     * @param operation Operação <i>elementWise</i> a ser aplicada
     * @return Imagem PPM com a operação aplicada
     */
    public PPM elementWiseOperation(ElementWiseOperation operation) {
        Channel newRedChannel = this.redChannel.elementWiseOperation(operation);
        Channel newGreenChannel = this.greenChannel.elementWiseOperation(operation);
        Channel newBlueChannel = this.blueChannel.elementWiseOperation(operation);

        return new PPM(newRedChannel, newGreenChannel, newBlueChannel);
    }

    /**
     * Aplica uma operação {@link pdi.operations.areaWise.Mask com máscara} em cada
     * canal da imagem PPM
     * 
     * @param operation Operação <i>Mask</i> a ser aplicada
     * @return Imagem PGM com a operação aplicada
     */
    public PPM maskOperation(Mask operation) {
        Channel newRedChannel = this.redChannel.maskOperation(operation);
        Channel newGreenChannel = this.greenChannel.maskOperation(operation);
        Channel newBlueChannel = this.blueChannel.maskOperation(operation);

        return new PPM(newRedChannel, newGreenChannel, newBlueChannel);
    }

    /**
     * Soma duas imagens PPM
     * 
     * @param addend Imagem PPM a ser somada
     * @return Cópia de PPM, resultante da soma
     */
    public PPM plus(PPM addend) {
        return new PPM(
                this.redChannel.plus(addend.redChannel),
                this.greenChannel.plus(addend.greenChannel),
                this.blueChannel.plus(addend.blueChannel));
    }

    /**
     * Subtrai duas imagens PPM
     * 
     * @param subtrahend Imagem PPM a ser subtraída
     * @return Cópia de PPM, resultante da subtração
     */
    public PPM minus(PPM subtrahend) {
        return new PPM(
                this.redChannel.minus(subtrahend.redChannel),
                this.greenChannel.minus(subtrahend.greenChannel),
                this.blueChannel.minus(subtrahend.blueChannel));
    }

    /**
     * Extrai os canais de uma imagem PPM, retornando um array de canais, gerando
     * uma cópia de cada canal.
     * 
     * @return Canais da imagem PPM
     */
    public Channel[] extractChannels() {
        return new Channel[] {
                this.redChannel.copyChannel(),
                this.greenChannel.copyChannel(),
                this.blueChannel.copyChannel()
        };
    }

    /**
     * Realiza a permutação de canais de uma imagem PPM, retornando uma nova imagem
     * 
     * @param order String com a ordem dos canais, por exemplo, "RGB" ou "BGR"
     * @return Nova imagem PPM com os canais permutados
     */
    public PPM permutateChannels(String order) {
        assert (order.matches("^[RGB]{3}$")) : "String só pode conter os caracteres R, G e B e ter 3 caracteres";
        Channel[] newPPMChannels = new Channel[3];

        for (int i = 0; i < 3; i++) {
            String channelLetter = order.substring(i, i + 1);
            newPPMChannels[i] = switch (channelLetter) {
                case "R" -> this.redChannel.copyChannel();
                case "G" -> this.greenChannel.copyChannel();
                case "B" -> this.blueChannel.copyChannel();
                default -> throw new IllegalStateException("Valor inesperado: " + channelLetter);
            };
        }

        return new PPM(newPPMChannels[0], newPPMChannels[1], newPPMChannels[2]);
    }
}
