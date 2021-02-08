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
        for (int i=0;i< image.getWidth();i++){
            for(int j=0;j< image.getHeight();j++){
               if(i==j){
                image.setRGB(i,j,1);
               }
            }
        }

        return image;
    }
}