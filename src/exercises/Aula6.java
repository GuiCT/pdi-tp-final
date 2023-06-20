package exercises;

import pdi.PGM;
import pdi.io.PGM.PGMReader;
import pdi.io.PGM.PGMWriter;
import pdi.operations.elementWise.HistogramEqualization;

public class Aula6 {
    public static void main(String[] args) {
        try {
            PGM phistf = PGMReader.readPGM("resources/2023-04-19/phistf.pgm");
            PGM phistfEqualizedHistogram = phistf.elementWiseOperation(
                new HistogramEqualization(phistf.getChannel())
            );
            PGMWriter.writeASCII(phistfEqualizedHistogram, "resources/2023-04-19/phistfEqualizedHistogram.pgm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
