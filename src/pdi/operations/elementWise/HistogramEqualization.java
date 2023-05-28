package pdi.operations.elementWise;

import pdi.Channel;

import java.util.Arrays;

public class HistogramEqualization implements ElementWiseOperation {
    private final int[] newValues;

    public HistogramEqualization(Channel channel) {
        int totalPixels = channel.width * channel.height;
        int[] histogram = channel.getHistogram();
        double[] probability = Arrays.stream(histogram)
                .mapToDouble(value -> ((double) value) / ((double) totalPixels))
                .toArray();
        this.newValues = Arrays.stream(probability)
                .mapToInt(value -> (int) Math.round(value * channel.maxValue))
                .toArray();
    }

    @Override
    public int apply(int pixel, int maxValue) {
        return this.newValues[pixel];
    }
}
