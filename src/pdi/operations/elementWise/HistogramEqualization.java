package pdi.operations.elementWise;

import pdi.Channel;

public class HistogramEqualization implements ElementWiseOperation {
    private final int[] mapping;

    public HistogramEqualization(Channel channel) {
        int maxValue = channel.getMaxValue();
        int numberOfPixels = channel.width * channel.height;
        int[] histogram = channel.getHistogram();
        double[] probabilities = new double[histogram.length];
        for (int i = 0; i < histogram.length; i++) {
            probabilities[i] = 
                ((double) histogram[i]) /
                ((double) numberOfPixels);
        }
        this.mapping = new int[histogram.length];
        for (int i = 0; i < histogram.length; i++) {
            double sumProbabilities = 0;
            for (int j = 0; j <= i; j++) {
                sumProbabilities += probabilities[j];
            }
            this.mapping[i] = (int) sumProbabilities * maxValue;
        }
    }

    @Override
    public int apply(int value, int maxValue) {
        return this.mapping[value];
    }
}
