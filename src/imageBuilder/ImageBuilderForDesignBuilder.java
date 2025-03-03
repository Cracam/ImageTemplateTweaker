package imageBuilder;

import Exceptions.TheXmlElementIsNotANodeException;
import Exceptions.ThisInterfaceDoesNotExistException;
import Exceptions.ThisLayerDoesNotExistException;
import Layers.Layer;
import Layers.SubClasses.QuadrupletFloat;

import designBuilder.DesignBuilder;
import interfaces.Interface;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import previewimagebox.PreviewImageBox;
import staticFunctions.StaticImageEditing;

/**
 * The role of this class is to build a image by piling it's differents layer
 * This calls is also in charge of initialising every layer from every types
 *
 * @author LECOURT Camille
 */
public class ImageBuilderForDesignBuilder extends ImageBuilder {

        Node loaderNode;

        /**
         * This constructor use
         *
         * @param batcher for reference and subInterface display
         * @param loaderNode
         */
        public ImageBuilderForDesignBuilder(DesignBuilder batcher, Node loaderNode) {
                String nameC = "";
                float x_sizeC = 0;
                float y_sizeC = 0;

                try {
                        Element element = (Element) loaderNode;
                        if (loaderNode.getNodeType() != Node.ELEMENT_NODE) {
                                throw new TheXmlElementIsNotANodeException("IN ImageBuilder" + loaderNode.getNodeName());
                        }
                        nameC = batcher.getId() + "_" + element.getAttribute("name");
                        x_sizeC = Float.parseFloat(element.getAttribute("size_x"));
                        y_sizeC = Float.parseFloat(element.getAttribute("size_y"));

                } catch (TheXmlElementIsNotANodeException ex) {
                        Logger.getLogger(ImageBuilderForDesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }

                super(batcher, nameC, x_sizeC, y_sizeC);
                this.loaderNode = loaderNode;

                this.computeXY_p_size();
                this.createLayers();

        }

        /**
         * This function will create the layer using it's parameter XML it will
         * mainly use the Layer class multi buider for it's work
         */
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
                                        if (linkedInterface == null) {
                                                linkedInterface = Interface.createInterface(key, nameElement, this.designBuilder);
                                                this.designBuilder.addInterface(linkedInterface);
                                                if (linkedInterface.isHaveGraphicInterface()) {
                                                        String tabname = element.getAttribute("tab_name");
                                                        if (tabname == null) {
                                                                tabname = "Defaut";
                                                        }
                                                        this.designBuilder.assignInterfaceToTab(tabname, linkedInterface);

                                                }
                                        }
                                        linkedInterface.activatePreview();

                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // Create a layer
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                                        float pos_x = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_x").getNodeValue());
                                        float pos_y = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_y").getNodeValue());
                                        float size_x = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_x").getNodeValue());
                                        float size_y = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_y").getNodeValue());

                                        QuadrupletFloat posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
                                        // System.out.println(key+ nameElement+ this.designBuilder.getModelResources()+ linkedInterface);
                                        Layer layerCreated = Layer.createLayer(key, nameElement, this.designBuilder.getModelResources(), linkedInterface, this, posSize);

                                        if (!(layerCreated == null)) {
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

                                } catch (ThisLayerDoesNotExistException | ThisInterfaceDoesNotExistException e) {
                                        System.out.println(e + " was detected ignoting it");
                                } catch (TheXmlElementIsNotANodeException ex) {
                                        Logger.getLogger(ImageBuilderForDesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }
                }
        }

        @Override
        public void refreshPreview(PreviewImageBox preview) {
                preview.setImageView(this.name, StaticImageEditing.createImageView(this.getImageOut()));
        }

}
