package ImageProcessor.ImagesTransformers;



import AppInterface.Interfaces.InterfaceResizableImage;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import ImageProcessor.ImageTransformer;
import ImageProcessor.Layer;
import ImageProcessor.SubClasses.PosFloat;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getFloatAttribute;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;

public class TransformerResizableImage extends ImageTransformer {

    private float maxScale;
    private float minScale;
    private float posRefX;
    private float posRefY;
    private float originalWidth;
    private float originalHeight;

    public TransformerResizableImage(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
        super(upperDE, elt);
        DRYRefreshDPI();
    }

    @Override
    protected void generateFromElement() throws XMLErrorInModelException {
        Element subElt = extractSingleElement(elt.getElementsByTagName("Scale"));
        this.maxScale = getFloatAttribute(subElt, "MaxScale", (float) 2.0);
        this.minScale = getFloatAttribute(subElt, "MinScale", (float) 0.5);
    }

@Override
public void DRY_DRYUpdate(BufferedImage img_in) {
    Layer layer = this.getUpperDN(Layer.class);
     float pixeMlilimeterFactor = this.getUpperDN(ImageBuilder.class).getDesignBuilder().getPixelMmFactor();
    
    float scale = ((InterfaceResizableImage) this.linkedinterface).getSliderValue();
    float currentScale = minScale + (maxScale - minScale) * scale;

    
    float newMMWidth = originalWidth * currentScale;
    float newMMHeight = originalHeight * currentScale;
    
    int newWidth = (int) (newMMWidth * pixeMlilimeterFactor);
    int newHeight = (int) (newMMHeight * pixeMlilimeterFactor);
  

    // Create a new BufferedImage for the resized image
    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = resizedImage.createGraphics();

    // Draw the original image to the new image, scaling it in the process
    g.drawImage(img_in.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
    g.dispose();

    this.imageOut = resizedImage;

    float offsetX = (originalWidth - newMMWidth) / 2;
    float offsetY = (originalHeight - newMMHeight) / 2;
     System.out.println("gggggg : "+offsetX+"       "+offsetY);

   // QuadrupletFloat posSize = new QuadrupletFloat(posRefX + offsetX, posRefY + offsetY, originalWidth, originalHeight);
    PosFloat pos = new PosFloat( offsetX, offsetY);
    layer.setAnOffset("ResizableTransformer",pos);
  //  layer.setPosSize(posSize);
 //   layer.DRYRefreshDPI();
  //  layer.update();
}


    @Override
    public void DRYRefreshDPI() {
        Layer layer = this.getUpperDN(Layer.class);
        //layer.DRYRefreshDPI();
        posRefX = layer.getPosSize().getPos_x();
        posRefY = layer.getPosSize().getPos_y();
        originalWidth = layer.getPosSize().getSize_x();
        originalHeight = layer.getPosSize().getSize_y();
    }
}
