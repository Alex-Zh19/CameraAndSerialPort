public class RemoteControllerEvent {
    private boolean isWhiteTop=false;//1
    private boolean isWhiteOblique1=false;//2
    private boolean isWhiteOblique2=false;//3
    private boolean isWhiteOblique3=false;//4
    private boolean isWhiteOblique4=false;//5
    private boolean isUF365=false;//6
    private boolean isAntiStocks980=false;//7
    private boolean isIRTop830=false;//8
    private boolean isIRTop950=false;//9
    private boolean isIROblique830_1=false;//10
    private boolean isIROblique830_2=false;//11
    private boolean isIROblique830_3=false;//12
    private boolean isIROblique830_4=false;//13
    private boolean isIRLum505=false;//14
    private boolean isM_Mark=false;//15
    private boolean isIRCircular830=false;//16
    private boolean isWhiteCircular=false;//17
    private boolean isZoom=false;//18
    private boolean isStop=false;//19
    private boolean isSleep=false;//20
    private int value;
    private int range;



    public void SetBut1(boolean buttonPressed){
       isWhiteTop=buttonPressed;
    }//1
    public void SetBut2(boolean buttonPressed){
        isWhiteOblique1=buttonPressed;
    }//2
    public void SetBut3(boolean buttonPressed){
        isWhiteOblique2=buttonPressed;
    }//3
    public void SetBut4(boolean buttonPressed){
        isWhiteOblique3=buttonPressed;
    }//4
    public void SetBut5(boolean buttonPressed){
        isWhiteOblique4=buttonPressed;
    }//5
    public void SetBut6(boolean buttonPressed){
        isUF365=buttonPressed;
    }//6
    public void SetBut7(boolean buttonPressed){
        isAntiStocks980=buttonPressed;
    }//7
    public void SetBut8(boolean buttonPressed){
        isIRTop830=buttonPressed;
    }//8
    public void SetBut9(boolean buttonPressed){
        isIRTop950=buttonPressed;
    }//9
    public void SetBut10(boolean buttonPressed){
        isIROblique830_1=buttonPressed;
    }//10
    public void SetBut11(boolean buttonPressed){
        isIROblique830_2=buttonPressed;
    }//11
    public void SetBut12(boolean buttonPressed){
        isIROblique830_3=buttonPressed;
    }//12
    public void SetBut13(boolean buttonPressed){
        isIROblique830_4=buttonPressed;
    }//13
    public void SetBut14(boolean buttonPressed){
        isIRLum505=buttonPressed;
    }//14
    public void SetBut15(boolean buttonPressed){
        isM_Mark=buttonPressed;
    }//15
    public void SetBut16(boolean buttonPressed){
        isIRCircular830=buttonPressed;
    }//16
    public void SetBut17(boolean buttonPressed){
        isWhiteCircular=buttonPressed;
    }//17
    public void SetBut18(boolean buttonPressed){
        isZoom=buttonPressed;
    }//18
    public void SetBut19(boolean buttonPressed){
        isStop=buttonPressed;
    }//19
    public void SetBut20(boolean buttonPressed){
        isSleep=buttonPressed;
    }//20

    public void SetVal(int Val){value=Val;}

    public void SetRange(int range){this.range=range;}//for 16,17

    public boolean isButton1Pressed(){
       return isWhiteTop;
    }//1
    public boolean isButton2Pressed(){
       return isWhiteOblique1;
    }//2
    public boolean isButton3Pressed(){
        return isWhiteOblique2;
    }//3
    public boolean isButton4Pressed(){
        return isWhiteOblique3;
    }//4
    public boolean isButton5Pressed(){
        return isWhiteOblique4;
    }//5
    public boolean isButton6Pressed(){
        return isUF365;
    }//6
    public boolean isButton7Pressed(){
        return isAntiStocks980;
    }//7
    public boolean isButton8Pressed(){
        return isIRTop830;
    }//8
    public boolean isButton9Pressed(){
        return isIRTop950;
    }//9
    public boolean isButton10Pressed(){
        return isIROblique830_1;
    }//10
    public boolean isButton11Pressed(){
        return isIROblique830_2;
    }//11
    public boolean isButton12Pressed(){
        return isIROblique830_3;
    }//12
    public boolean isButton13Pressed(){
        return isIROblique830_4;
    }//13
    public boolean isButton14Pressed(){
        return isIRLum505;
    }//14
    public boolean isButton15Pressed(){
        return isM_Mark;
    }//15
    public boolean isButton16Pressed(){
        return isIRCircular830;
    }//16
    public boolean isButton17Pressed(){
        return isWhiteCircular;
    }//17
    public boolean isButton18Pressed(){
        return isZoom;
    }//18
    public boolean isButton19Pressed(){
        return isStop;
    }//19
    public boolean isButton20Pressed(){
        return isSleep;
    }//20


    public int GetVal(){return value;}

    public int GetRange(){return range;}//for 16,17
}
