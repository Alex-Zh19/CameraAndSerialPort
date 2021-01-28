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


    @Test
    public void addFilter() {

    }

    @Test
    public void applyEditors_no_null() {
        ImageProcessor processor=new ImageProcessor();
        BufferedImage expected=processor.ApplyFilters();
    }
}