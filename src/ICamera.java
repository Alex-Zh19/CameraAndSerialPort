import java.awt.image.BufferedImage;

public interface ICamera {
    void Open();
    BufferedImage Read();
    void Grab();
    BufferedImage Get();
    int GetXResolution();
    int GetYResolution();
    void SetResolution(int Width,int Height);
    void Close();

}
