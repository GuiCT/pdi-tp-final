package pdi;

import java.util.ArrayList;
import java.util.List;

import pdi.operations.areaWise.Mask;
import pdi.operations.elementWise.ElementWiseOperation;

/**
 * <h1>Canal de uma imagem</h1>
 * <h2>Classe mais importante do projeto!</h2>
 * <br>
 * <p>
 * Classe responsável por encapsular a responsabilidade de:
 * <ul>
 * <li>Armazenar os valores dos pixels de uma imagem</li>
 * <li>Alterar de forma segura o valor de um pixel, de forma que nunca
 * ultrapasse <i>maxVal</i></li>
 * <li>Aplicar operações <i>elementWise</i></li>
 * <li>Aplicar operações <i>areaWise</i></li>
 * <li>Entre outras funcionalidades, como cálculo de histograma e criar cópia ou
 * clone.</li>
 * </ul>
 * </p>
 */
public class Channel {
    /**
     * Largura da imagem.
     */
    public final int width;
    /**
     * Altura da imagem.
     */
    public final int height;
    /**
     * Valor máximo que um pixel pode assumir.
     */
    public final int maxValue;
    /**
     * Matriz que armazena os valores dos pixels.
     */
    private final int[][] data;

    /**
     * Cria um canal de uma imagem com os parâmetros
     * 
     * @param width    Largura da imagem
     * @param height   Altura da imagem
     * @param maxValue Valor máximo que um pixel pode assumir
     */
    public Channel(int width, int height, int maxValue) {
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.data = new int[height][width];
    }

    /**
     * <p>
     * Altera o valor de um pixel da imagem na posição [i, j]
     * </p>
     * <p>
     * Garante que o valor do pixel fique no intervalo [0, maxVal]
     * </p>
     * 
     * @param i     Linha
     * @param j     Coluna
     * @param value Novo valor do pixel
     */
    public void set(int i, int j, int value) {
        assert (value > 0) : "Valor do pixel deve ser maior ou igual a zero";
        assert (value <= this.maxValue) : "Valor do pixel deve ser menor ou igual ao valor máximo";

        this.data[i][j] = value;
    }

    /**
     * @param i Linha
     * @param j Coluna
     * @return Valor do pixel na posição [i, j]
     */
    public int get(int i, int j) {
        return this.data[i][j];
    }

    /**
     * @return Valor máximo que um <i>pixel</i> desse canal pode assumir
     */
    public int getMaxValue() {
        return this.maxValue;
    }

    /**
     * Clona um canal, criando um novo canal vazio, mas com as mesmas:
     * <ul>
     * <li>Largura</li>
     * <li>Altura</li>
     * <li>Valor máximo</li>
     * </ul>
     * 
     * @return Novo canal clonado
     */
    public Channel cloneChannel() {
        return new Channel(this.width, this.height, this.maxValue);
    }

    /**
     * Cria uma cópia do canal, ou seja, cria um novo canal com os mesmos valores
     * 
     * @return Novo canal com os mesmos valores
     */
    public Channel copyChannel() {
        Channel newChannel = this.cloneChannel();
        for (int i = 0; i < newChannel.height; i++) {
            for (int j = 0; j < newChannel.width; j++) {
                newChannel.set(i, j, this.data[i][j]);
            }
        }

        return newChannel;
    }

    /**
     * Aplica uma operação <i>elementWise</i> em todos os pixels do canal
     * <p>
     * <b>Exemplo: como aplicar uma equalização de histograma.</b>
     * 
     * <pre>
     * {@code
     * Channel equalizado = canal.elementWiseOperation(new HistogramEqualization(canal))
     * }
     * </pre>
     * </p>
     * 
     * @param operation Operação a ser aplicada
     * @return Novo canal com a operação aplicada
     * @see pdi.operations.elementWise.ElementWiseOperation Operações elementWise
     * @see pdi.operations.elementWise.ElementWiseOperation#apply(int, int)
     *      Assinatura de operações elementWise
     * @see pdi.operations.elementWise.HistogramEqualization Equalização de
     *      histograma
     */
    public Channel elementWiseOperation(ElementWiseOperation operation) {
        // Novo canal, vazio mas com as mesmas propriedades
        Channel newChannel = this.cloneChannel();
        for (int i = 0; i < newChannel.height; i++) {
            for (int j = 0; j < newChannel.width; j++) {
                // Aplica a operação e salva o resultado no novo canal
                newChannel.set(i, j, operation.apply(this.data[i][j], this.maxValue));
            }
        }

        return newChannel;
    }

    /**
     * Aplica uma operação de máscara em todos os pixels do canal
     * <p>
     * <b>Exemplo: como aplicar um filtro da média 3x3.</b>
     * 
     * <pre>
     * {@code
     * Mean filtroMedia = new Mean(3);
     * Channel borrado = canal.maskOperation(filtroMedia)
     * }
     * </pre>
     * </p>
     * 
     * @param operation Operação com máscara a ser aplicada
     * @return Novo canal com a operação aplicada
     * @see pdi.operations.areaWise.Mask Operações com máscara
     * @see pdi.operations.areaWise.Mask#apply(List, int) Assinatura de operações
     *      com máscara
     * @see pdi.operations.areaWise.Mean Filtro da média
     */
    public Channel maskOperation(Mask operation) {
        Channel newChannel = this.cloneChannel();
        for (int i = 0; i < newChannel.height; i++) {
            for (int j = 0; j < newChannel.width; j++) {
                // 1 - Seleciona os pixels vizinhos através de selectPixels
                // 2 - Aplica a operação com máscara
                // 3 - Salva o resultado no novo canal
                newChannel.set(i, j, operation.apply(
                        selectPixels(i, j, operation.size),
                        newChannel.maxValue));
            }
        }

        return newChannel;
    }

    /**
     * Subtrai o canal atual por outro canal.
     * <p>
     * É necessário que ambos os canais possuam a mesma largura, altura e valor
     * máximo por <i>pixel.</i>
     * </p>
     * 
     * @param subtrahend Canal a ser subtraído
     * @return Novo canal com a subtração aplicada
     */
    public Channel minus(Channel subtrahend) {
        assert this.width == subtrahend.width : "Largura deve ser a mesma";
        assert this.height == subtrahend.height : "Altura deve ser a mesma";
        assert this.maxValue == subtrahend.maxValue : "Valor máximo dos pixels deve ser igual";
        Channel difference = new Channel(this.width, this.height, this.maxValue);

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                int result = this.data[i][j] - subtrahend.data[i][j];
                difference.set(i, j, Math.min(Math.max(0, result), this.maxValue));
            }
        }

        return difference;
    }

    /**
     * Soma o canal atual por outro canal.
     * <p>
     * É necessário que ambos os canais possuam a mesma largura, altura e valor
     * máximo por <i>pixel.</i>
     * </p>
     * 
     * @param addend Canal a ser somado
     * @return Novo canal com a soma aplicada
     */
    public Channel plus(Channel addend) {
        assert this.width == addend.width : "Largura deve ser a mesma";
        assert this.height == addend.height : "Altura deve ser a mesma";
        assert this.maxValue == addend.maxValue : "Valor máximo dos pixels deve ser igual";
        Channel sum = new Channel(this.width, this.height, this.maxValue);

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                int result = this.data[i][j] + addend.data[i][j];
                sum.set(i, j, Math.min(Math.max(0, result), this.maxValue));
            }
        }

        return sum;
    }

    /**
     * Calcula o histograma do canal.
     * <p>
     * O histograma é a distribuição de frequência dos valores possíveis dos
     * <i>pixels</i>.
     * </p>
     * 
     * @return Histograma do canal
     */
    public int[] getHistogram() {
        int[] histogram = new int[this.maxValue + 1];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                histogram[this.data[i][j]]++;
            }
        }

        return histogram;
    }

    /**
     * Seleciona os <i>pixels</i> vizinhos de um <i>pixel</i> na posição [i, j].
     * <p>
     * Caso o <i>pixel</i> central esteja próximo da borda, os <i>pixels</i> que
     * não existirem serão preenchidos com zero.
     * </p>
     * 
     * @param i    Linha
     * @param j    Coluna
     * @param size Tamanho da vizinhança
     * @return Lista com os <i>pixels</i> vizinhos
     */
    private List<Integer> selectPixels(int i, int j, int size) {
        List<Integer> pixels = new ArrayList<>();
        // Valor inicial do offset
        // Por exemplo, para tamanho = 3
        // O offset inicia em -1 e termina em +1
        int startValue = -(size - 1) / 2;

        // Para todos os offsets, nas duas direções
        for (int ki = 0; ki < size; ki++) {
            for (int kj = 0; kj < size; kj++) {
                int iMoved = i + startValue + ki;
                int jMoved = j + startValue + kj;
                // Se na borda, usa zero
                if ((iMoved < 0 || iMoved >= this.height) ||
                        (jMoved < 0 || jMoved >= this.width)) {
                    pixels.add(0);
                } else {
                    pixels.add(this.data[iMoved][jMoved]);
                }
            }
        }

        return pixels;
    }
}
