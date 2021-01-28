import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

class TestFilter implements IImageEditor{
    private int number;

    TestFilter(int num){
        number=num;
    }

    private BufferedImage AddNumberToPixels(BufferedImage orig){
        BufferedImage dstImage=orig;
        for(int i=0;i<orig.getWidth();++i){
            for(int j=0;i<orig.getHeight();++j){
                int orignum=orig.getRGB(i,j);
                dstImage.setRGB(i,j,orignum+number);
            }
        }
        return dstImage;
    }

    @Override
    public BufferedImage Apply(BufferedImage orig) {
        orig=AddNumberToPixels(orig);
        return orig;
    }
}


public class ImageProcessorTest {
    TestFilter filter=new TestFilter(2);
    BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
    @Test
    public void addFilter() {

    }

    @Test
    public void applyFilters() {
        BufferedImage expected=filter.Apply(image);
        Assert.assertTrue(imageEqual(expected,image));
    }

    @Test
    public void applyEditors_no_null() {
        BufferedImage expected=filter.Apply(image);
        Assert.assertNotNull(expected);
    }

    boolean imageEqual(BufferedImage im1,BufferedImage im2){
        for(int i=0;i<im1.getWidth();++i){
            for(int j=0;i<im1.getHeight();++j){
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