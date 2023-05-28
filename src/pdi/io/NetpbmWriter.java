package pdi.io;

import pdi.PGM;
import pdi.PPM;

import java.io.*;

public class NetpbmWriter {
    private final Object savedImage;
    private final String format;
    private final String filePath;
    private final int columns;
    private final int lines;
    private final int maxVal;
    private File file;
    private boolean binary;
    private FileWriter fileWriter;

    public NetpbmWriter(Object savedImage, String filePath) throws InvalidObjectException {
        if (savedImage instanceof PGM pgm) {
            this.savedImage = savedImage;
            this.format = "PGM";
            this.columns = pgm.getWidth();
            this.lines = pgm.getHeight();
            this.maxVal = pgm.getMaxValue();
        } else if (savedImage instanceof PPM ppm) {
            this.savedImage = savedImage;
            this.format = "PPM";
            this.columns = ppm.getWidth();
            this.lines = ppm.getHeight();
            this.maxVal = ppm.getMaxValue();
        } else {
            throw new InvalidObjectException("Imagem não é suportada");
        }

        int bpp = (int) Math.ceil(Math.log(this.maxVal) / Math.log(2));

        if (bpp != 8) {
            throw new UnsupportedOperationException("BPP não suportado");
        }

        this.filePath = filePath;
    }

    public void saveFile(boolean binary) throws IOException {
        this.binary = binary;

        switch (this.format) {
            case "PGM" -> {
                if (this.binary) {
                    savePGMBinary();
                } else {
                    savePGMASCII();
                }
            }
            case "PPM" -> {
                if (this.binary) {
                    savePPMBinary();
                } else {
                    savePPMASCII();
                }
            }
            default -> throw new InvalidObjectException("Formato não suportado");
        }
    }

    private int writeHeader() throws IOException {
        this.file = new File(this.filePath);
        this.fileWriter = new FileWriter(this.file);
        int headerSizeInBytes = 0;

        switch (this.format) {
            case "PGM" -> {
                if (this.binary) {
                    this.fileWriter.write("P5\n");
                } else {
                    this.fileWriter.write("P2\n");
                }
            }
            case "PPM" -> {
                if (this.binary) {
                    this.fileWriter.write("P6\n");
                } else {
                    this.fileWriter.write("P3\n");
                }
            }
            default -> throw new InvalidObjectException("Formato não suportado");
        }
        headerSizeInBytes += 3;

        String widthAndHeight = this.columns + " " + this.lines + "\n";
        this.fileWriter.write(widthAndHeight);
        headerSizeInBytes += widthAndHeight.length();

        String maxValue = this.maxVal + "\n";
        this.fileWriter.write(maxValue);
        headerSizeInBytes += maxValue.length();

        return headerSizeInBytes;
    }

    // ==================================================
    // PGM
    // ==================================================

    private void savePGMASCII() throws IOException {
        PGM pgm = (PGM) this.savedImage;
        int ignored = writeHeader();

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.fileWriter.write(pgm.get(i, j) + " ");
            }
            this.fileWriter.write("\n");
        }

        this.fileWriter.close();
    }

    private void savePGMBinary() throws IOException {
        PGM pgm = (PGM) this.savedImage;
        int ignored = writeHeader();
        this.fileWriter.close();

        // BufferedOutputStream no modo append
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.file, true));

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                bufferedOutputStream.write(pgm.get(i, j));
            }
        }

        bufferedOutputStream.close();
    }

    // ==================================================
    // PPM
    // ==================================================

    private void savePPMASCII() throws IOException {
        PPM ppm = (PPM) this.savedImage;
        int ignored = writeHeader();

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.fileWriter.write(ppm.getRed(i, j) + " ");
                this.fileWriter.write(ppm.getGreen(i, j) + " ");
                this.fileWriter.write(ppm.getBlue(i, j) + " ");
            }
            this.fileWriter.write("\n");
        }

        this.fileWriter.close();
    }

    private void savePPMBinary() throws IOException {
        PPM ppm = (PPM) this.savedImage;
        int ignored = writeHeader();
        this.fileWriter.close();

        // BufferedOutputStream no modo append
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.file, true));

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                bufferedOutputStream.write(ppm.getRed(i, j));
                bufferedOutputStream.write(ppm.getGreen(i, j));
                bufferedOutputStream.write(ppm.getBlue(i, j));
            }
        }

        bufferedOutputStream.close();
    }
}
