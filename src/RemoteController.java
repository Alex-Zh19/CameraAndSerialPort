import jssc.*;

public class RemoteController {
     private SerialPort serialPort;
     IRemoteControllerListener listener;
     final int BUTTON1_VALUE=1;
     final int BUTTON2_VALUE=2;
     final int BUTTON3_VALUE=3;
     final int BUTTON4_VALUE=4;
     final int BUTTON5_VALUE=5;
     final int BUTTON6_VALUE=6;
     final int BUTTON7_VALUE=7;


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

    private RemoteControllerEvent CreateDeviceEvent(int value){
        RemoteControllerEvent event=new RemoteControllerEvent();
        switch (value){

            case BUTTON1_VALUE:{
                event.SetBut1(true);
                break;
            }
            case BUTTON2_VALUE:{
                event.SetBut2(true);
                break;
            }
            case BUTTON3_VALUE:{
                event.SetBut3(true);
                break;
            }
            case BUTTON4_VALUE:{
                event.SetBut4(true);
                break;
            }
            case BUTTON5_VALUE:{
                event.SetBut5(true);
                break;
            }
            case BUTTON6_VALUE:{
                event.SetBut6(true);
                break;
            }
            case BUTTON7_VALUE:{
                event.SetBut7(true);
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
                   String data = serialPort.readString();
                   Integer val=Integer.parseInt(data);
                   int value=val;
                   listener.DeviceEvent(CreateDeviceEvent(value));
                 }catch (SerialPortException e){
                     System.out.println(e);
                 }
             }
         }
     }
}
