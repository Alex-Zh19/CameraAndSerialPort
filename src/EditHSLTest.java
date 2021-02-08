import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.nio.BufferUnderflowException;

import static org.junit.Assert.*;

public class EditHSLTest {

    @Test
    public void apply_null_image() {
        EditHSL filter=new EditHSL();

        BufferedImage image2=new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        filter.SetBrightnessFactor(1.5f);
        filter.SetSaturationFactor(1.5f);
        filter.SetContrastFactor(1.5f);

        BufferedImage dst2Image=filter.Apply(image2);
        Assert.assertTrue(imageEqual(image2,dst2Image));

    }
    @Test
    public void apply_not_null_image() {
        EditHSL filter=new EditHSL();

        BufferedImage image=CreateNotNullImage();

        filter.SetBrightnessFactor(1.5f);
        filter.SetSaturationFactor(1.5f);
        filter.SetContrastFactor(1.5f);


        BufferedImage dst1Image=filter.Apply(image);

        Assert.assertFalse(imageEqual(image,dst1Image));
    }

    boolean imageEqual(BufferedImage im1,BufferedImage im2){
        for(int i=0;i<im1.getWidth();++i){
            for(int j=0;j<im1.getHeight();++j){
                int im1Num=im1.getRGB(i,j);
                int im2Num=im2.getRGB(i,j);
                if(im1Num!=im2Num){
                    return false;
                }
            }
        }
        return true;
    }

    BufferedImage CreateNotNullImage(){
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        double []rgb={1,2,3};
        for (int i=0;i< image.getWidth();i++){
            for(int j=0;j< image.getHeight();j++){
               if(i==j){
                image.setRGB(i,j,rgb2Hsl(rgb));
               }
            }
        }

        return image;
    }
    private int rgb2Hsl(double[] rgb){
        double max = Math.max(Math.max(rgb[0], rgb[1]), rgb[2]); // 0xdd = 221
        double delta = max - Math.min(Math.min(rgb[0], rgb[1]), rgb[2]); // 153
        double h = 0;
        int s = 0;
        int l = (int) Math.round(max * 100d / 255d); // 87 ok
        if (max != 0) {
            s = (int) Math.round(delta * 100d / max); // 69 ok
            if (max == rgb[0]) {
                h = (rgb[1] - rgb[2]) / delta;
            } else if (max == rgb[1]) {
                h = (rgb[2] - rgb[0]) / delta + 2d;
            } else {
                h = (rgb[0] - rgb[1]) / delta + 4d; // 4.8888888888
            }
            h = Math.min(Math.round(h * 60d), 360d); // 293
            if (h < 0d) {
                h += 360d;
            }
        }
        return (int)Math.round(h+s+l);
    }
}