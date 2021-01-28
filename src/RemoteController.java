import jssc.*;

import java.util.LinkedList;
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
        event.SetBrightness(value);
        switch (isMode){
            case isWhiteTop:{
                event.SetMode(Mode.WhiteTop);
                break;
            }
            case isWhiteOblique1:{
                event.SetMode(Mode.WhiteOblique1);

                break;
            }
            case isWhiteOblique2:{
                event.SetMode(Mode.WhiteOblique2);

                break;
            }
            case isWhiteOblique3:{
                event.SetMode(Mode.WhiteOblique3);

                break;
            }
            case isWhiteOblique4:{
                event.SetMode(Mode.WhiteOblique4);

                break;
            }
            case isUF365:{
                event.SetMode(Mode.UF365);

                break;
            }
            case isAntiStocks980:{//7
                event.SetMode(Mode.AntiStocks980);

                break;
            }
            case isIRTop830:{//8
                 event.SetMode(Mode.IRTop830);

                break;
            }
            case isIRTop950:{//9
                event.SetMode(Mode.IRTop950);

                break;
            }
            case isIROblique830_1:{//10
                event.SetMode(Mode.IROblique830_1);

                break;
            }
            case isIROblique830_2:{//11
                event.SetMode(Mode.IROblique830_2);

                break;
            }
            case isIROblique830_3:{//12
                event.SetMode(Mode.IROblique830_3);

                break;
            }
            case isIROblique830_4:{//13
                event.SetMode(Mode.IROblique830_4);

                break;
            }
            case isIRLum505:{//14
                event.SetMode(Mode.IRLum505);

                break;
            }
            case isM_Mark:{//15
                event.SetMode(Mode.M_Mark);

                break;
            }
            case isIRCircular830:{//16
                event.SetMode(Mode.IRCircular830);
                event.SetRange(range);
                break;
            }
            case isWhiteCircular:{//17
                event.SetMode(Mode.WhiteCircular);
                event.SetRange(range);
                break;
            }
            case isZoom:{//18
                event.SetMode(Mode.Zoom);

                break;
            }
            case isStop:{//19
                event.SetMode(Mode.Stop);

                break;
            }
            case isSleep:{//20
                event.SetMode(Mode.Sleep);

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
                     int h=0;
                     for(byte i:data){
                         infoSaver.offer(i);
                     }
                     while(infoSaver.size()>=3){
                         byte[]packData=new byte[3];
                         for (int i = 0; i < 3; ++i) {
                             packData[i] = infoSaver.poll();
                         }
                         listener.DeviceEvent(CreateDeviceEvent(packData[0],packData[1],packData[2]));
                     }
                 }catch (SerialPortException e){
                     System.out.println(e);
                 }
             }
         }
     }
}
