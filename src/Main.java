import org.opencv.core.Core;
import javax.swing.*;

public class Main {
    public static void main(String[]args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        GUI gui=new GUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);

    }
}
