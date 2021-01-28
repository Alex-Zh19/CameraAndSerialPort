import java.awt.image.BufferedImage;

public interface ICamera {
    void Open();
    BufferedImage Read();
    void Grab();
    BufferedImage Get();
    void Set(BufferedImage bi);
    int GetXResolution();
    int GetYResolution();
    void SetResolution(int Width,int Height);
    //void SetFPS(double value);
    //double GetFPS();





    void Close();


}
