import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageProcessor {
     private ArrayList<IImageEditor>filters=new ArrayList<>();

     public void AddFilter(IImageEditor editor){
          if(editor!=null){
          filters.add(editor);
          }
     }

     public BufferedImage ApplyFilters(BufferedImage original){
          for (IImageEditor edit:filters){
               original=edit.Apply(original);
          }
          return original;
     }

     public ArrayList<IImageEditor> GetFilters(){
          return filters;
     }
}
