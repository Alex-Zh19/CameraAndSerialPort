import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class ImageProcessorTest {

    @Test
    public void addFilter() {

    }

    @Test
    public void applyEditors_no_null() {
        ImageProcessor processor=new ImageProcessor();
        BufferedImage expected=processor.ApplyFilters();
    }
}