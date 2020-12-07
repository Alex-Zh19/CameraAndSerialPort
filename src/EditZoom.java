import java.awt.*;
import java.awt.image.BufferedImage;

public class EditZoom implements IImageEditor{
    private double zoomFactor=1.0;
    private int DEFAULTMX=0;
    private int DEFAULTMY=0;
    private double mX=DEFAULTMX;
    private double mY=DEFAULTMY;


    private BufferedImage Zooming(BufferedImage bi) {
        BufferedImage img = bi;
        int startx=StartX(bi);
        int starty=StartY(bi);
        int newWidth=(int)Math.round(img.getWidth()/zoomFactor);
        int newHeight=(int)Math.round(img.getHeight()/zoomFactor);
        BufferedImage subim=img.getSubimage(startx,starty,newWidth,newHeight);
        BufferedImage dstImage = new BufferedImage(img.getWidth(), img.getHeight() , BufferedImage.TYPE_INT_RGB);
        dstImage.getGraphics().drawImage(subim.getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_SMOOTH),
                                                                                        0, 0, null);
        return dstImage;
    }


    public void SetMX(double x){
        mX=x;
    }

    public void SetMY(double y){
        mY=y;
    }

    private int StartX(BufferedImage bi){
        double xr=Math.min(bi.getWidth()-1,mX+(bi.getWidth()/(zoomFactor*2)));
        double xl_temp=xr-(bi.getWidth()/(zoomFactor))+1;
        double start=Math.max(0,xl_temp);
        int startx=(int)Math.round(start);
        return startx;
    }

    private int StartY(BufferedImage bi){
        double yt=Math.min(bi.getHeight()-1,mY+(bi.getHeight()/(zoomFactor*2)));
        double yb_temp=yt-(bi.getHeight()/(zoomFactor))+1;
        double start=Math.max(0,yb_temp);
        int starty=(int)Math.round(start);
        return starty;
    }


    public double GetZoomFactor(){
        return zoomFactor;
    }
    public void SetZoomFactor(double factor){
        zoomFactor=factor;
    }
    @Override
    public BufferedImage Apply(BufferedImage orig) {
        orig=Zooming(orig);
        return orig;
    }
}
