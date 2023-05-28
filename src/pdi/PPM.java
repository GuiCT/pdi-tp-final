package pdi;

import pdi.operations.areaWise.Mask;
import pdi.operations.elementWise.ElementWiseOperation;

public class PPM {
    private final Channel redChannel;
    private final Channel greenChannel;
    private final Channel blueChannel;

    public PPM(Channel redChannel, Channel greenChannel, Channel blueChannel) {
        this.redChannel = redChannel;
        this.greenChannel = greenChannel;
        this.blueChannel = blueChannel;
    }

    public int getWidth() {
        return this.redChannel.width;
    }

    public int getHeight() {
        return this.redChannel.height;
    }

    public int getMaxValue() {
        return this.redChannel.maxValue;
    }

    public int getRed(int i, int j) {
        return this.redChannel.get(i, j);
    }

    public int getGreen(int i, int j) {
        return this.greenChannel.get(i, j);
    }

    public int getBlue(int i, int j) {
        return this.blueChannel.get(i, j);
    }

    public void setRed(int i, int j, int value) {
        this.redChannel.set(i, j, value);
    }

    public void setGreen(int i, int j, int value) {
        this.greenChannel.set(i, j, value);
    }

    public void setBlue(int i, int j, int value) {
        this.blueChannel.set(i, j, value);
    }

    public PPM copyPPM() {
        Channel newRedChannel = this.redChannel.copyChannel();
        Channel newGreenChannel = this.greenChannel.copyChannel();
        Channel newBlueChannel = this.blueChannel.copyChannel();
        return new PPM(newRedChannel, newGreenChannel, newBlueChannel);
    }

    public PPM elementWiseOperation(ElementWiseOperation operation) {
        Channel newRedChannel = this.redChannel.elementWiseOperation(operation);
        Channel newGreenChannel = this.greenChannel.elementWiseOperation(operation);
        Channel newBlueChannel = this.blueChannel.elementWiseOperation(operation);

        return new PPM(newRedChannel, newGreenChannel, newBlueChannel);
    }

    public PPM elementWiseOperation(
            ElementWiseOperation redOp,
            ElementWiseOperation greenOp,
            ElementWiseOperation blueOp
    ) {
        Channel newRedChannel = this.redChannel.elementWiseOperation(redOp);
        Channel newGreenChannel = this.greenChannel.elementWiseOperation(greenOp);
        Channel newBlueChannel = this.blueChannel.elementWiseOperation(blueOp);

        return new PPM(newRedChannel, newGreenChannel, newBlueChannel);
    }

    public PPM maskOperation(Mask operation) {
        Channel newRedChannel = this.redChannel.maskOperation(operation);
        Channel newGreenChannel = this.greenChannel.maskOperation(operation);
        Channel newBlueChannel = this.blueChannel.maskOperation(operation);

        return new PPM(newRedChannel, newGreenChannel, newBlueChannel);
    }

    public PPM maskOperation(
            Mask greenOp,
            Mask blueOp,
            Mask redOp
    ) {
        Channel newRedChannel = this.redChannel.maskOperation(redOp);
        Channel newGreenChannel = this.greenChannel.maskOperation(greenOp);
        Channel newBlueChannel = this.blueChannel.maskOperation(blueOp);

        return new PPM(newRedChannel, newGreenChannel, newBlueChannel);
    }

    public PPM plus(PPM addend) {
        return new PPM(
                this.redChannel.plus(addend.redChannel),
                this.greenChannel.plus(addend.greenChannel),
                this.blueChannel.plus(addend.blueChannel)
        );
    }

    public PPM minus(PPM subtrahend) {
        return new PPM(
                this.redChannel.minus(subtrahend.redChannel),
                this.greenChannel.minus(subtrahend.greenChannel),
                this.blueChannel.minus(subtrahend.blueChannel)
        );
    }
}
