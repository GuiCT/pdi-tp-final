package exercises;

import pdi.Channel;
import pdi.PGM;
import pdi.PPM;
import pdi.io.PGM.PGMWriter;
import pdi.io.PPM.PPMReader;
import pdi.io.PPM.PPMWriter;
import pdi.operations.elementWise.Darken;
import pdi.operations.elementWise.Lighten;

import java.io.IOException;

public class Aula7 {
    public static void main(String[] args) {
        try {
            // Extração dos 3 canais
            PPM lenna = PPMReader.readPPM("resources/2023-05-10/lenna.ppm");
            Channel[] channels = lenna.extractChannels();
            PGM lennaRedChannel = new PGM(channels[0]);
            PGM lennaGreenChannel = new PGM(channels[1]);
            PGM lennaBlueChannel = new PGM(channels[2]);
            PGMWriter.writeASCII(lennaRedChannel, "resources/2023-05-10/lennaRedChannel.pgm");
            PGMWriter.writeASCII(lennaGreenChannel, "resources/2023-05-10/lennaGreenChannel.pgm");
            PGMWriter.writeASCII(lennaBlueChannel, "resources/2023-05-10/lennaBlueChannel.pgm");
            // Alteração de um canal e recomposição
            // Diminuir o vermelho e recompor o PPM
            PGM redChannelDarkened = lennaRedChannel
                    .elementWiseOperation(
                            new Darken((short) 50));
            PPM lennaRedDarkened = new PPM(
                    redChannelDarkened.extractChannel(),
                    lennaGreenChannel.extractChannel(),
                    lennaBlueChannel.extractChannel());
            PPMWriter.writeASCII(lennaRedDarkened, "resources/2023-05-10/lennaRedDarkened.ppm");
            // Aumentar o azul
            PGM blueChannelLightened = lennaBlueChannel
                    .elementWiseOperation(
                            new Lighten((short) 50));
            PPM lennaBlueLightened = new PPM(
                    lennaRedChannel.extractChannel(),
                    lennaGreenChannel.extractChannel(),
                    blueChannelLightened.extractChannel());
            PPMWriter.writeASCII(lennaBlueLightened, "resources/2023-05-10/lennaBlueLightened.ppm");
            // Misturar os canais e visualizando
            // RBG
            PPM lennaRBG = lenna.permutateChannels("RBG");
            PPMWriter.writeASCII(lennaRBG, "resources/2023-05-10/lennaRBG.ppm");
            // GRB
            PPM lennaGRB = lenna.permutateChannels("GRB");
            PPMWriter.writeASCII(lennaGRB, "resources/2023-05-10/lennaGRB.ppm");
            // GBR
            PPM lennaGBR = lenna.permutateChannels("GBR");
            PPMWriter.writeASCII(lennaGBR, "resources/2023-05-10/lennaGBR.ppm");
            // BRG
            PPM lennaBRG = lenna.permutateChannels("BRG");
            PPMWriter.writeASCII(lennaBRG, "resources/2023-05-10/lennaBRG.ppm");
            // BGR
            PPM lennaBGR = lenna.permutateChannels("BGR");
            PPMWriter.writeASCII(lennaBGR, "resources/2023-05-10/lennaBGR.ppm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
