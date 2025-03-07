package imageBuilder;

import Exceptions.TheXmlElementIsNotANodeException;
import Exceptions.ThisInterfaceDoesNotExistException;
import Exceptions.XMLExeptions.GetAttributeValueException;
import Layers.Layer_old;
import Layers.SubClasses.QuadrupletFloat;

import AppInterface.DesignBuilder;
import interfaces.Interface;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.ImageView;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static staticFunctions.StaticImageEditing.convertToFXImage;

/**
 * The role of this class is to build a image by piling it's differents layer
 * This calls is also in charge of initialising every layer from every types
 *
 * @author LECOURT Camille
 */
public class SubImageBuilder extends ImageBuilder_old{
private SubImageBuilderInterface subInterface;
     
      //  ArrayList<Layer> layers = new ArrayList<>();
    //        ArrayList<Interface> interfaces = new ArrayList<>();

private final float QUALITY_FACTOR=2;
        
 private final BooleanProperty changed = new SimpleBooleanProperty(false);
private ImageView preview;

        
          /**
         * This constructor use
         *
         * @param batcher for reference and subInterface display
         * @param loaderNode
         * @param size_x
         * @param size_y
         * @param upperLayer
         */
        public SubImageBuilder(DesignBuilder batcher, Node loaderNode, float size_x, float size_y,Layer_old upperLayer ) {
                
               super(batcher, loaderNode, size_x*2, size_y*2);
                  this.name = ((Element) loaderNode).getAttribute("name");
               subInterface=new SubImageBuilderInterface(name);
                this.createLayers();
                
                preview= new ImageView();
                preview.setFitHeight(150);
                 preview.setFitWidth(150);
        }
        
        
      

     
        
       @Override
        public void refresh() {
                DRYrefresh();
                setChanged(true);
                preview.setImage(convertToFXImage(this.getImageOut()));
        }
           
        @Override
        public void refreshAll() {
                DRYrefreshAll();
                setChanged(true);
                 preview.setImage(convertToFXImage(this.getImageOut()));
        }

        
        /**
         * This function will create the layer using it's parameter XML it will
         * mainly use the Layer class multi buider for it's work
         */
        @Override
         void createLayers() {
                NodeList nodeLayerList = this.loaderNode.getChildNodes();

                for (int i = 0; i < nodeLayerList.getLength(); i++) {
                        if (nodeLayerList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node
                                try {
                                        Element element = (Element) nodeLayerList.item(i);
                                        String key = element.getNodeName(); // key for defining the layer and the Interface
                                        String nameElement = element.getAttribute("name");

                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        //Create an interface or return it
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        
                                        Interface linkedInterface = this.designBuilder.getInterface(key, nameElement);
                                        if (linkedInterface == null) { // if the interface 
                                                linkedInterface = Interface.createInterface(key, nameElement, this.designBuilder);
                                                 this.designBuilder.addInterface(linkedInterface);
                                                    linkedInterface.desactivatePreview();
                                        }
                                        
                                        if (linkedInterface.isHaveGraphicInterface()) {
                                                     
                                                   //     interfaces.add(linkedInterface);
                                                       subInterface.linkInterface(linkedInterface);
                                       }
                                  

                                        
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // Create a layer
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        float pos_x = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_x").getNodeValue())*QUALITY_FACTOR;
                                        float pos_y = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_y").getNodeValue())*QUALITY_FACTOR;
                                        float size_x = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_x").getNodeValue())*QUALITY_FACTOR;
                                        float size_y = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_y").getNodeValue())*QUALITY_FACTOR;

                                        QuadrupletFloat posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
                                        // System.out.println(key+ nameElement+ this.designBuilder.getModelResources()+ linkedInterface);
                                        Layer_old layerCreated = Layer_old.createLayer(key, nameElement, this.designBuilder.getModelResources(), linkedInterface, this, posSize);

                                        if (layerCreated != null){
                                                System.out.println(layerCreated);
                                                //This code verify if the <Param> element is really an element
                                                Element retElement = (Element) element.getElementsByTagName("Param").item(0);
                                                if (retElement.getNodeType() != Node.ELEMENT_NODE) {
                                                        throw new TheXmlElementIsNotANodeException("IN Layer(2) " + nameElement);
                                                }
                                                layerCreated.readNode(retElement); //read the specific parameter

                                                // add the layer to the layers list
                                                this.layers.add(layerCreated);

                                                linkedInterface.linkNewLayer(layerCreated); //link the layer tothe interface
                                                linkedInterface.linkNewImageBuilder(this);
                                        }

                                } catch (GetAttributeValueException | ThisInterfaceDoesNotExistException e) {
                                        System.out.println(e + " was detected ignoting it");
                                } catch (TheXmlElementIsNotANodeException ex) {
                                        Logger.getLogger(SubImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }
                }
        }
     
        
    
        
         public BooleanProperty  isChanged() {
                return changed;
        }

        public void setChanged(boolean value) {
                        this.changed.set(value);
        }

//        public ArrayList<Interface> getInterfaces() {
//                return interfaces;
//        }

             public SubImageBuilderInterface getSubInterface(){
                     return this.subInterface;
             }
     
             public ImageView getPreview(){
                     return preview;
             }

}
