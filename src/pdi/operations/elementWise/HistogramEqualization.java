package pdi.operations.elementWise;

import pdi.Channel;

/**
 * <h1>Equalização de histograma</h1>
 * <p>
 * Equaliza o histograma de uma imagem a partir de um mapeamento
 * de valores dos <i>pixels</i> originais para os <i>pixels</i>
 * resultantes, com base na distribuição de probabilidades dos
 * valores dos <i>pixels</i> originais.
 * </p>
 */
public class HistogramEqualization implements ElementWiseOperation {
    /**
     * Mapeamento de valores dos <i>pixels</i> originais para os
     * <i>pixels</i> resultantes.
     */
    private final int[] mapping;

    /**
     * Construtor que calcula o mapeamento de valores dos <i>pixels</i>
     * originais para os <i>pixels</i> resultantes a partir de um
     * canal de uma imagem, utilizando o seu histograma.
     * 
     * @param channel Canal da imagem utilizado para calcular o mapeamento
     */
    public HistogramEqualization(Channel channel) {
        int maxValue = channel.getMaxValue();
        int numberOfPixels = channel.width * channel.height;
        int[] histogram = channel.getHistogram();
        double[] probabilities = new double[histogram.length];
        // Calcula a probabilidade de cada valor de pixel
        for (int i = 0; i < histogram.length; i++) {
            probabilities[i] = ((double) histogram[i]) /
                    ((double) numberOfPixels);
        }
        // Formando o mapeamento utilizado
        this.mapping = new int[histogram.length];
        for (int i = 0; i < histogram.length; i++) {
            // Acumula as probabilidades de 0 até o valor atual
            double sumProbabilities = 0;
            for (int j = 0; j <= i; j++) {
                sumProbabilities += probabilities[j];
            }
            // Multiplica a soma das probabilidades pelo valor máximo
            this.mapping[i] = (int) Math.round(sumProbabilities * (double) maxValue);
        }
    }

    @Override
    public int apply(int value, int maxValue) {
        // Utiliza o mapa para retornar o valor do pixel resultante
        return this.mapping[value];
    }
}
