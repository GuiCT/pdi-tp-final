package pdi.operations.sequential;

import pdi.PPM;
import pdi.operations.areaWise.Mean;
import pdi.operations.elementWise.ScalarMultiplication;

public class HighBoostPPM {
    private final PPM ppm;
    private final int medianSize;
    private final double k;

    public HighBoostPPM(PPM ppm, int medianSize, double k) {
        this.ppm = ppm;
        this.medianSize = medianSize;
        this.k = k;
    }

    public PPM apply() {
        // Borra utilizando média
        PPM blurred = this.ppm.maskOperation(new Mean(medianSize));
        // Diferença
        PPM g = this.ppm.minus(blurred);
        // Somando, multiplicado por k
        PPM kg = g.elementWiseOperation(new ScalarMultiplication(this.k));
        return this.ppm.plus(kg);
    }
}
