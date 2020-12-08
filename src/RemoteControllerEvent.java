import java.util.Random;

enum Mode {
    WhiteTop, //1
    WhiteOblique1,
    WhiteOblique2,
    WhiteOblique3,
    WhiteOblique4,//5
    UF365,
    AntiStocks980,
    IRTop830,
    IRTop950,
    IROblique830_1,//10
    IROblique830_2,
    IROblique830_3,
    IROblique830_4,
    IRLum505,
    M_Mark,//15
    IRCircular830,
    WhiteCircular,
    Zoom,
    Stop,
    Sleep,//20
}
enum Range{
    NONE,
    RANGE_16,
    RANGE_17
}
public class RemoteControllerEvent {
    Mode mode;
    private int brightness;
    Range range;




    public void SetMode(boolean buttonPressed){
       isWhiteTop=buttonPressed;
    }//1

    public void SetBrightness(int Val){brightness=Val;}

    public void SetRange(int range){this.range=range;}//for 16,17

    public boolean GetMode(){
       return isWhiteTop;
    }

    public int GetBrightness(){return brightness;}

    public int GetRange(){return range;}//for 16,17
}
