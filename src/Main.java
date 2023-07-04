import pdi.Channel;
import pdi.PGM;
import pdi.PPM;
import pdi.io.PGM.PGMReader;
import pdi.io.PGM.PGMWriter;
import pdi.io.PPM.PPMReader;
import pdi.io.PPM.PPMWriter;
import pdi.operations.areaWise.Laplacian;
import pdi.operations.areaWise.Mean;
import pdi.operations.areaWise.Median;
import pdi.operations.elementWise.HistogramEqualization;
import pdi.operations.sequential.ApplyLaplacian;
import pdi.operations.sequential.HighBoostPGM;
import pdi.operations.sequential.HighBoostPPM;
import utils.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public Main() {
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private final List<Status> statuses = new ArrayList<>();
    private PGM pgmFile;
    private PPM ppmFile;
    private Channel redChannel;
    private Channel greenChannel;
    private Channel blueChannel;

    public void run() throws IOException {
        printHeader();
        while (true) {
            printMenu();
            dealChosenOption();
        }
    }

    private void printHeader() {
        System.out.println("""
                Trabalho de Processamento Digital de Imagens.
                Alunos: Guilherme Cesar Tomiasi e Carlos Eduardo Fernandes de Santana
                ================================================================================
                """);
    }

    private void printMenu() throws IOException {
        System.out.println("""
                Opções:
                1 - Carregar um arquivo .pgm
                2 - Carregar um arquivo .ppm
                3 - Juntar RGB
                4 - Sair""");
    }

    private void dealChosenOption() {
        int chosenOption = promptUntilValid("Escolha uma opção: ", List.of(1, 2, 3, 4));
        switch (chosenOption) {
            case 1 -> {
                try {
                    loadPGM();
                } catch (IOException e) {
                    System.out.println("Erro ao carregar imagem!");
                }
            }
            case 2 -> {
                try {
                    loadPPM();
                } catch (IOException e) {
                    System.out.println("Erro ao carregar imagem!");
                }
            }

            case 3 -> {
                try{
                juntarRGB();
                } catch (IOException e) {
                    System.out.println("Erro ao carregar imagem!");
                }
            }
            case 4 -> System.exit(0);
            default -> System.out.println("Opção inválida!");
        }
    }

    /**
     * Junta os canais RGB em uma imagem PPM.
     * <p>
     *     O usuário deve escolher um canal de cada vez, e o programa irá juntar os canais
     *     em uma imagem PPM.
     *     <br>
     *
     */
    private void juntarRGB() throws IOException {
        System.out.println("Escolha canal vermelho:");

        File[] availableFiles = listAllFiles(".ppm");
        if (availableFiles == null)
            return;
        File chosenFile = choseOneFile(availableFiles);
        PGM chosenFileImage = PGMReader.readPGM(chosenFile.getPath());
        this.redChannel = chosenFileImage.getChannel();

        System.out.println("Escolha canal verde:");
        availableFiles = listAllFiles(".ppm");
        if (availableFiles == null)
            return;
        chosenFile = choseOneFile(availableFiles);
        chosenFileImage = PGMReader.readPGM(chosenFile.getPath());
        this.greenChannel = chosenFileImage.getChannel();

        System.out.println("Escolha canal azul:");
        availableFiles = listAllFiles(".ppm");
        if (availableFiles == null)
            return;
        chosenFile = choseOneFile(availableFiles);
        chosenFileImage = PGMReader.readPGM(chosenFile.getPath());
        this.blueChannel = chosenFileImage.getChannel();

        PPM ppm = new PPM(redChannel, greenChannel, blueChannel);
        PPMWriter.writeASCII(ppm, "saida/rgb.ppm");
        System.out.println("Imagem salva em saida/rgb.ppm");
    }

    private File[] listAllFiles(String suffix) {
        // Listar todos os arquivos de uma determinada extensão no diretório /images
        File[] availableFiles = Paths
                .get("images")
                .toFile()
                .listFiles((dir, name) -> name.endsWith(suffix));
        assert availableFiles != null;
        if (availableFiles.length == 0) {
            System.out.println("Nenhum arquivo encontrado!");
            return null;
        }
        for (int i = 0; i < availableFiles.length; i++) {
            System.out.printf("%d - %s%n", i + 1, availableFiles[i].getName());
        }
        return availableFiles;
    }

    private File choseOneFile(File[] availableFiles) {
        int chosenFileIdx = promptUntilValid(
                "Escolha uma imagem para carregar: ",
                IntStream.range(1, availableFiles.length + 1)
                        .boxed()
                        .toList());

        return availableFiles[chosenFileIdx - 1];
    }

    private void loadPGM() throws IOException {
        File[] availableFiles = listAllFiles(".pgm");
        if (availableFiles == null)
            return;
        File chosenFile = choseOneFile(availableFiles);
        PGM chosenFileImage = PGMReader.readPGM(chosenFile.getPath());
        this.pgmFile = chosenFileImage;
        pgmOperations(chosenFileImage);

    }

    private void loadPPM() throws IOException {
        File[] availableFiles = listAllFiles(".ppm");
        if (availableFiles == null)
            return;
        File chosenFile = choseOneFile(availableFiles);
        PPM chosenFileImage = PPMReader.readPPM(chosenFile.getPath());
        this.ppmFile = chosenFileImage;
        ppmOperations(chosenFileImage);
    }

    /**
     * Operações que podem ser feitas em uma imagem PGM.
     * <p>
     *     O usuário deve escolher uma operação, e o programa irá aplicar a operação
     *     na imagem PGM escolhida.
     *     <br>
     *     As operações disponíveis são:
     *     <ul>
     *         <li>Aplicar transformação High Boost</li>
     *         <li>Aplicar filtro da média</li>
     *         <li>Aplicar filtro da mediana</li>
     *         <li>Aplicar filtro Laplaciano</li>
     *         <li>Equalizar histograma</li>
     *         <li>Separar RGB</li>
     *         <li>Voltar</li>
     *         <br>
 *         </ul>
     *
     *     O arquivo resultante será salvo em /saida.
     *     <br>
     *     <p>
     *         A opção 6 é exclusiva para imagens PPM, e separa os canais RGB em imagens PGM.
     *     </p>
     *
     * @param chosenFileImage
     * @throws IOException
     */
    private void ppmOperations(PPM chosenFileImage) throws IOException {
        System.out.println("""
                Opções:
                1 - Aplicar transformação High Boost
                2 - Aplicar filtro da média
                3 - Aplicar filtro da mediana
                4 - Aplicar filtro Laplaciano
                5 - Equalizar histograma
                6 - Separar RGB
                0 - Voltar""");
        int chosenOption = promptUntilValid("Escolha uma opção: ", List.of(1, 2, 3, 4, 5, 6, 0));
        switch (chosenOption){
            case 1 -> {
                System.out.println("Insira o valor da dimensão da máscara: "); // Valor de n, assim a operação aplica um máscara n*n.
                Scanner scanner = new Scanner(System.in);
                int n = scanner.nextInt();

                System.out.println("Insira o valor de k: ");
                double k = scanner.nextDouble();

                HighBoostPPM highBoostPPM = new HighBoostPPM(chosenFileImage, n, k);
                this.ppmFile = highBoostPPM.apply();

                try{
                    PPMWriter.writeASCII(this.ppmFile, "saida/highBoostRgb.ppm"); // TODO Iterar sobre o nome do
                    // arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com filtro de alta potência aplicado!");
            }
            case 2 -> {
                System.out.println("Insira o valor da dimensão da máscara: "); // Valor de n, assim a operação aplica um máscara n*n.
                Scanner scanner = new Scanner(System.in);
                int n = scanner.nextInt();

                PPM meanPPM = this.ppmFile.maskOperation(new Mean(n));
                this.ppmFile = meanPPM;

                try{
                    PPMWriter.writeASCII(this.ppmFile, "saida/meanRgb.ppm"); // TODO Iterar sobre o nome do arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com filtro da média aplicado!");
            }
            case 3 ->{
                System.out.println("Insira o valor da dimensão da máscara: "); // Valor de n, assim a operação aplica um máscara n*n.
                Scanner scanner = new Scanner(System.in);
                int n = scanner.nextInt();

                PPM medianPPM = this.ppmFile.maskOperation(new Median(n));
                this.ppmFile = medianPPM;

                try{
                    PPMWriter.writeASCII(this.ppmFile, "saida/medianRgb.ppm"); // TODO Iterar sobre o nome do arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com filtro da mediana aplicado!");

            }
            case 4 -> {
                PPM laplacianPPM = this.ppmFile.maskOperation(new Laplacian(false, false));
                ApplyLaplacian applyLaplacianRed = new ApplyLaplacian(this.ppmFile.extractChannels()[0],
                        new Laplacian(false, false));
                ApplyLaplacian applyLaplacianGreen = new ApplyLaplacian(this.ppmFile.extractChannels()[1],
                        new Laplacian(false, false));
                ApplyLaplacian applyLaplacianBlue = new ApplyLaplacian(this.ppmFile.extractChannels()[2],
                        new Laplacian(false, false));
                this.ppmFile = new PPM(applyLaplacianRed.apply(), applyLaplacianGreen.apply(), applyLaplacianBlue.apply());

                try{
                    PPMWriter.writeASCII(this.ppmFile, "saida/laplacianRgb.ppm"); // TODO Iterar sobre o nome do arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com filtro laplaciano aplicado!");
            }
            case 5 -> {
                Channel redChannel = this.ppmFile.extractChannels()[0];
                Channel redChannelEqualized = redChannel.elementWiseOperation(new HistogramEqualization(redChannel));

                Channel greenChannel = this.ppmFile.extractChannels()[1];
                Channel greenChannelEqualized = greenChannel.elementWiseOperation(new HistogramEqualization(greenChannel));

                Channel blueChannel = this.ppmFile.extractChannels()[2];
                Channel blueChannelEqualized = blueChannel.elementWiseOperation(new HistogramEqualization(blueChannel));

                PPM equalizedPPM = new PPM(redChannelEqualized, greenChannelEqualized, blueChannelEqualized);
                this.ppmFile = equalizedPPM;

                try{
                    PPMWriter.writeASCII(this.ppmFile, "saida/rgb_equalized.ppm"); // TODO Iterar sobre o nome do
                    // arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com histograma equalizado!");
            }
            case 6 -> {
                Channel[] channels = this.ppmFile.extractChannels();
                PGM redChannel = new PGM(channels[0]);
                PGM greenChannel = new PGM(channels[1]);
                PGM blueChannel = new PGM(channels[2]);
                PGMWriter.writeASCII(redChannel, "saida/red.pgm");
                PGMWriter.writeASCII(greenChannel, "saida/green.pgm");
                PGMWriter.writeASCII(blueChannel, "saida/blue.pgm");
                System.out.println("Imagens salvas em saida/red.pgm, saida/green.pgm e saida/blue.pgm");
            }
            case 0 -> System.out.println("Voltando...");

            default -> System.out.println("Opção inválida!");


        }
    }

    /**
     * Operações que podem ser realizadas em um arquivo PGM
     * <p>
     *     Opções:
     *     <br>1 - Aplicar transformação High Boost
     *     <br>2 - Aplicar filtro da média
     *     <br>3 - Aplicar filtro da mediana
     *     <br>4 - Aplicar filtro Laplaciano
     *     <br>5 - Equalizar histograma
     *     <br>0 - Voltar
     *     </p>
     *     <p>
     *         O arquivo resultante é salvo na pasta saida.
     *         </p>
     *
     *
     * @param chosenFileImage   Arquivo PGM escolhido
     */
    private void pgmOperations(PGM chosenFileImage) {
        System.out.println("""
                Opções:
                1 - Aplicar transformação High Boost
                2 - Aplicar filtro da média
                3 - Aplicar filtro da mediana
                4 - Aplicar filtro Laplaciano
                5 - Equalizar histograma
                0 - Voltar""");
        int chosenOption = promptUntilValid("Escolha uma opção: ", List.of(1, 2, 3, 4, 5, 0));
        switch (chosenOption) {
            case 1 -> {
                System.out.println("Insira o valor da dimensão da máscara: "); // Valor de n, assim a operação aplica um máscara n*n.
                Scanner scanner = new Scanner(System.in);
                int n = scanner.nextInt();

                System.out.println("Insira o valor de k: ");
                double k = scanner.nextDouble();

                HighBoostPGM highBoostPGM = new HighBoostPGM(chosenFileImage, n, k);
                this.pgmFile = highBoostPGM.apply();

                try{
                    PGMWriter.writeASCII(this.pgmFile, "saida/high-boost.pgm"); // TODO Iterar sobre o nome do arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com filtro de alta potência aplicado!");
            }
            case 2 -> {
                System.out.println("Insira o valor da dimensão da máscara: "); // Valor de n, assim a operação aplica um máscara n*n.
                Scanner scanner = new Scanner(System.in);
                int n = scanner.nextInt();

                PGM meanPGM = this.pgmFile.maskOperation(new Mean(n));
                this.pgmFile = meanPGM;

                try{
                    PGMWriter.writeASCII(this.pgmFile, "saida/mean.pgm"); // TODO Iterar sobre o nome do arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com filtro da média aplicado!");
            }
            case 3 ->{
                System.out.println("Insira o valor da dimensão da máscara: "); // Valor de n, assim a operação aplica um máscara n*n.
                Scanner scanner = new Scanner(System.in);
                int n = scanner.nextInt();

                PGM medianPGM = this.pgmFile.maskOperation(new Median(n));
                this.pgmFile = medianPGM;

                try{
                    PGMWriter.writeASCII(this.pgmFile, "saida/median.pgm"); // TODO Iterar sobre o nome do arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com filtro da mediana aplicado!");

            }
            case 4 -> {
                PGM laplacianPGM = this.pgmFile.maskOperation(new Laplacian(false, false));
                ApplyLaplacian applyLaplacian = new ApplyLaplacian(this.pgmFile.getChannel(),
                        new Laplacian(false, false));
                this.pgmFile = new PGM(applyLaplacian.apply());

                try{
                    PGMWriter.writeASCII(this.pgmFile, "saida/laplacian.pgm"); // TODO Iterar sobre o nome do arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com filtro laplaciano aplicado!");
            }
            case 5 -> {
                Channel canal = this.pgmFile.getChannel();
                Channel equalizado = canal.elementWiseOperation(new HistogramEqualization(canal));
                this.pgmFile = new PGM(equalizado);

                try{
                    PGMWriter.writeASCII(this.pgmFile, "saida/equalized.pgm"); // TODO Iterar sobre o nome do arquivo
                } catch (IOException e) {
                    System.out.println("Erro ao salvar imagem!");
                }
                System.out.println("Imagem com histograma equalizado!");
            }

            default -> System.out.println("Opção inválida!");
        }
    }

    private void printLoadedImages() {
        if (this.statuses.size() == 0) {
            System.out.println("Nenhuma imagem carregada!");
            return;
        }

        System.out.println("Imagens carregadas:");
        for (Status status : statuses) {
            System.out.println(status);
        }
    }

    private void choseLoadedImages() {
        // Índices disponíveis
        int fileCount = 0;
        if (this.pgmFile != null) {
            fileCount++;
        }
        if (this.ppmFile != null) {
            fileCount++;
        }
        List<Integer> availableIndexes = IntStream.range(1, fileCount + 1)
                .boxed()
                .toList();
        if (availableIndexes.size() == 0) {
            return;
        }

        int chosenIndex = promptUntilValid(
                "Escolha uma imagem: ",
                availableIndexes);

    }

    private void printImageMenu() {
        System.out.println("""
                Opções:
                1 - Carregar um arquivo .pgm
                2 - Carregar um arquivo .ppm
                3 - Manejar uma imagem carregada
                4 - Sair""");
    }

    private int promptUntilValid(String prompt, List<Integer> valids) {
        int chosenOption;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            try {
                chosenOption = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Por favor insira um número.");
                scanner.next();
                continue;
            }

            if (!valids.contains(chosenOption)) {
                System.out.println("Opção inválida!");
            } else {
                break;
            }
        }

        return chosenOption;
    }
}
