enum Mode {
    isWhiteTop, //1
    isWhiteOblique1,
    isWhiteOblique2,
    isWhiteOblique3,
    isWhiteOblique4,//5
    isUF365,
    isAntiStocks980,
    isIRTop830,
    isIRTop950,
    isIROblique830_1,//10
    isIROblique830_2,
    isIROblique830_3,
    isIROblique830_4,
    isIRLum505,
    isM_Mark,//15
    isIRCircular830,
    isWhiteCircular,
    isZoom,
    isStop,
    isSleep,//20

}
enum Range{
    NONE,
    RANGE_16,
    RANGE_17
}
public class RemoteControllerEvent {
    Mode mode;
    private int brightness;





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
