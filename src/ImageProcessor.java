import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ImageProcessor implements Iterable{
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

     public IImageEditor GetFilters(int index){
          try{
               return filters.get(index);
          }catch(IndexOutOfBoundsException e){
               return null;
          }
     }

     @Override
     public Iterator iterator() {
          return filters.iterator();
     }

}
