import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;



public class GUI extends JFrame implements ICameraListener {
    private JMenu settingsmenu=new JMenu("Settings");
    private JMenuBar Menu=new JMenuBar();

    private JButton PauseButton=new JButton("Pause");
    private JButton CloseButton=new JButton("Close");
    private JButton ConnectButton=new JButton("Connect");

    private JLabel video=new JLabel();
    private JLabel FpsLabel=new JLabel("50");
    private JLabel CountOfPixelsLabel=new JLabel("0.0");
    private JLabel resolutionLabel=new JLabel("640х480");


    private JSlider BrightnessSlider=new JSlider();
    private JSlider ContrastSlider=new JSlider();
    private JSlider SaturationSlider=new JSlider();

    private JSlider RColorSlider=new JSlider();
    private JSlider GColorSlider=new JSlider();
    private JSlider BColorSlider=new JSlider();
    private JSlider WhiteFilterSlider=new JSlider();

    private JSlider FPSSlider=new JSlider();


    private JMenuItem saveMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem closeMenuItem;

    private JMenuItem fpsMenuItem;
    private JMenuItem resolutionMenuItem;

    CameraRecorder recorder=new CameraRecorder();
    EditRGB editRGB=new EditRGB();
    EditHSL editHSL=new EditHSL();
    EditZoom editZoom=new EditZoom();
    ImageProcessor processor=new ImageProcessor();
    BufferedImage saveImage=null;

    private String LAST_FOLDER_USED = "LAST_FOLDER_USED";

    private int WidthResol=640;
    private int HeightResol=480;

    private boolean ScheduleIsRunning=false;
    private boolean isResolutionChanged=false;
    private boolean isOpen=false;

    private   double CountOfPixels=0.0;
    ArrayList<Integer> camsID;
    JComboBox camsCombo;
    final  int DEFAULT_CAM_ID=0;
    int cameraID=DEFAULT_CAM_ID;
    Box ButtonBox = Box.createHorizontalBox();

    String ComId;
    String[]comsID;
    JComboBox comsIDCombo;
    RemoteController deviceCom;



    CompletableFuture<String[]> CountSerialPortsAsync =CompletableFuture.supplyAsync(() -> {
        comsID=RemoteController.GetSerialPorts();
        return RemoteController.GetSerialPorts();
    });

    CompletableFuture<ArrayList<Integer>> CountUSB_And_AnalyzedCameraIDAsync =CompletableFuture.supplyAsync(() -> {
        camsID=Camera.CountUSB_And_AnalyzedCameraID();
        return Camera.CountUSB_And_AnalyzedCameraID();
    });

    CompletableFuture ReturncamsID=CountUSB_And_AnalyzedCameraIDAsync.thenRun(this::AddCamstoCombo);

    CompletableFuture ReturnComsId=CountSerialPortsAsync.thenRun(this::AddComsToCombo);


    private void AddCamstoCombo(){
        String[]strcamsId=new String[camsID.size()];
        for (int i=0;i<camsID.size();++i){
            strcamsId[i]=camsID.get(i).toString();
        }
        camsCombo=new JComboBox(strcamsId);
        ButtonBox.add(camsCombo);
        camsCombo.addItemListener(new CamsIDComboListener());
        pack();
    }

    private void AddComsToCombo(){
        comsIDCombo=new JComboBox(comsID);
        ButtonBox.add(comsIDCombo);
        comsIDCombo.addItemListener(new ComPortsComboListener());
        ComId=comsID[0];
        pack();
    }

    GUI(){
        processor.AddFilter(editRGB);
        processor.AddFilter(editHSL);
        processor.AddFilter(editZoom);
        recorder.setCameraListener(this);


        video.setPreferredSize(new Dimension(WidthResol,HeightResol));
        //GUI creation start
        setTitle("Camera");

        ButtonBox.add(Box.createHorizontalStrut(20));
        ButtonBox.add(Box.createHorizontalStrut(20));
        ButtonBox.add(PauseButton);
        PauseButton.setEnabled(false);
        ButtonBox.add(Box.createHorizontalStrut(15));
        ButtonBox.add(CloseButton);
        ButtonBox.add(Box.createHorizontalStrut(15));


        ButtonBox.add(Box.createHorizontalStrut(15));
        ButtonBox.add(ConnectButton);

        ButtonBox.add(Box.createHorizontalStrut(5));



        //HSL sliders
        Box HSLSliderBox=Box.createVerticalBox();
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(new JLabel("Brightness"));
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(BrightnessSlider);
        BrightnessSlider.setMajorTickSpacing(10);
        BrightnessSlider.setMinorTickSpacing(5);
        BrightnessSlider.setMinimum(-50);
        BrightnessSlider.setMaximum(50);
        BrightnessSlider.setValue(0);
        BrightnessSlider.setPaintTicks(true);
        BrightnessSlider.setPaintLabels(true);

        HSLSliderBox.add(Box.createVerticalStrut(10));

        HSLSliderBox.add(new JLabel("Contrast"));
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(ContrastSlider);
        ContrastSlider.setMajorTickSpacing(10);
        ContrastSlider.setMinorTickSpacing(5);
        ContrastSlider.setMinimum(-50);
        ContrastSlider.setMaximum(50);
        ContrastSlider.setValue(0);
        ContrastSlider.setPaintTicks(true);
        ContrastSlider.setPaintLabels(true);

        HSLSliderBox.add(Box.createVerticalStrut(10));

        HSLSliderBox.add(new JLabel("Saturation"));
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(SaturationSlider);
        SaturationSlider.setMajorTickSpacing(10);
        SaturationSlider.setMinorTickSpacing(5);
        SaturationSlider.setMinimum(-50);
        SaturationSlider.setMaximum(50);
        SaturationSlider.setValue(0);
        SaturationSlider.setPaintTicks(true);
        SaturationSlider.setPaintLabels(true);

        HSLSliderBox.add(Box.createVerticalStrut(10));

        HSLSliderBox.add(new JLabel("White Filter"));
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(WhiteFilterSlider);
        WhiteFilterSlider.setMajorTickSpacing(10);
        WhiteFilterSlider.setMinorTickSpacing(5);
        WhiteFilterSlider.setMinimum(-50);
        WhiteFilterSlider.setMaximum(50);
        WhiteFilterSlider.setValue(0);
        WhiteFilterSlider.setPaintTicks(true);
        WhiteFilterSlider.setPaintLabels(true);

        HSLSliderBox.add(Box.createVerticalStrut(15));

        //color sliders
        Box ColorSliderBox=Box.createVerticalBox();
        ColorSliderBox.add(new JLabel("R"));
        ColorSliderBox.add(Box.createVerticalStrut(5));
        ColorSliderBox.add(RColorSlider);
        RColorSlider.setMajorTickSpacing(10);
        RColorSlider.setMinorTickSpacing(5);
        RColorSlider.setMinimum(-50);
        RColorSlider.setMaximum(50);
        RColorSlider.setValue(0);
        RColorSlider.setPaintTicks(true);
        RColorSlider.setPaintLabels(true);

        ColorSliderBox.add(Box.createVerticalStrut(10));

        ColorSliderBox.add(new JLabel("G"));
        ColorSliderBox.add(Box.createVerticalStrut(5));
        ColorSliderBox.add(GColorSlider);
        GColorSlider.setMajorTickSpacing(10);
        GColorSlider.setMinorTickSpacing(5);
        GColorSlider.setMinimum(-50);
        GColorSlider.setMaximum(50);
        GColorSlider.setValue(0);
        GColorSlider.setPaintTicks(true);
        GColorSlider.setPaintLabels(true);

        ColorSliderBox.add(Box.createVerticalStrut(10));

        ColorSliderBox.add(new JLabel("B"));
        ColorSliderBox.add(Box.createVerticalStrut(5));
        ColorSliderBox.add(BColorSlider);
        BColorSlider.setMajorTickSpacing(10);
        BColorSlider.setMinorTickSpacing(5);
        BColorSlider.setMinimum(-50);
        BColorSlider.setMaximum(50);
        BColorSlider.setValue(0);
        BColorSlider.setPaintTicks(true);
        BColorSlider.setPaintLabels(true);


        Box EditorBox = Box.createVerticalBox();
        EditorBox.add(HSLSliderBox);
        EditorBox.add(ColorSliderBox);


        Box InfoBox=Box.createHorizontalBox();
        InfoBox.add(Box.createHorizontalStrut(15));

        Box DistanceLabelBox=Box.createVerticalBox();
        DistanceLabelBox.add(new JLabel("Distance"));
        DistanceLabelBox.add(Box.createVerticalStrut(5));
        DistanceLabelBox.add(CountOfPixelsLabel);
        InfoBox.add(DistanceLabelBox);

        Box FpsLabelBox=Box.createVerticalBox();
        FpsLabelBox.add(new JLabel("FPS"));
        FpsLabelBox.add(Box.createVerticalStrut(5));
        FpsLabelBox.add(FpsLabel);
        InfoBox.add(FpsLabelBox);

        Box resolutionLabelBox=Box.createVerticalBox();
        resolutionLabelBox.add(new JLabel("Resolution"));
        resolutionLabelBox.add(Box.createVerticalStrut(5));
        resolutionLabelBox.add(resolutionLabel);
        InfoBox.add(resolutionLabelBox);


        Box SettingsBox=Box.createVerticalBox();
        SettingsBox.add(Box.createVerticalStrut(10));

        SettingsBox.add(new JLabel("FPS"));
        SettingsBox.add(Box.createVerticalStrut(5));
        SettingsBox.add(FPSSlider);
        FPSSlider.setOrientation(1);
        FPSSlider.setMajorTickSpacing(10);
        FPSSlider.setMinorTickSpacing(5);
        FPSSlider.setValue(50);
        FPSSlider.setPaintTicks(true);
        FPSSlider.setPaintLabels(true);
        FPSSlider.setMinimum(0);
        FPSSlider.setMaximum(100);


        //Listener
        CloseButton.addActionListener(new CloseButtonListener());
        PauseButton.addActionListener(new PauseButtonListener());
        ConnectButton.addActionListener(new ConnectButtonListener());
        RColorSlider.addChangeListener(new RColorSliderListener());
        BColorSlider.addChangeListener(new BColorSliderListener());
        GColorSlider.addChangeListener(new GColorSliderListener());
        ContrastSlider.addChangeListener(new ContrastSliderListener());
        BrightnessSlider.addChangeListener(new BrightnessSliderListener());
        SaturationSlider.addChangeListener(new SaturationSliderListener());
        WhiteFilterSlider.addChangeListener(new WhiteFilterSliderListener());
        addMouseWheelListener(new MouseWheelListener());
        addMouseListener(new MouseButtonsListener());
        FPSSlider.addChangeListener(new FPSSliderListener());
        //

        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);
        JMenu fileMenu = new JMenu("file");
        menu.add(fileMenu);
        JMenu settingsMenu = new JMenu("settings");
        menu.add(settingsMenu);


        saveMenuItem = fileMenu.add(saveAction);
        saveMenuItem.setEnabled(false);
        closeMenuItem=fileMenu.add(closeAction);
        fpsMenuItem=settingsMenu.add(FPS);
        resolutionMenuItem=settingsMenu.add(Resolution);
        openMenuItem=fileMenu.add(openAction);

        Container layout=getContentPane();

        layout.add(ButtonBox, BorderLayout.NORTH);
        layout. add(EditorBox,BorderLayout.EAST);
        layout. add(InfoBox,BorderLayout.SOUTH);
        layout.add(SettingsBox, BorderLayout.WEST);
        layout.add(video,BorderLayout.CENTER);


        pack();
        //GUI creation end
    }


    //main
    @Override
    public void AcceptImage(BufferedImage bi) {
        BufferedImage videoImage=null;
        videoImage=processor.ApplyEditors(bi);
        videoImage= processor.ApplyEditors(videoImage);
        videoImage=StretchOutImage(videoImage);
        RepaintImage(videoImage);
        saveImage=bi;
    }


    class ByteAccepted implements IRemoteControllerListener{
        @Override
        public void DeviceEvent(RemoteControllerEvent e) {
            if(e.isButton1Pressed()){
                //do smth


                e.SetBut1(false);
            }
            else if(e.isButton2Pressed()){
                //do smth

                e.SetBut2(false);
            }
            else if(e.isButton3Pressed()){
                //do smth

                e.SetBut3(false);
            }
            else if(e.isButton4Pressed()){
                //do smth

                e.SetBut4(false);
            }
            else if(e.isButton5Pressed()){
                //do smth

                e.SetBut5(false);
            }
            else if(e.isButton6Pressed()){
                //do smth

                e.SetBut6(false);
            }
            else if(e.isButton7Pressed()){
                //do smth

                e.SetBut7(false);
            }
        }
    }



    private void SetUf(){

    }
    private void SetBlackAndWhite(){

    }
    private void SetIR(){

    }
    private void RollUp(){

    }



    private void RepaintImage(Image image){
        video.setIcon(new ImageIcon(image));
        getContentPane().repaint();
    }

    private BufferedImage StretchOutImage(BufferedImage bi){
        int x=video.getWidth();
        int y=video.getHeight();
        double k=(double)bi.getWidth()/bi.getHeight();
        int newWidth,newHeight;
        if(x>y){
            newHeight=y;
            newWidth=(int)Math.round(k*newHeight);
            while(newWidth>x){
                --newHeight;
                newWidth=(int)Math.round(k*newHeight);
            }
        }else{
            newWidth=x;
            newHeight=(int)Math.round(newWidth/k);
            while(newHeight>y){
                --newWidth;
                newHeight=(int)Math.round(k*newWidth);
            }
        }
        BufferedImage dstImage = new BufferedImage(newWidth , newHeight , BufferedImage.TYPE_INT_RGB);
        dstImage.getGraphics().drawImage(bi.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH),
                0, 0, null);

        return dstImage;
    }


    Action openAction=new AbstractAction("Open") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            PauseButton.doClick();
            JFileChooser fileChooser=new JFileChooser();
            fileChooser.setDialogTitle("Opening");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if(LAST_FOLDER_USED!=null){
                fileChooser.setCurrentDirectory(new File(LAST_FOLDER_USED));}
            if (fileChooser.showOpenDialog(GUI.this) == JFileChooser.OPEN_DIALOG) {
                File file = fileChooser.getSelectedFile();
                OpenFile(file);
            } else{
                JOptionPane.showMessageDialog(GUI.this, "Opening canceled");
                PauseButton.doClick();
            }
        }
    };

    private void OpenFile(File selectedFile) {
        try {
             isOpen=true;
             PauseButton.setText("Pause");
             PauseButton.setEnabled(false);
             ConnectButton.setEnabled(true);
             recorder.StopAndClose();
             saveImage=ImageIO.read(selectedFile);
             recorder.Set(saveImage);
             recorder.Start();
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(this,
                    "File not found", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;

        }catch (IOException e){
            JOptionPane.showMessageDialog(this,
                    "Wrong format",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }



    Action saveAction = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser=new JFileChooser();
            fileChooser.setDialogTitle("Saving");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(LAST_FOLDER_USED!=null){
                fileChooser.setCurrentDirectory(new File(LAST_FOLDER_USED));}
            if (fileChooser.showSaveDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
                File folder = fileChooser.getSelectedFile();

                SaveToFile(folder);
            }
            else{
                JOptionPane.showMessageDialog(GUI.this, "Save error");
                System.exit(-4);
            }
        }
    };

    private void SaveToFile(File selectedDir) {
        LAST_FOLDER_USED=selectedDir.getAbsolutePath();
        Date NameOfFile = new Date();
        SimpleDateFormat FormatOfDate_Name = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = FormatOfDate_Name.format(NameOfFile) + ".jpg";
        try {
            ImageIO.write(saveImage, "png",new File(selectedDir,name));
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(GUI.this, "Save error");
            System.exit(-3);
        }
    }

    Action closeAction = new AbstractAction("Close") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            recorder.StopAndClose();
            System.exit(0);
        }
    };

    Action FPS = new AbstractAction("FPS") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Box fpsBox = Box.createVerticalBox();
            JLabel input = new JLabel("Enter FPS");
            Integer fps=recorder.GetFPS();
            String Stringfps=fps.toString();
            JTextField inputFPS = new JTextField(Stringfps, 15);
            fpsBox.add(input);
            fpsBox.add(Box.createVerticalStrut(10));
            fpsBox.add(inputFPS);
            inputFPS.setMaximumSize(inputFPS.getPreferredSize());
            JOptionPane.showMessageDialog(GUI.this,
                    fpsBox, "" +
                            "FPS", JOptionPane.QUESTION_MESSAGE);
            Integer Val= Integer.parseInt(inputFPS.getText());
            if(Val<=0){
                JOptionPane.showMessageDialog(GUI.this, "Wrong Fps");
                Val=50;
            }
            FpsLabel.setText(Val.toString());
            FPSSlider.setValue(Val);
            getContentPane().repaint();
            if(ScheduleIsRunning){
                recorder.Stop();
                int value=Val;
                recorder.SetFPS(value);
                recorder.Start();
            }else{
                int value=Val;
                recorder.SetFPS(value);
            }

        }
    };

    Action Resolution = new AbstractAction("Resolution") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Box resolBox = Box.createVerticalBox();
            JLabel input = new JLabel("Resolution");
            String[]ResolutionsStrings={"320x240","352x288","640x480","1280x720"};
            JComboBox ResolutionCombo =new JComboBox(ResolutionsStrings);
            ResolutionCombo.setSelectedItem(resolutionLabel.getText());
            resolBox.add(input);
            resolBox.add(Box.createVerticalStrut(10));
            resolBox.add(ResolutionCombo);
            ResolutionCombo.addItemListener(new ResolutionComboListener());
            JOptionPane.showMessageDialog(GUI.this,
                    resolBox, "" +
                            "Resolution", JOptionPane.QUESTION_MESSAGE);



        }
    };


    public void ShowCountOfPixels(){
        Double CountPixe;
        CountPixe=CountOfPixels/editZoom.GetZoomFactor();
        CountOfPixelsLabel.setText(CountPixe.toString());
    }



    class PauseButtonListener implements ActionListener{
        public void actionPerformed (ActionEvent ev){
            if(PauseButton.getText().equals("Pause")){
                recorder.SetIsOnPause(true);
                PauseButton.setText("Continue");
            }
            else {
                PauseButton.setText("Pause");
                recorder.SetIsOnPause(false);

            }
        }
    }


    class CloseButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            recorder.StopAndClose();
            System.exit(0);
        }
    }

class Pausebykeyboard implements java.awt.event.KeyListener{

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.println("pressed");
    if(keyEvent.getKeyChar()=='p'||keyEvent.getKeyChar()=='P'){
    PauseButton.doClick();
    }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}

    class ConnectButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
           if(isOpen){
               recorder.Stop();
           }
            if(comsID.length>0){
                isOpen=false;
                deviceCom=new RemoteController();
               if(deviceCom.Open(ComId)){
                   deviceCom.SetDeviceItemListener(new ByteAccepted());
                   Camera camera= new Camera(cameraID);
                   recorder.SetCamera(camera);
                   recorder.OpenCam();
                   camera.SetResolution(WidthResol,HeightResol);
                   video.setPreferredSize(new Dimension(WidthResol, HeightResol));
                   recorder.Start();

                   ScheduleIsRunning=true;
                   saveMenuItem.setEnabled(true);
                   PauseButton.setEnabled(true);
                   ConnectButton.setEnabled(false);
               }else{
                   JOptionPane.showMessageDialog(GUI.this, "Can't Open "+ComId);
               }

            }
            else{
                JOptionPane.showMessageDialog(GUI.this, "Com aren't found");
            }

        }

    }




    //ColorListeners
    class RColorSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            float buff;
            JSlider Buffer=(JSlider)changeEvent.getSource();
            if(!Buffer.getValueIsAdjusting()){
                int value=Buffer.getValue();
                if(value==0){
                    editRGB.SetRColorFactors(1.0f);
                    editRGB.SetRoColorOffsets(1.0f);
                }
                else {
                    buff = (float) value / 50;
                    editRGB.SetRColorFactors( 1.0f+buff);
                    editRGB.SetRoColorOffsets(value-10);
                }
            }
        }
    }
    class GColorSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            float buff;
            JSlider Buffer=(JSlider)changeEvent.getSource();
            if(!Buffer.getValueIsAdjusting()){
                int value=Buffer.getValue();
                if(value==0){
                    editRGB.SetGColorFactors(1.0f);
                    editRGB.SetGoColorOffsets(1.0f);
                }
                else {
                    buff = (float) value / 50;
                    editRGB.SetGColorFactors( 1.0f+buff);
                    editRGB.SetGoColorOffsets(value-10);
                }
            }
        }
    }
    class BColorSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            float buff;
            JSlider Buffer=(JSlider)changeEvent.getSource();
            if(!Buffer.getValueIsAdjusting()){
                int value=Buffer.getValue();
                if(value==0){
                    editRGB.SetBColorFactors(1.0f);
                    editRGB.SetBoColorOffsets(1.0f);
                }
                else {
                    buff = (float) value / 50;
                    editRGB.SetBColorFactors( 1.0f+buff);
                    editRGB.SetBoColorOffsets(value-10);
                }
            }
        }
    }


    //HSL Listeners
    class ContrastSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            float buff;
            JSlider Buffer=(JSlider)changeEvent.getSource();
            if(!Buffer.getValueIsAdjusting()){
                int value=Buffer.getValue();
                if(value==0){
                    editHSL.SetContrastFactor(1.0f);
                }
                else {
                    buff = (float) value / 25;
                    editHSL.SetContrastFactor(1.0f+buff);

                }
            }
        }
    }
    class SaturationSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            float buff;
            JSlider Buffer=(JSlider)changeEvent.getSource();
            if(!Buffer.getValueIsAdjusting()){
                int value=Buffer.getValue();
                if(value==0){
                    editHSL.SetSaturationFactor(1.0f);
                }
                else {
                    buff = (float) value / 25;
                    editHSL.SetSaturationFactor(1.0f+buff);
                }
            }
        }
    }
    class BrightnessSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            float buff;
            JSlider Buffer=(JSlider)changeEvent.getSource();
            if(!Buffer.getValueIsAdjusting()){
                int value=Buffer.getValue();
                if(value==0){
                    editHSL.SetBrightnessFactor(1.0f);
                }
                else {
                    buff = (float) value / 50;
                    editHSL.SetBrightnessFactor(1.0f+buff);
                }
            }
        }
    }

    class WhiteFilterSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            float buff;
            JSlider Buffer=(JSlider)changeEvent.getSource();
            if(!Buffer.getValueIsAdjusting()){
                int value=Buffer.getValue();
                if(value==0){
                    editHSL.SetContrastFactor(1.0f);
                    editHSL.SetSaturationFactor(1.0f);
                    editHSL.SetBrightnessFactor(1.0f);
                }
                else {
                    buff = (float) value / 50;
                    editHSL.SetContrastFactor(1.0f+buff);
                    editHSL.SetSaturationFactor(1.0f+buff);
                    editHSL.SetBrightnessFactor(1.0f+buff);
                }
            }
        }
    }



    class MouseWheelListener implements java.awt.event.MouseWheelListener{
        @Override
        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            editZoom.SetMX(mouseWheelEvent.getX());
            editZoom.SetMY(mouseWheelEvent.getY());
            if(mouseWheelEvent.getWheelRotation()>0){
                if(editZoom.GetZoomFactor()<=1.0d){
                   editZoom.SetZoomFactor(1.0d);
                }else
                if( editZoom.GetZoomFactor()>1.0d){
                    double Factor=Math.abs(mouseWheelEvent.getPreciseWheelRotation());
                    Factor=Factor/5;
                    double factor=editZoom.GetZoomFactor();
                    editZoom.SetZoomFactor(factor-Factor);
                }
            }
            else {
                if (editZoom.GetZoomFactor() <= 2.0d ) {
                    double Factor = Math.abs(mouseWheelEvent.getPreciseWheelRotation());
                    Factor = Factor / 5;
                    double factor=editZoom.GetZoomFactor();
                    editZoom.SetZoomFactor(factor+Factor);
                }
            }

        }
    }
    class MouseButtonsListener implements MouseListener{
        double x=0;
        double y=0;
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            this.x=mouseEvent.getX();
            this.y=mouseEvent.getY();
            if(isInFrame(x,y)) {
               if(PauseButton.getText().equals("Pause")){
                   PauseButton.doClick();
               }
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            double x1=mouseEvent.getX();
            double y1=mouseEvent.getY();
            if(isInFrame(x1,y1)){
            CountOfPixels=Math.sqrt((Math.abs(x1-this.x)*Math.abs(x1-this.x))+(Math.abs(y1-this.y)*Math.abs(y1-this.y)));
            ShowCountOfPixels();
            PauseButton.doClick();
            }

        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }

        private boolean isInFrame(double mX,double mY){
            if(mX>0&&mX<video.getWidth()
                    &&mY>0&&mY<video.getHeight()){
                return true;
            }
            return false;
        }

    }


    class ResolutionComboListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if(!ConnectButton.isEnabled()){
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    String str = (String) event.getItem();
                    resolutionLabel.setText(str);
                    int x = str.indexOf('x');
                    String str1 = str.substring(0, x);
                    Double Width = Double.parseDouble(str1);
                    str1 = str.substring(x + 1);
                    Double Height = Double.parseDouble(str1);
                    WidthResol=(int)Math.round(Width);
                    HeightResol=(int)Math.round(Height);
                    PauseButton.setEnabled(false);
                    saveMenuItem.setEnabled(false);
                    ConnectButton.setEnabled(true);
                    recorder.Stop();
                    deviceCom.Close();
                }

            }
            else{
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    String str = (String) event.getItem();
                    resolutionLabel.setText(str);
                    int x = str.indexOf('x');
                    String str1 = str.substring(0, x);
                    Double Width = Double.parseDouble(str1);
                    str1 = str.substring(x + 1);
                    Double Height = Double.parseDouble(str1);
                    WidthResol=(int)Math.round(Width);
                    HeightResol=(int)Math.round(Height);

                }
            }
        }

    }

    class MenuLister implements MenuListener{
        @Override
        public void menuCanceled(MenuEvent menuEvent) {

        }

        @Override
        public void menuSelected(MenuEvent menuEvent) {

        }

        @Override
        public void menuDeselected(MenuEvent menuEvent) {

        }

    }

    class FPSSliderListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            JSlider Buffer=(JSlider)changeEvent.getSource();
            if(!Buffer.getValueIsAdjusting()){
                int value=Buffer.getValue();
                if(value==0){
                    value=1;
                }
                Integer Val=value;
                FpsLabel.setText(Val.toString());
                if(ScheduleIsRunning){
                    recorder.Stop();
                }
                recorder.SetFPS(value);
                recorder.Start();
            }
        }
    }



    class CamsIDComboListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if(!ConnectButton.isEnabled()){
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    recorder.StopAndClose();
                    String strNextCam=(String) event.getItem();
                    Integer nextCam=Integer.parseInt(strNextCam);
                    cameraID=nextCam;
                    PauseButton.setEnabled(false);
                    saveMenuItem.setEnabled(false);
                    ConnectButton.setEnabled(true);
                    ConnectButton.doClick();
                }

            }
            else{
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    String strNextCam=(String) event.getItem();
                    Integer nextCam=Integer.parseInt(strNextCam);
                    cameraID=nextCam;
                    PauseButton.setEnabled(false);
                    saveMenuItem.setEnabled(false);
                    ConnectButton.setEnabled(true);//check here is mistake
                }
            }
        }

    }
    class ComPortsComboListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            if(!ConnectButton.isEnabled()){
                //while running
                recorder.StopAndClose();
                deviceCom.Close();
                String strNextCom=(String) itemEvent.getItem();
                ComId=strNextCom;
                PauseButton.setEnabled(false);
                saveMenuItem.setEnabled(false);
                ConnectButton.setEnabled(true);
                ConnectButton.doClick();
            }
            else{
                //before running
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    String strNextCom=(String) itemEvent.getItem();
                    ComId=strNextCom;
                    PauseButton.setEnabled(false);
                    saveMenuItem.setEnabled(false);
                }
            }
        }
    }


}




