import pdi.PGM;
import pdi.io.NetpbmReader;
import pdi.io.NetpbmWriter;
import pdi.operations.Darken;
import pdi.operations.Lighten;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            NetpbmReader reader = new NetpbmReader();
            PGM pgm = reader.readPGM("lena256.pgm");
            // Lighten and Darken
            PGM lightenedLena = pgm.elementWiseOperation(new Lighten((short) 50));
            PGM darkenedLena = pgm.elementWiseOperation(new Darken((short) 50));
            // Salvando
            new NetpbmWriter(lightenedLena, "lena256_lightened.pgm").saveFile(false);
            new NetpbmWriter(darkenedLena, "lena256_darkened.pgm").saveFile(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
