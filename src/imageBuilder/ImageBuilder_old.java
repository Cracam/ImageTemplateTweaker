package imageBuilder;

import Exceptions.TheXmlElementIsNotANodeException;
import Layers.Layer_old;

import AppInterface.DesignBuilder;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import previewimagebox.PreviewImageBox;
import staticFunctions.StaticImageEditing;
import static staticFunctions.StaticImageEditing.createBufferedImage;

/**
 * The role of this class is to build a image by piling it's differents layer
 * This calls is also in charge of initialising every layer from every types
 *
 * @author LECOURT Camille
 */
public class ImageBuilder_old {

         String name; // name the the Image Builder :  "Index of the windows" + "Name given in the model.XML file"
         final DesignBuilder designBuilder;

         float x_size;
         float y_size;

         int x_p_size;
         int y_p_size;

        ArrayList<Layer_old> layers = new ArrayList<>();

         Node loaderNode;
     
        /**
         * This constructor use
         *
         * @param batcher for reference and subInterface display
         * @param loaderNode
         */
        public ImageBuilder_old(DesignBuilder batcher, Node loaderNode) {
                this.designBuilder = batcher;
                this.loaderNode = loaderNode;
                try {
                        Element element = (Element) loaderNode;
                        if (loaderNode.getNodeType() != Node.ELEMENT_NODE) {
                                throw new TheXmlElementIsNotANodeException("IN ImageBuilder" + loaderNode.getNodeName());
                        }
                        this.name = this.designBuilder.getId() + "_" + element.getAttribute("name");
                        this.x_size = Float.parseFloat(element.getAttribute("size_x"));
                        this.y_size = Float.parseFloat(element.getAttribute("size_y"));

                } catch (TheXmlElementIsNotANodeException ex) {
                        Logger.getLogger(ImageBuilder_old.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.computeXY_p_size();
                this.createLayers();
        }
        
        
         /**
         * This constructor use
         *
         * @param batcher for reference and subInterface display
         * @param loaderNode
         * @param size_x
         * @param size_y
         */
        public ImageBuilder_old(DesignBuilder batcher, Node loaderNode, float size_x, float size_y ) {
                
                this.designBuilder = batcher;
                this.loaderNode = loaderNode;
             
                      //  this.name = this.designBuilder.getId() + "_" + "INTER";
                        this.x_size =size_x;
                        this.y_size = size_y;
              
                this.computeXY_p_size();
        }
        

        
      

        /**
         * This function retunr the pixel milimeter fator from the batcher
         *
         * @return
         */
        public float get_pixel_mm_Factor() {
                return this.designBuilder.getPixelMmFactor();
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
         * This code is used to refresh the final image in the case of a
         * modification of one layer. (we dont compute the images_out of the
         * layer if there is no modification before. -- upgrade ? : considering
         * only the zone modified by the changed layer
         */
        public void DRYrefresh() {
                boolean changedPrecedently = false;
                BufferedImage imgBegining = null;
                int indexBegining = 0;
                for (int i = 0; i < layers.size(); i++) {
                        if (layers.get(i).isChanged()) {
                                changedPrecedently = true;
                                if (i != 0) {
                                        layers.get(i).computeImage_Out(this.name);
                                        imgBegining = layers.get(i - 1).getImage_out();
                                } else {
                                        imgBegining = createBufferedImage(this.x_p_size, this.y_p_size);
                                }
                                indexBegining = i;
                                break;
                        }
                }

                if (!changedPrecedently) {
                        return;
                }

                layers.get(indexBegining).setImage_in(imgBegining);
                layers.get(indexBegining).computeImage_Out(this.name);

                for (int j = indexBegining + 1; j < layers.size(); j++) {
                        layers.get(j).setImage_in(layers.get(j - 1).getImage_out());
                        layers.get(j).computeImage_Out(this.name);
                        layers.get(j).refreshPreview();
                }
       //         System.out.println("REFRESH");
                this.designBuilder.refreshPreview();//refresh the main preview
        }

        /**
         * This code refresh All the Image (including all the previews of the
         * differrents layers)
         */
        public void DRYrefreshAll() {
                BufferedImage imgBegining = createBufferedImage(this.x_p_size, this.y_p_size);
                layers.get(0).setImage_in(imgBegining);
                layers.get(0).refreshImageGet();
                layers.get(0).computeImage_Out(this.name);
                layers.get(0).refreshPreview();

                for (int j = 1; j < layers.size(); j++) {
                        layers.get(j).setImage_in(layers.get(j - 1).getImage_out());
                        layers.get(j).refreshImageGet();
                        layers.get(j).computeImage_Out(this.name);
                        layers.get(j).refreshPreview();
                }
                this.designBuilder.refreshPreview();
        }

        
         public void refresh() {
                 DRYrefresh();
         }
        
           public void refreshAll() {
                    DRYrefreshAll();
         }
        
        /**
         * This function will create the layer using it's parameter XML it will
         * mainly use the Layer class multi buider for it's work
         */
          void createLayers() {
                NodeList nodeLayerList = this.loaderNode.getChildNodes();

//                for (int i = 0; i < nodeLayerList.getLength(); i++) {
//                        if (nodeLayerList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node
//                                try {
//                                        Element element = (Element) nodeLayerList.item(i);
//                                        String key = element.getNodeName(); // key for defining the layer and the Interface
//                                        String nameElement = element.getAttribute("name");
//
//                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
//                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        //Create an interface or return it
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
//                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
//                                        Interface linkedInterface = this.designBuilder.getInterface(key, nameElement);
//                                        if (linkedInterface == null) {
//                                                linkedInterface = Interface.createInterface(key, nameElement, this.designBuilder);
//                                                this.designBuilder.addInterface(linkedInterface);
//                                                if (linkedInterface.isHaveGraphicInterface()) {
//                                                        String tabname = element.getAttribute("tab_name");
//                                                        if (tabname == null) {
//                                                                tabname = "Defaut";
//                                                        }
//                                                        this.designBuilder.assignInterfaceToTab(tabname, linkedInterface);
//                                                     
//                                                }
//                                        }
//                                          linkedInterface.activatePreview();
//                                          
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // Create a layer
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
//                                        float pos_x = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_x").getNodeValue());
//                                        float pos_y = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_y").getNodeValue());
//                                        float size_x = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_x").getNodeValue());
//                                        float size_y = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_y").getNodeValue());
//
//                                        QuadrupletFloat posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
//                                        // System.out.println(key+ nameElement+ this.designBuilder.getModelResources()+ linkedInterface);
//                                        Layer_old layerCreated = Layer_old.createLayer(key, nameElement, this.designBuilder.getModelResources(), linkedInterface, this, posSize);
//
//                                        if (!(layerCreated == null)) {
//                                                System.out.println(layerCreated);
//                                                //This code verify if the <Param> element is really an element
////                                                Element retElement = (Element) element.getElementsByTagName("Param").item(0);
//                                                if (retElement.getNodeType() != Node.ELEMENT_NODE) {
//                                                        throw new TheXmlElementIsNotANodeException("IN Layer(2) " + nameElement);
//                                                }
//                                                layerCreated.readNode(retElement); //read the specific parameter
//
//                                                // add the layer to the layers list
//                                                this.layers.add(layerCreated);
//
//                                                linkedInterface.linkNewLayer(layerCreated); //link the layer tothe interface
//                                                linkedInterface.linkNewImageBuilder(this);
//                                        }
//
//                                } catch (GetAttributeValueException | ThisInterfaceDoesNotExistException e) {
//                                        System.out.println(e + " was detected ignoting it");
//                                } catch (TheXmlElementIsNotANodeException ex) {
//                                        Logger.getLogger(ImageBuilder_old.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                        }
//                }
//        }
        
      
//
//        public String getName() {
//                return this.name;
//        }
//
//        @Override
//        public String toString() {
//                return "ImageBuilder{" + "name=" + name + ", x_size=" + x_size + ", y_size=" + y_size + ", x_p_size=" + x_p_size + ", y_p_size=" + y_p_size + ", layers=" + layers + '}';
//        }
//
//        public int getX_p_size() {
//                return x_p_size;
//        }
//
//        public int getY_p_size() {
//                return y_p_size;
//        }

        /**
         * This method retunr the last image out
         *
         * @return
         */
//        public BufferedImage getImageOut() {
//                return layers.get(layers.size() - 1).getImage_out();
//        }

//        public float getPixelMmFactor() {
//                return this.designBuilder.getPixelMmFactor();
//        }
//
//        public void refreshPreview(PreviewImageBox preview) {
//                preview.setImageView(this.name, StaticImageEditing.createImageView(this.getImageOut()));
//        }

//        public DesignBuilder getDesignBuilder() {
//                return designBuilder;
//        }
//        
          }

}
