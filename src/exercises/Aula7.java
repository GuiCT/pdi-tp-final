package exercises;
import pdi.Channel;
import pdi.PGM;
import pdi.PPM;
import pdi.io.NetpbmReader;
import pdi.io.NetpbmWriter;
import pdi.operations.elementWise.Darken;
import pdi.operations.elementWise.Lighten;

import java.io.IOException;

public class Aula7 {
    public static void main(String[] args) {
        try {
            // Extração dos 3 canais
            PPM lenna = new NetpbmReader().readPPM("resources/2023-05-10/lenna.ppm");
            Channel[] channels = lenna.extractChannels();
            PGM lennaRedChannel = new PGM(channels[0]);
            PGM lennaGreenChannel = new PGM(channels[1]);
            PGM lennaBlueChannel = new PGM(channels[2]);
            new NetpbmWriter(lennaRedChannel, "resources/2023-05-10/lennaRedChannel.ppm").saveFile(false);
            new NetpbmWriter(lennaGreenChannel, "resources/2023-05-10/lennaGreenChannel.ppm").saveFile(false);
            new NetpbmWriter(lennaBlueChannel, "resources/2023-05-10/lennaBlueChannel.ppm").saveFile(false);
            // Alteração de um canal e recomposição
            // Diminuir o vermelho e recompor o PPM
            PGM redChannelDarkened = lennaRedChannel
                .elementWiseOperation(
                    new Darken((short) 50)
                );
            PPM lennaRedDarkened = new PPM(
                redChannelDarkened.extractChannel(),
                lennaGreenChannel.extractChannel(),
                lennaBlueChannel.extractChannel()
            );
            new NetpbmWriter(lennaRedDarkened, "resources/2023-05-10/lennaRedDarkened.ppm").saveFile(false);
            // Aumentar o azul
            PGM blueChannelLightened = lennaBlueChannel
                .elementWiseOperation(
                    new Lighten((short) 50)
                );
            PPM lennaBlueLightened = new PPM(
                lennaRedChannel.extractChannel(),
                lennaGreenChannel.extractChannel(),
                blueChannelLightened.extractChannel()
            );
            new NetpbmWriter(lennaBlueLightened, "resources/2023-05-10/lennaBlueLightened.ppm").saveFile(false);
            // Misturar os canais e visualizando
            // RBG
            PPM lennaRBG = lenna.permutateChannels("RBG");
            new NetpbmWriter(lennaRBG, "resources/2023-05-10/lennaRBG.ppm").saveFile(false);
            // GRB
            PPM lennaGRB = lenna.permutateChannels("GRB");
            new NetpbmWriter(lennaGRB, "resources/2023-05-10/lennaGRB.ppm").saveFile(false);
            // GBR
            PPM lennaGBR = lenna.permutateChannels("GBR");
            new NetpbmWriter(lennaGBR, "resources/2023-05-10/lennaGBR.ppm").saveFile(false);
            // BRG
            PPM lennaBRG = lenna.permutateChannels("BRG");
            new NetpbmWriter(lennaBRG, "resources/2023-05-10/lennaBRG.ppm").saveFile(false);
            // BGR
            PPM lennaBGR = lenna.permutateChannels("BGR");
            new NetpbmWriter(lennaBGR, "resources/2023-05-10/lennaBGR.ppm").saveFile(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
