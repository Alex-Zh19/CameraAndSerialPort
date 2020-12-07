import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CameraRecorder {
    ICameraListener listener;
    private ICamera superCamera;
    private ScheduledExecutorService schedule=null;
    private final int  defaultFPS=50;
    private int FPS=defaultFPS;
    private boolean isOnPause=false;
    private boolean isOpen=false;
    private BufferedImage openIm=null;


    Runnable MainCycle=new Runnable() {
        @Override
        public void run() {
            if(!isOnPause){
            listener.AcceptImage(superCamera.Read());
            }
            else{
               listener.AcceptImage(superCamera.Get());
            }
        }
    };

    Runnable OpenCycle=new Runnable() {
        @Override
        public void run() {
            listener.AcceptImage(openIm);
        }
    };
    public  void SetCamera(ICamera camera){
        superCamera=camera;
    }

    public void setCameraListener(ICameraListener li){
      listener=li;
    }

    public void SetIsOnPause(boolean mark){
        superCamera.Grab();
        isOnPause=mark;
    }

    public boolean GetIsOnPause(){
        return isOnPause;
    }

    public void OpenCam(){
        superCamera.Open();
    }

    public void Start() {
        schedule= Executors.newSingleThreadScheduledExecutor();
        if(!isOpen){
        schedule.scheduleAtFixedRate(MainCycle,0,FPS, TimeUnit.MILLISECONDS);}
        else{
            schedule.scheduleAtFixedRate(OpenCycle,0,50, TimeUnit.MILLISECONDS);
            isOpen=false;
        }
    }

    public void Stop() {
      schedule.shutdown();
    }

    public void StopAndClose(){
      if(schedule!=null&&superCamera!=null){
        schedule.shutdown();
        superCamera.Close();
      }

    }

    public void SetFPS(int fps){
        FPS=fps;
    }

    public  int GetFPS(){
        return FPS;
    }

    public void Set(BufferedImage bi){
        isOpen=true;
        openIm=bi;
    }
}
