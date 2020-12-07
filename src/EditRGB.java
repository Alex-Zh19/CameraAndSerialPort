import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class EditRGB implements IImageEditor {

    private float []RGBFactors={1.0f,1.0f,1.0f};
    private float[] RGBOffsets={0.0f,0.0f,0.0f};

    public void SetRColorFactors(float R) {
        RGBFactors[0]=R;

    }
    public void SetGColorFactors(float G) {
        RGBFactors[1]=G;

    }
    public void SetBColorFactors(float B) {
        RGBFactors[2]=B;

    }
    public void SetRoColorOffsets(float Ro) {
        RGBOffsets[0]=Ro;

    }
    public void SetGoColorOffsets(float Go) {
        RGBOffsets[1]=Go;

    }
    public void SetBoColorOffsets(float Bo) {
        RGBOffsets[2]=Bo;

    }
    private BufferedImage ChangeColor(BufferedImage bi) {
        BufferedImage DstImage=null;
        RescaleOp op = new RescaleOp(RGBFactors, RGBOffsets, null);
        DstImage = op.filter(bi, null);
        return DstImage;
    }

    @Override
    public BufferedImage Apply(BufferedImage orig) {
        orig=ChangeColor(orig);
        return orig;
    }
}
