import com.sun.jdi.InvalidTypeException;
import pdi.Format;
import pdi.PGM;
import pdi.PPM;
import pdi.io.PGM.PGMReader;
import pdi.io.PPM.PPMReader;
import utils.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private final List<Status> statuses = new ArrayList<>();

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
    3 - Manejar uma imagem carregada
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
                printLoadedImages();
                choseLoadedImages();
            }
            case 4 -> System.exit(0);
            default -> System.out.println("Opção inválida!");
        }
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
        if (availableFiles == null) return;
        File chosenFile = choseOneFile(availableFiles);
        PGM chosenFileImage = PGMReader.readPGM(chosenFile.getPath());
        this.statuses.add(new Status(Format.PGM, chosenFile, chosenFileImage));
    }

    private void loadPPM() throws IOException {
        File[] availableFiles = listAllFiles(".ppm");
        if (availableFiles == null) return;
        File chosenFile = choseOneFile(availableFiles);
        PPM chosenFileImage = PPMReader.readPPM(chosenFile.getPath());
        this.statuses.add(new Status(Format.PPM, chosenFile, chosenFileImage));
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
        List<Integer> availableIndexes = this.statuses.stream().mapToInt(Status::id).boxed().toList();
        if (availableIndexes.size() == 0) {
            return;
        }

        int chosenIndex = promptUntilValid(
                "Escolha uma imagem: ",
                availableIndexes);
        // TODO
        // A partir do arquivo carregado selecionado
        // Aplicar qualquer manipulação possível
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
