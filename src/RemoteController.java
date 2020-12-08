import jssc.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RemoteController {
     private SerialPort serialPort;
     IRemoteControllerListener listener;
    private final String isWhiteTop="160";//1
    private final String isWhiteOblique1="161";//2
    private final String isWhiteOblique2="162";//3
    private final String isWhiteOblique3="163";//4
    private final String isWhiteOblique4="164";//5
    private final String isUF365="165";//6
    private final String isAntiStocks980="166";//7
    private final String isIRTop830="167";//8
    private final String isIRTop950="168";//9
    private final String isIROblique830_1="169";//10
    private final String isIROblique830_2="170";//11
    private final String isIROblique830_3="171";//12
    private final String isIROblique830_4="172";//13
    private final String isIRLum505="173";//14
    private final String isM_Mark="174";//15
    private final String isIRCircular830="175";//16
    private final String isWhiteCircular="176";//17
    private final String isZoom="192";//18
    private final String isStop="193";//19
    private final String isSleep="194";//20

   Queue<Byte> infoSaver=new LinkedList<>();


    public static String[] GetSerialPorts(){
        String[]comsList=SerialPortList.getPortNames();
        return comsList;
    }


   public void SetDeviceItemListener(IRemoteControllerListener listen){
        listener=listen;
   }

    public boolean Open(String comId){
      serialPort=new SerialPort(comId);
      try {
          serialPort.openPort();
          SetOptions();
          serialPort.addEventListener(new EventListener());
          return true;
      }catch (SerialPortException exception){
          System.out.println(exception);
          return false;
      }
    }

    public void Close(){
        if(serialPort.isOpened()){
            try {
            serialPort.closePort();
            }catch (SerialPortException ex){
                System.out.println(ex);
            }

        }
    }

    private void SetOptions(){
        try {
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
                                  SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
        }catch (SerialPortException e){
            System.out.println(e);
        }
    }

    private RemoteControllerEvent CreateDeviceEvent(String isMode,int range,int value){
        RemoteControllerEvent event=new RemoteControllerEvent();
        switch (isMode){
            case isWhiteTop:{
                event.SetBut1(true);
                event.SetVal(value);
                break;
            }
            case isWhiteOblique1:{
                event.SetBut2(true);
                event.SetVal(value);
                break;
            }
            case isWhiteOblique2:{
                event.SetBut3(true);
                event.SetVal(value);
                break;
            }
            case isWhiteOblique3:{
                event.SetBut4(true);
                event.SetVal(value);
                break;
            }
            case isWhiteOblique4:{
                event.SetBut5(true);
                event.SetVal(value);
                break;
            }
            case isUF365:{
                event.SetBut6(true);
                event.SetVal(value);
                break;
            }
            case isAntiStocks980:{//7
                event.SetBut7(true);
                event.SetVal(value);
                break;
            }
            case isIRTop830:{//8
                event.SetBut8(true);
                event.SetVal(value);
                break;
            }
            case isIRTop950:{//9
                event.SetBut9(true);
                event.SetVal(value);
                break;
            }
            case isIROblique830_1:{//10
                event.SetBut10(true);
                event.SetVal(value);
                break;
            }
            case isIROblique830_2:{//11
                event.SetBut11(true);
                event.SetVal(value);
                break;
            }
            case isIROblique830_3:{//12
                event.SetBut12(true);
                event.SetVal(value);
                break;
            }
            case isIROblique830_4:{//13
                event.SetBut13(true);
                event.SetVal(value);
                break;
            }
            case isIRLum505:{//14
                event.SetBut14(true);
                event.SetVal(value);
                break;
            }
            case isM_Mark:{//15
                event.SetBut15(true);
                event.SetVal(value);
                break;
            }
            case isIRCircular830:{//16
                event.SetBut16(true);
                event.SetVal(value);
                event.SetRange(range);
                break;
            }
            case isWhiteCircular:{//17
                event.SetBut17(true);
                event.SetVal(value);
                event.SetRange(range);
                break;
            }
            case isZoom:{//18
                event.SetBut18(true);
                event.SetVal(value);
                break;
            }
            case isStop:{//19
                event.SetBut19(true);
                break;
            }
            case isSleep:{//20
                event.SetBut20(true);
                break;
            }
        }
        return event;
    }

     class EventListener implements SerialPortEventListener{
         @Override
         public void serialEvent(SerialPortEvent PortEvent) {
             if(PortEvent.isRXCHAR()&&PortEvent.getEventValue()>0){
                 try {
                     byte[]data=serialPort.readBytes();
                     byte[]packData=new byte[3];
                     int h=0;
                     for(byte i:data){
                         infoSaver.offer(i);
                     }
                     while(infoSaver.size()>=3){
                         packData[h]=infoSaver.poll();
                         h++;
                         if(h==3){
                             CreateDeviceEvent(packData[0],packData[1],packData[2]);
                             h=0;
                         }
                     }
                 }catch (SerialPortException e){
                     System.out.println(e);
                 }
             }
         }
     }
}
