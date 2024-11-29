package imageBuilder;

import Exceptions.TheXmlElementIsNotANodeException;
import Exceptions.ThisLayerDoesNotExistException;
import Layers.Layer;

import designBuilder.DesignBuilder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The role of this class is to build a image by piling it's differents layer
 * This calls is also in charge of initialising every layer from every types
 *
 * @author LECOURT Camille
 */
public class ImageBuilder {

        private String name; // name the the Image Builder :  "Index of the windows" + "Name given in the model.XML file"
        private final DesignBuilder designBuilder;

        private float x_size;
        private float y_size;

        private int x_p_size;
        private int y_p_size;

        ArrayList<Layer> layers = new ArrayList<>();

        private Node loaderNode;

        /**
         * This constructor use
         *
         * @param batcher for reference and subInterface display
         * @param loaderNode
         */
        public ImageBuilder(DesignBuilder batcher, Node loaderNode) {
                this.designBuilder = batcher;
                this.loaderNode = loaderNode;
                try {
                        Element element = (Element) loaderNode;
                        if (loaderNode.getNodeType() != Node.ELEMENT_NODE) {
                                throw new TheXmlElementIsNotANodeException("IN ImageBuilder"+loaderNode.getNodeName());
                        }
                        this.name = this.designBuilder.getId() + "_" + element.getAttribute("name");
                        this.x_size = Float.parseFloat(element.getAttribute("size_x"));
                        this.y_size = Float.parseFloat(element.getAttribute("size_y"));

                } catch (TheXmlElementIsNotANodeException ex) {
                        Logger.getLogger(ImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.computeXY_p_size();
                this.createLayers();
        }

        
        /**
         * This function retunr the pixel milimeter fator from the batcher
         *
         * @return
         */
        public float get_pixel_mm_Factor() {
                return this.designBuilder.get_pixel_mm_Factor();
        }

        
        /**
         * this function is use to compute the size of the image in pixel
         */
        private void computeXY_p_size() {
                float factor = get_pixel_mm_Factor();
                this.x_p_size = (int) (this.x_size * factor);
                this.y_p_size = (int) (this.y_size * factor);
        }

        
        /**
         * Creates a BufferedImage with the specified dimensions.
         *
         * @param x the width of the image
         * @param y the height of the image
         * @return the created BufferedImage
         */
        public static BufferedImage createBufferedImage(int x, int y) {
                // Create a BufferedImage with the specified dimensions
                BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);

                // Get the Graphics2D object to draw on the image
                Graphics2D g2d = image.createGraphics();

                // Set the background color to black with 0 opacity
                Color transparentBlack = new Color(0, 0, 0, 0);
                g2d.setColor(transparentBlack);
                g2d.fillRect(0, 0, x, y);

                // Dispose of the Graphics2D object to free resources
                g2d.dispose();

                return image;
        }

        
        /**
         * This code is used to refresh the final image in the case of a
         * modification of one layer. (we dont compute the images_out of the
         * layer if there is no modification before. -- upgrade ? : considering
         * only the zone modified by the changed layer
         */
        public void refresh() {
                boolean changedPrecedently = false;
                BufferedImage imgBegining = createBufferedImage(this.x_p_size, this.y_p_size);
                int indexBegining = 0;
                for (int i = 0; i < layers.size(); i++) {
                        if (layers.get(i).isChanged()) {
                                changedPrecedently = true;
                                if (i != 0) {
                                        layers.get(i).computeImage_Out(this.name);
                                        imgBegining = layers.get(i).getImage_out(this.name);
                                        indexBegining = i;
                                }
                                break;
                        }
                }

                if (changedPrecedently & indexBegining >= (layers.size() - 1)) { //we will refresh the image out from there if the image is changed ot if a precedent imag is changed
                        layers.get(indexBegining).setImage_in(this.name, imgBegining);
                        layers.get(indexBegining).computeImage_Out(this.name);

                        for (int j = indexBegining + 1; j < layers.size(); j++) {
                                layers.get(j).setImage_in(this.name, layers.get(j - 1).getImage_out(this.name));
                                layers.get(j).computeImage_Out(this.name);
                        }
                }
        }

        
        /**
         * This code refresh All the Image (including all the previews of the
         * differrents layers)
         */
        public void refreshAll() {
                BufferedImage imgBegining = createBufferedImage(this.x_p_size, this.y_p_size);
                layers.get(0).setImage_in(this.name, imgBegining);
                layers.get(0).computeImage_Out(this.name);

                for (int j = 1; j < layers.size(); j++) {
                        layers.get(j).setImage_in(this.name, layers.get(j - 1).getImage_out(this.name));
                        layers.get(j).computeImage_Out(this.name);
                }
        }
        
        
        /**
         * This function will create the layer using it's parameter XML
         *  it will mainly use the Layer class multi buider for it's work
         */
        private void createLayers() {
                NodeList nodeLayerList = this.loaderNode.getChildNodes();
//                System.out.println("Name  "+this.loaderNode.getNodeName()+"longueru "+nodeLayerList.getLength());
//                for(int i=0;i<nodeLayerList.getLength();i++){
//                        System.out.println("nodeName "+i+"  "+nodeLayerList.item(i).getNodeName());
//                }

                for (int i = 0; i < nodeLayerList.getLength(); i++) {
                        if (nodeLayerList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node
                                try{
                                Layer layerTocreate = Layer.loadLayer(this, nodeLayerList.item(i), this.designBuilder.getTemplateResources(), this.designBuilder.getDesignResources());
                                        layers.add(layerTocreate);
                                }catch(ThisLayerDoesNotExistException e){
                                        System.out.println(e+" was detected ignoting it");
                                }

                        }
                }
                this.refreshAll();
        }
        
        
        public String getName() {
                return this.name;
        }

        public void assignLayerToTab(Layer layer, String tabName) {
                if(layer.isHaveTiltlePane()) this.designBuilder.assignLayerToTab(layer,tabName);
        }
        
        

        @Override
        public String toString() {
                return "ImageBuilder{" + "name=" + name  + ", x_size=" + x_size + ", y_size=" + y_size + ", x_p_size=" + x_p_size + ", y_p_size=" + y_p_size + ", layers=" + layers  + '}';
        }

        public int getX_p_size() {
                return x_p_size;
        }

        public int getY_p_size() {
                return y_p_size;
        }
        
        
        
        
        
}
