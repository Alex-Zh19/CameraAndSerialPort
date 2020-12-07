import java.awt.image.BufferedImage;

public class EditHSL implements IImageEditor {
    private float [] HSLFactors={1.0f,1.0f,1.0f};//[0]-contrast,[1]-saturation,[2]-brightness

    public void SetContrastFactor(float contrast) {
        HSLFactors[0]=contrast;
    }
    public void SetSaturationFactor(float saturation){
        HSLFactors[1]=saturation;
    }
    public void SetBrightnessFactor( float brightness){
        HSLFactors[2]=brightness;
    }
    private BufferedImage ChangeHsl(BufferedImage bi) {
        BufferedImage DstImage=null;
        int width = bi.getWidth();
        int height = bi.getHeight();
        if(DstImage == null){
            DstImage = createCompatibleDestImage(bi);
        }
        int[] inpixels = new int[width*height];
        int[] outpixels = new int[width*height];
        getRGB(bi,0,0,width,height,inpixels);

        int index = 0;
        for(int row=0;row<height;row++){
            int ta = 0,tr = 0,tg = 0,tb = 0;
            for(int col=0;col<width;col++){
                index = row*width+col;
                ta = (inpixels[index] >> 24) & 0xff;
                tr = (inpixels[index] >> 16) & 0xff;
                tg = (inpixels[index] >> 8) & 0xff;
                tb = inpixels[index] & 0xff;
                // RGB color space is converted to HSL
                double[] hsl = rgb2Hsl(new double[]{tr,tg,tb});

                // adjust saturation
                hsl[1] = hsl[1]*HSLFactors[1];
                if(hsl[1]<0.0){
                    hsl[1] = 0.0;
                }
                if(hsl[1]>255.0){
                    hsl[1] = 255.0;
                }

                // adjust brightness
                hsl[2] = hsl[2]*HSLFactors[2];
                if(hsl[2]<0.0){
                    hsl[2] = 0.0;
                }
                if(hsl[2]>255.0){
                    hsl[2] = 255.0;
                }

                // HSL convert rgb space
                int[] rgb = hsl2RGB(hsl);
                tr = Check(rgb[0]);
                tg = Check(rgb[1]);
                tb = Check(rgb[2]);

                // adjust the contrast
                double cr = ((tr/255.0d)-0.5d)*HSLFactors[0];
                double cg = ((tg/255.0d)-0.5d)*HSLFactors[0];
                double cb = ((tb/255.0d)-0.5d)*HSLFactors[0];
                // output RGB values
                tr = (int)((cr+0.5f)*255.0f);
                tg = (int)((cg+0.5f)*255.0f);
                tb = (int)((cb+0.5f)*255.0f);

                outpixels[index] = (ta << 24) | (Check(tr) << 16 ) | (Check(tg) << 8) | Check(tb);
            }
        }
        setRGB(DstImage,0,0,width,height,outpixels);
        return DstImage;
    }







    private  int[] hsl2RGB(double []hsl)
    {

        double h = hsl[0] / 360d;
        double s = hsl[1] / 100d;
        double l = hsl[2] / 100d;
        double r = 0d;
        double g = 0d;
        double b;

        if (s > 0d) {
            if (h >= 1d) {
                h = 0d;
            }
            h = h * 6d;
            double f = h - Math.floor(h);
            double a = Math.round(l * 255d * (1d - s));
            b = Math.round(l * 255d * (1d - (s * f)));
            double c = Math.round(l * 255d * (1d - (s * (1d - f))));
            l = Math.round(l * 255d);

            switch ((int) Math.floor(h)) {
                case 0:
                    r = l;
                    g = c;
                    b = a;
                    break;
                case 1:
                    r = b;
                    g = l;
                    b = a;
                    break;
                case 2:
                    r = a;
                    g = l;
                    b = c;
                    break;
                case 3:
                    r = a;
                    g = b;
                    b = l;
                    break;
                case 4:
                    r = c;
                    g = a;
                    b = l;
                    break;
                case 5:
                    r = l;
                    g = a;
                    break;
            }
            return new int[] { (int) Math.round(r), (int) Math.round(g), (int) Math.round(b) };
        }

        l = Math.round(l * 255d);
        return new int[] { (int) l, (int) l, (int) l };

    }


    private double[] rgb2Hsl(double[] rgb){
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
        return new double[] { h, s, l };
    }


    private int Check(int value){
        if(value>255){
            return 255;
        }else if(value<0){
            return 0;
        }
        else{
            return value;
        }
    }


    private BufferedImage createCompatibleDestImage(BufferedImage bi){
        return new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
    }


    private int[] getRGB(BufferedImage image,int x, int y, int width, int height,int[] pixels){
        int type = image.getType();
        if(type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB){
            return (int[]) image.getRaster().getDataElements(x,y,width,height,pixels);
        }else{
            return image.getRGB(x,y,width,height,pixels,0,width);
        }
    }

    private void setRGB(BufferedImage image,int x, int y, int width, int height, int[] pixels){
        int type = image.getType();
        if(type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB){
            image.getRaster().setDataElements(x,y,width,height,pixels);
        }else {
            image.setRGB(x,y,width,height,pixels,0,width);
        }
    }



    @Override
    public BufferedImage Apply(BufferedImage orig) {
        orig=ChangeHsl(orig);
        return orig;
    }
}
