package pdi.operations.sequential;

import pdi.PGM;
import pdi.io.NetpbmWriter;
import pdi.operations.areaWise.Mean;
import pdi.operations.elementWise.ScalarMultiplication;

import java.io.IOException;
import java.io.InvalidObjectException;

public class HighBoostPGM {
    private final PGM pgm;
    private final int medianSize;
    private final double k;

    public HighBoostPGM(PGM pgm, int medianSize, double k) {
        this.pgm = pgm;
        this.medianSize = medianSize;
        this.k = k;
    }

    public PGM apply() throws InvalidObjectException {
        // Borra utilizando média
        PGM blurred = this.pgm.maskOperation(new Mean(medianSize));
        // Diferença
        PGM g = this.pgm.minus(blurred);
        // Somando, multiplicado por k
        PGM kg = g.elementWiseOperation(new ScalarMultiplication(this.k));
        return this.pgm.plus(kg);
    }
}
