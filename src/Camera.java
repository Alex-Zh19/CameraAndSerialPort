import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;


public class Camera implements ICamera{
   private  final int Width=960;
    private  final int Height=576;

    private Mat tmpFrame_=new Mat();

    private BufferedImage VideoIm=null;

    private int CameraID;

    private VideoCapture camera;

    int[]AllCamID;

    private int SaveCamID=0;

    private boolean ResolutionMark=false;

    Camera(){
        CameraID=0;
    }
    Camera(int CamID){
        CameraID=CamID;
    }


    @Override
    public void Open() {
        CountUSB_And_AnalyzedCameraID();
        camera=new VideoCapture(CameraID);
        SetResolution(Width,Height);
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
            Width=960;
            Height=576;
        }
        camera.set(CAP_PROP_FRAME_WIDTH,Width);
        camera.set(CAP_PROP_FRAME_HEIGHT,Height);
    }

    @Override
    public void Close() {
        camera.release();
        tmpFrame_.release();
    }

Runnable CountCam=new Runnable() {
    @Override
    public void run() {
        int MaxCount=10;
        int [] buff=new int[MaxCount];
        int h=0;
        for(int i=0;i<MaxCount;++i){
            VideoCapture buffCam=new VideoCapture(i);
            if(buffCam.isOpened()){
                buff[h]=i;
                ++h;
                buffCam.release();
            }
        }
        AllCamID=new int[h-1];
        for(int i=0;i<h-1;++i){
            AllCamID[i]=buff[i];
        }
    }
};


    public void CountUSB_And_AnalyzedCameraID(){
        ExecutorService sched= Executors.newCachedThreadPool();
        sched.submit(CountCam);
    }


    static BufferedImage Mat2BufferedImage(Mat matrix)throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        byte ba[]=mob.toArray();
        BufferedImage bi= ImageIO.read(new ByteArrayInputStream(ba));
        return bi;
    }


}

