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



public class GUI extends JFrame implements ICameraListener,IRemoteControllerListener {
    private JMenu settingsmenu=new JMenu("Настройки");
    private JMenuBar Menu=new JMenuBar();

    private JButton PauseButton=new JButton("Пауза");
    private JButton CloseButton=new JButton("Закрыть");
    private JButton ConnectButton=new JButton("Соединить");
    private JButton resetButton=new JButton("Сброс");

    private JLabel video=new JLabel();
    private JLabel FpsLabel=new JLabel("50");
    private JLabel CountOfPixelsLabel=new JLabel("0.0");
    private JLabel resolutionLabel=new JLabel("640x480");
    private JLabel ModeLabel=new JLabel("Режим");


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
    private boolean isOnPause=false;
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
        ButtonBox.add(resetButton);
        resetButton.setEnabled(false);
        ButtonBox.add(Box.createHorizontalStrut(15));
        ButtonBox.add(CloseButton);
        ButtonBox.add(Box.createHorizontalStrut(15));



        ButtonBox.add(Box.createHorizontalStrut(15));
        ButtonBox.add(ConnectButton);

        ButtonBox.add(Box.createHorizontalStrut(5));



        //HSL sliders
        Box HSLSliderBox=Box.createVerticalBox();
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(new JLabel("Яркость"));
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(BrightnessSlider);
        BrightnessSlider.setMajorTickSpacing(10);
        BrightnessSlider.setMinorTickSpacing(5);
        BrightnessSlider.setMinimum(-20);
        BrightnessSlider.setMaximum(80);
        BrightnessSlider.setValue(0);
        BrightnessSlider.setPaintTicks(true);
        BrightnessSlider.setPaintLabels(true);

        HSLSliderBox.add(Box.createVerticalStrut(10));

        HSLSliderBox.add(new JLabel("Контрастность"));
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(ContrastSlider);
        ContrastSlider.setMajorTickSpacing(10);
        ContrastSlider.setMinorTickSpacing(5);
        ContrastSlider.setMinimum(-10);
        ContrastSlider.setMaximum(90);
        ContrastSlider.setValue(0);
        ContrastSlider.setPaintTicks(true);
        ContrastSlider.setPaintLabels(true);

        HSLSliderBox.add(Box.createVerticalStrut(10));

        HSLSliderBox.add(new JLabel("Насыщенность"));
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(SaturationSlider);
        SaturationSlider.setMajorTickSpacing(10);
        SaturationSlider.setMinorTickSpacing(5);
        SaturationSlider.setMinimum(-20);
        SaturationSlider.setMaximum(80);
        SaturationSlider.setValue(0);
        SaturationSlider.setPaintTicks(true);
        SaturationSlider.setPaintLabels(true);

        HSLSliderBox.add(Box.createVerticalStrut(10));

        HSLSliderBox.add(new JLabel("Белый фильтр"));
        HSLSliderBox.add(Box.createVerticalStrut(5));
        HSLSliderBox.add(WhiteFilterSlider);
        WhiteFilterSlider.setMajorTickSpacing(10);
        WhiteFilterSlider.setMinorTickSpacing(5);
        WhiteFilterSlider.setMinimum(-20);
        WhiteFilterSlider.setMaximum(80);
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
        DistanceLabelBox.add(new JLabel("Расстояние"));
        DistanceLabelBox.add(Box.createVerticalStrut(5));
        DistanceLabelBox.add(CountOfPixelsLabel);
        InfoBox.add(DistanceLabelBox);

        Box FpsLabelBox=Box.createVerticalBox();
        FpsLabelBox.add(new JLabel("FPS"));
        FpsLabelBox.add(Box.createVerticalStrut(5));
        FpsLabelBox.add(FpsLabel);
        InfoBox.add(FpsLabelBox);

        Box resolutionLabelBox=Box.createVerticalBox();
        resolutionLabelBox.add(new JLabel("Разрешение"));
        resolutionLabelBox.add(Box.createVerticalStrut(5));
        resolutionLabelBox.add(resolutionLabel);
        InfoBox.add(resolutionLabelBox);

        Box ModeLabelBox=Box.createVerticalBox();
        ModeLabelBox.add(new JLabel("Режим"));
        ModeLabelBox.add(Box.createVerticalStrut(5));
        ModeLabelBox.add(ModeLabel);
        InfoBox.add(ModeLabelBox);

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
        addKeyListener(new Pausebykeyboard());
        resetButton.addActionListener(new ResetButtonListener());
        //

        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);
        JMenu fileMenu = new JMenu("Файл");
        menu.add(fileMenu);
        JMenu settingsMenu = new JMenu("Настройки");
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

    @Override
    public void DeviceEvent(RemoteControllerEvent e) {
        if(e.isButton1Pressed()){//1
           WhiteTop(e);
            e.SetBut1(false);
        }
        else if(e.isButton2Pressed()){//2
            //do  WhiteOblique1
             WhiteOblique1(e);
            e.SetBut2(false);
        }
        else if(e.isButton3Pressed()){//3
            //do WhiteOblique2
            WhiteOblique2(e);
            e.SetBut3(false);
        }
        else if(e.isButton4Pressed()){//4
            //do WhiteOblique3
            WhiteOblique3(e);
            e.SetBut4(false);
        }
        else if(e.isButton5Pressed()){//5
            //do WhiteOblique4
            WhiteOblique4(e);
            e.SetBut5(false);
        }
        else if(e.isButton6Pressed()){//6
            //do uf
              Uf(e);
            e.SetBut6(false);
        }
        else if(e.isButton7Pressed()){//7
            //do antistocks
             AntiStocks(e);
            e.SetBut7(false);
        }
        else if(e.isButton8Pressed()){//8
            //do IR top 830
             IRTop830(e);
            e.SetBut8(false);
        }
        else if(e.isButton9Pressed()){//9
            //do IR top 950
              IRTop950(e);
            e.SetBut9(false);
        }
        else if(e.isButton10Pressed()){//10
            //do IR oblique1
              IROblique1(e);
            e.SetBut10(false);
        }
        else if(e.isButton11Pressed()){//11
            //do IR oblique2
            IROblique2(e);
            e.SetBut11(false);
        }
        else if(e.isButton12Pressed()){//12
            //do IR oblique3
            IROblique3(e);
            e.SetBut12(false);
        }
        else if(e.isButton13Pressed()){//13
            //do IR oblique4
            IROblique4(e);
            e.SetBut13(false);
        }
        else if(e.isButton14Pressed()){//14
            //do IR Lum
             IRLum(e);
            e.SetBut14(false);
        }
        else if(e.isButton15Pressed()){//15
            //do M-mark
               M_Mark(e);
            e.SetBut15(false);
        }
        else if(e.isButton16Pressed()){//16
            //do IR circular
              IRCircular(e);
            e.SetBut16(false);
        }
        else if(e.isButton17Pressed()){//17
            //do WhiteCircule
            WhiteCircular(e);
            e.SetBut17(false);
        }

        else if(e.isButton18Pressed()){//18
            //do zoom
            ZoomControl(e);
            e.SetBut18(false);
        }
        else if(e.isButton19Pressed()){//19
            //do pause
            PuaseControl();
            e.SetBut19(false);
        }
        else if(e.isButton20Pressed()){//20
            //do sleep
            RollUp();
            e.SetBut20(false);
        }


    }

    private void WhiteTop(RemoteControllerEvent e){//1
        int v=(int)Math.round(e.GetVal()*0.8);
        System.out.println("white top");
        WhiteFilterSlider.setValue(v);
        ModeLabel.setText("Белый Верхний");
    }
    private void WhiteOblique1(RemoteControllerEvent e){//2
        int v=(int)Math.round(e.GetVal()*0.8);
        System.out.println("white Oblique1");
        WhiteFilterSlider.setValue(v);
        ModeLabel.setText("Белый Косопадающий 1");
    }
    private void WhiteOblique2(RemoteControllerEvent e){//3
        int v=(int)Math.round(e.GetVal()*0.8);
        System.out.println("white Oblique2");
        WhiteFilterSlider.setValue(v);
        ModeLabel.setText("Белый Косопадающий 2");
    }
    private void WhiteOblique3(RemoteControllerEvent e){//4
        int v=(int)Math.round(e.GetVal()*0.8);
        System.out.println("white Oblique3");
        WhiteFilterSlider.setValue(v);
        ModeLabel.setText("Белый Косопадающий 3");
    }
    private void WhiteOblique4(RemoteControllerEvent e){//5
        int v=(int)Math.round(e.GetVal()*0.8);
        System.out.println("white Oblique4");
        WhiteFilterSlider.setValue(v);
        ModeLabel.setText("Белый Косопадающий 4");
    }

    private void Uf(RemoteControllerEvent e){//6
        int val=e.GetVal();
        System.out.println(val+" UF");
        ModeLabel.setText("УФ");
    }

    private void AntiStocks(RemoteControllerEvent e){//7

        int val=e.GetVal();
        System.out.println(val+" Antistocks");
        ModeLabel.setText("Антистокс");
    }
    private void IRTop830(RemoteControllerEvent e){//8
         int val=e.GetVal();
        System.out.println(val+" IRTop830");
        ModeLabel.setText("ИК Верхний 830нм");
    }
    private void IRTop950(RemoteControllerEvent e){//9
        int val=e.GetVal();
        System.out.println(val+" IRTop950");
        ModeLabel.setText("ИК Верхний 950нм");
    }
    private void IROblique1(RemoteControllerEvent e){//10
        int val=e.GetVal();
        System.out.println(val+" IR Oblique1");
        ModeLabel.setText("ИК Косопадающий 1");
    }
    private void IROblique2(RemoteControllerEvent e){//11
        int val=e.GetVal();
        System.out.println(val+" IR Oblique2");
        ModeLabel.setText("ИК Косопадающий 2");
    }
    private void IROblique3(RemoteControllerEvent e){//12
        int val=e.GetVal();
        System.out.println(val+" IR Oblique3");
        ModeLabel.setText("ИК Косопадающий 3");
    }
    private void IROblique4(RemoteControllerEvent e){//13
        int val=e.GetVal();
        System.out.println(val+" IR Oblique4");
        ModeLabel.setText("ИК Косопадающий 4");
    }
    private void IRLum(RemoteControllerEvent e){//14
        int val=e.GetVal();
        System.out.println(val+" IR Lum");
        ModeLabel.setText("ИК Люминесценция");
    }
    private void M_Mark(RemoteControllerEvent e){//15
        int val=e.GetVal();
        System.out.println(val+" M_mark");
        ModeLabel.setText("М-метка");
    }
    private void IRCircular(RemoteControllerEvent e){//16
        int val=e.GetVal();
        int range=e.GetRange();
        System.out.println(val+"  range:"+range+" IR Oblique4");
        ModeLabel.setText("ИК Круговой");
    }
    private void WhiteCircular(RemoteControllerEvent e){//17
        int v=(int)Math.round(e.GetVal()*0.8);
        int range=e.GetRange();
        System.out.println("white Circular");
        WhiteFilterSlider.setValue(v);
        ModeLabel.setText("Белый Круговой");
    }
    private void ZoomControl(RemoteControllerEvent e){//18
        if( editZoom.GetZoomFactor()>1.0d){
            double Fact = e.GetVal();
            Fact = Fact / 50;
            double factor=editZoom.GetZoomFactor();
            editZoom.SetZoomFactor(factor-Fact);
        }else
        if(editZoom.GetZoomFactor()<=3.0d) {
            double Fact = e.GetVal();
            Fact = Fact / 50;
            double factor = editZoom.GetZoomFactor();
            editZoom.SetZoomFactor(factor + Fact);
        }
    }
    private void PuaseControl(){//19
        PauseButton.doClick();
    }

    private void RollUp(){//20
        System.out.println("rollUp");
        setState(JFrame.ICONIFIED);
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



    Action openAction=new AbstractAction("Открыть") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(!isOnPause){
            PauseButton.doClick();
            }
            JFileChooser fileChooser=new JFileChooser();
            fileChooser.setDialogTitle("Открытие...");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if(LAST_FOLDER_USED!=null){
                fileChooser.setCurrentDirectory(new File(LAST_FOLDER_USED));}
            if (fileChooser.showOpenDialog(GUI.this) == JFileChooser.OPEN_DIALOG) {
                File file = fileChooser.getSelectedFile();
                OpenFile(file);
            } else{
                JOptionPane.showMessageDialog(GUI.this, "Открытие отменено");
                PauseButton.doClick();
            }
        }
    };

    private void OpenFile(File selectedFile) {
        try {
             isOpen=true;
             FPSSlider.setEnabled(false);
             PauseButton.setText("Пауза");
             PauseButton.setEnabled(false);
             ConnectButton.setEnabled(true);
             recorder.StopAndClose();
             saveImage=ImageIO.read(selectedFile);
             recorder.Set(saveImage);
             recorder.Start();
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(this,
                    "Файл не найден", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;

        }catch (IOException e){
            JOptionPane.showMessageDialog(this,
                    "Неправильный формат",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }



    Action saveAction = new AbstractAction("Сохранить") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser=new JFileChooser();
            fileChooser.setDialogTitle("Сохранение...");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(LAST_FOLDER_USED!=null){
                fileChooser.setCurrentDirectory(new File(LAST_FOLDER_USED));}
            if (fileChooser.showSaveDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
                File folder = fileChooser.getSelectedFile();

                SaveToFile(folder);
            }
            else{
                JOptionPane.showMessageDialog(GUI.this, "Сохранение отменено");
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
            JOptionPane.showMessageDialog(GUI.this, "Ошибка сохранения");

        }
    }

    Action closeAction = new AbstractAction("Закрыть") {
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
            JLabel input = new JLabel("Введите FPS");
            Integer fps=Integer.parseInt(FpsLabel.getText());
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
            if(Val<=0||Val>100){
                JOptionPane.showMessageDialog(GUI.this, "Нерпаивльный формат");
                Val=50;
            }
            FpsLabel.setText(Val.toString());
            FPSSlider.setValue(Val);
            getContentPane().repaint();
            if(ScheduleIsRunning){
                recorder.Stop();
                int value=Val;
                int realVal=(int)Math.round(1000/value);
                recorder.SetFPS(realVal);
                recorder.Start();
            }else{
                int value=Val;
                int realVal=(int)Math.round(1000/value);
                recorder.SetFPS(realVal);
            }

        }
    };

    Action Resolution = new AbstractAction("Разрешение") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Box resolBox = Box.createVerticalBox();
            JLabel input = new JLabel("Разрешение");
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
            if(PauseButton.getText().equals("Пауза")){
                isOnPause=true;
                recorder.SetIsOnPause(true);
                PauseButton.setText("Продолжить");
            }
            else {
                isOnPause=false;
                PauseButton.setText("Пауза");
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
                   FPSSlider.setEnabled(true);
                   deviceCom.SetDeviceItemListener(GUI.this::DeviceEvent);
                   Camera camera= new Camera(cameraID);
                   recorder.SetCamera(camera);
                   recorder.OpenCam();
                   camera.SetResolution(WidthResol,HeightResol);
                   video.setPreferredSize(new Dimension(WidthResol, HeightResol));
                   recorder.Start();

                   ScheduleIsRunning=true;
                   saveMenuItem.setEnabled(true);
                   PauseButton.setEnabled(true);
                   resetButton.setEnabled(true);
                   ConnectButton.setEnabled(false);
               }else{
                   JOptionPane.showMessageDialog(GUI.this, "Невозможно открыть "+ComId);
               }

            }
            else{
                JOptionPane.showMessageDialog(GUI.this, "Ком не найден");
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
                    buff = (float) value / 50;
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
                    buff = (float) value / 35;
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
        boolean startonframe=false;
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            this.x=mouseEvent.getX();
            this.y=mouseEvent.getY();
            if(!isOnPause){
                if(isInFrame(x,y)) {
                    PauseButton.doClick();
                    isOnPause=false;
                }
            }
            if(isInFrame(x,y)){
                startonframe=true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            if(startonframe) {
                double x1 = mouseEvent.getX();
                double y1 = mouseEvent.getY();
                if (isInFrame(x1, y1)) {
                    CountOfPixels = Math.sqrt((Math.abs(x1 - this.x) * Math.abs(x1 - this.x)) +
                                     (Math.abs(y1 - this.y) * Math.abs(y1 - this.y)));
                    ShowCountOfPixels();
                }
                if (!isOnPause) {
                    PauseButton.doClick();
                }
            }
            startonframe=false;
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
                    if(isOnPause){
                        PauseButton.doClick();
                    }
                    recorder.Stop();
                    deviceCom.Close();
                    FPSSlider.setEnabled(false);
                    resetButton.setEnabled(false);
                    PauseButton.setEnabled(false);
                    saveMenuItem.setEnabled(false);
                    ConnectButton.setEnabled(true);

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
                int realVal=(int)Math.round(1000/value);
                recorder.SetFPS(realVal);
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
                    deviceCom.Close();
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
    class ResetButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event) {
        RColorSlider.setValue(0);
        GColorSlider.setValue(0);
        BColorSlider.setValue(0);
        SaturationSlider.setValue(0);
        ContrastSlider.setValue(0);
        BrightnessSlider.setValue(0);
        WhiteFilterSlider.setValue(0);
        CountOfPixelsLabel.setText("0.0");
        }

    }


}




