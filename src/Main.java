import pdi.PGM;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Lendo arquivo PGM e salvando
        try {
            PGM pgm = new PGM("lena256_binary.pgm");
            pgm.saveFile("lena256_2.pgm");
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo PGM");
        }
    }
}
