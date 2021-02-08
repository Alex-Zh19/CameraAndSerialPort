import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class EditHSLTest {

    @Test
    public void apply() {
        EditHSL filter=new EditHSL();
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        filter.SetBrightnessFactor(1.5f);
        filter.SetSaturationFactor(1.5f);
        filter.SetContrastFactor(1.5f);
        
        BufferedImage dstImage=filter.Apply(image);

        Assert.assertFalse(imageEqual(image,dstImage));
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
}