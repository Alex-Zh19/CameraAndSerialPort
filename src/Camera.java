import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.util.ArrayList;

import static org.opencv.videoio.Videoio.*;


public class Camera implements ICamera{
   private  final int DefaultWidth=960;
    private  final int DefaultHeight=576;

    private Mat tmpFrame_=new Mat();

    private BufferedImage VideoIm=null;


    private int CameraID;

    private VideoCapture camera=null;



    Camera(){
        CameraID=1;
    }
    Camera(int CamID){
        CameraID=CamID;
    }


    @Override
    public void Open() {
        camera=new VideoCapture(CameraID);
        camera.set(CAP_PROP_AUTO_EXPOSURE, 0);
       // camera.set(CAP_PROP_FPS,fps);
    }

    @Override
    public BufferedImage Read() {
        BufferedImage VideoImage=null;
       if(camera.isOpened()) {
           camera.read(tmpFrame_);
           try{
           VideoImage = Mat2BufferedImage(tmpFrame_);}catch (IOException ex){
               System.out.println("Camera read error");
           }
       }
       return VideoImage;
    }

    @Override
    public void Grab() {
       VideoIm=Read();
    }


    @Override
    public BufferedImage Get() {
        return VideoIm;
    }

    @Override
    public void Set(BufferedImage bi) {
        VideoIm=bi;
    }

    @Override
    public int GetXResolution() {
        int Width=(int)Math.round(camera.get(CAP_PROP_FRAME_WIDTH));
        return Width;
    }

    @Override
    public int GetYResolution() {
        int Height=(int)Math.round(camera.get(CAP_PROP_FRAME_HEIGHT));
        return Height;
    }

    @Override
    public void SetResolution(int Width,int Height) {
        if(Width==0&&Height==0){
            Width=DefaultWidth;
            Height=DefaultHeight;
        }
       camera.set(CAP_PROP_FRAME_WIDTH,Width);
        camera.set(CAP_PROP_FRAME_HEIGHT,Height);
    }


    @Override
    public void Close() {
        camera.release();
        tmpFrame_.release();
    }



    public static ArrayList<Integer> CountUSB_And_AnalyzedCameraID() {
        ArrayList<Integer>listOfCameras=new ArrayList<>();
        int MaxCount = 10;
        for (int i = 0; i < MaxCount; ++i) {
            VideoCapture buffCam = new VideoCapture(i);
            if (buffCam.isOpened()) {
                listOfCameras.add(i);
                buffCam.release();
            }
        }
        return listOfCameras;
    }


    static BufferedImage Mat2BufferedImage(Mat matrix)throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        byte ba[]=mob.toArray();
        BufferedImage bi= ImageIO.read(new ByteArrayInputStream(ba));
        return bi;
    }
   // @Override
public void SetFPS(double value){
        camera.set(CAP_PROP_FPS,value);
}

   // @Override
    public double GetFPS() {
        return camera.get(CAP_PROP_FPS);
    }
}

