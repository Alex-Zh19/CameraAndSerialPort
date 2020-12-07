public class RemoteControllerEvent {
    private boolean isButton1Pressed=false;
    private boolean isButton2Pressed=false;
    private boolean isButton3Pressed=false;
    private boolean isButton4Pressed=false;
    private boolean isButton5Pressed=false;
    private boolean isButton6Pressed=false;
    private boolean isButton7Pressed=false;



    public void SetBut1(boolean buttonPressed){
       isButton1Pressed=buttonPressed;
    }
    public void SetBut2(boolean buttonPressed){
        isButton2Pressed=buttonPressed;
    }
    public void SetBut3(boolean buttonPressed){
        isButton3Pressed=buttonPressed;
    }
    public void SetBut4(boolean buttonPressed){
        isButton4Pressed=buttonPressed;
    }
    public void SetBut5(boolean buttonPressed){
        isButton5Pressed=buttonPressed;
    }
    public void SetBut6(boolean buttonPressed){
        isButton6Pressed=buttonPressed;
    }
    public void SetBut7(boolean buttonPressed){
        isButton7Pressed=buttonPressed;
    }


    public boolean isButton1Pressed(){
       return isButton1Pressed;
    }
    public boolean isButton2Pressed(){
       return isButton2Pressed;
    }
    public boolean isButton3Pressed(){
        return isButton3Pressed;
    }
    public boolean isButton4Pressed(){
        return isButton4Pressed;
    }
    public boolean isButton5Pressed(){
        return isButton5Pressed;
    }
    public boolean isButton6Pressed(){
        return isButton6Pressed;
    }
    public boolean isButton7Pressed(){
        return isButton7Pressed;
    }

}
