import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageProcessor {
 ArrayList<IImageEditor>filters=new ArrayList<>();

    public void AddFilter(IImageEditor editor){
     filters.add(editor);
     }


     public BufferedImage ApplyEditors(BufferedImage original){
       for (IImageEditor edit:filters){
         original=edit.Apply(original);
       }
      return original;
     }

}
