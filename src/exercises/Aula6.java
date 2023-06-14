package exercises;

import pdi.PGM;
import pdi.io.NetpbmReader;
import pdi.io.NetpbmWriter;
import pdi.operations.elementWise.HistogramEqualization;

public class Aula6 {
    public static void main(String[] args) {
        try {
            PGM phistf = new NetpbmReader().readPGM("resources/2023-04-19/phistf.pgm");
            PGM phistfEqualizedHistogram = phistf.elementWiseOperation(
                new HistogramEqualization(phistf.getChannel())
            );
            new NetpbmWriter(phistfEqualizedHistogram, "resources/2023-04-19/phistfEqualizedHistogram.pgm").saveFile(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
