import jssc.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RemoteController {

    private SerialPort serialPort;
     IRemoteControllerListener listener;
    private final byte isWhiteTop=(byte)0xA0;//1
    private final byte isWhiteOblique1=(byte)0xA1;//2
    private final byte isWhiteOblique2=(byte)0xA2;//3
    private final byte isWhiteOblique3=(byte)0xA3;//4
    private final byte isWhiteOblique4=(byte)0xA4;//5
    private final byte isUF365=(byte)0xA5;//6
    private final byte isAntiStocks980=(byte)0xA6;//7
    private final byte isIRTop830=(byte)0xA7;//8
    private final byte isIRTop950=(byte)0xA8;//9
    private final byte isIROblique830_1=(byte)0xA9;//10
    private final byte isIROblique830_2=(byte)0xAA;//11
    private final byte isIROblique830_3=(byte)0xAB;//12
    private final byte isIROblique830_4=(byte)0xAC;//13
    private final byte isIRLum505=(byte)0xAD;//14
    private final byte isM_Mark=(byte)0xAE;//15
    private final byte isIRCircular830=(byte)0xAF;//16
    private final byte isWhiteCircular=(byte)0xB0;//17
    private final byte isZoom=(byte)0xC0;//18
    private final byte isStop=(byte)0xC1;//19
    private final byte isSleep=(byte)0xC2;//20

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

    private RemoteControllerEvent CreateDeviceEvent(byte isMode,byte rang,byte val){
        RemoteControllerEvent event=new RemoteControllerEvent();
        int value=val;
        int range=rang;
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
                             //System.out.println(packData[0]+" "+packData[1]+" "+packData[2]);
                             listener.DeviceEvent(CreateDeviceEvent(packData[0],packData[1],packData[2]));
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
