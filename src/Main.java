import pdi.PGM;
import pdi.io.NetpbmReader;
import pdi.io.NetpbmWriter;
import pdi.operations.sequential.HighBoostPGM;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            NetpbmReader reader = new NetpbmReader();
            PGM pgm = reader.readPGM("resources/2023-05-24/dipxe_texto.pgm");
            // Aplicando mediana
            PGM res = new HighBoostPGM(pgm, 3, 10).apply();
            // Salvando
            new NetpbmWriter(res, "resources/2023-05-24/dipxe_texto_HIGH_BOOST.pgm").saveFile(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
