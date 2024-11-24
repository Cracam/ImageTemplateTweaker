package imageBuilder;

import Exceptions.TheXmlElementIsNotANodeException;
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

/**
 * The role of this class is to build a image by piling it's differents layer
 * This calls is also in charge of initialising every layer from every types
 *
 * @author LECOURT Camille
 */
public class ImageBuilder {

        private final String name; // name the the Image Builder :  "Index of the windows" + "Name given in the model.XML file"
        private final DesignBuilder batcher;

        private final float x_size;
        private final float y_size;

        private int x_p_size;
        private int y_p_size;

        ArrayList<Layer> layers = new ArrayList<>();

        /**
         * This constructor use
         *
         * @param batcher for reference and subInterface display
         * @param imageBuilderNode for the name x_size and y_size variable
         */
        public ImageBuilder(DesignBuilder batcher, Node imageBuilderNode) {
                this.batcher = batcher;

                try {
                        Element element = (Element) imageBuilderNode;
                        if (imageBuilderNode.getNodeType() != Node.ELEMENT_NODE) {
                                this.name = "error";
                                this.x_size = 0;
                                this.y_size = 0;
                                throw new TheXmlElementIsNotANodeException(imageBuilderNode.getNodeName());
                        }
                        this.name = this.batcher.getId() + "_" + element.getAttribute("name");
                        this.x_size = Float.parseFloat(element.getAttribute("size_x"));
                        this.y_size = Float.parseFloat(element.getAttribute("size_y"));

                } catch (TheXmlElementIsNotANodeException ex) {
                        Logger.getLogger(ImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        
                }
                this.computeXY_p_size();
        }

        /**
         * This function retunr the pixel milimeter fator from the batcher
         *
         * @return
         */
        public float get_pixel_mm_Factor() {
                return this.batcher.get_pixel_mm_Factor();
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
                        layers.get(indexBegining + 1).setImage_in(this.name, imgBegining);

                        for (int j = indexBegining + 1; j < layers.size(); j++) {
                                layers.get(j).setImage_in(this.name, layers.get(j - 1).getImage_out(this.name));
                                layers.get(j).computeImage_Out(this.name);
                        }
                }

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

        
        
        
        
//         /**
//          * This function will create all the Layers of the iamge passed by the
//          * XML file
//          *
//          * @param LayersList the list of all node element nessesary for the
//          * layer creation
//          */
//         public void setLayers(List<Node> LayersList) {
//                  this.layers = new Layer[LayersList.size()];
//                  int i = 0;
//                  for (Node node : LayersList) {
//                           if (node.getNodeType() == Node.ELEMENT_NODE) {
//                                    Element element = (Element) node;
//                                    String nodeName = element.getNodeName();
//                                    System.out.println(nodeName);
//                                    String name = element.getAttribute("name");
//
//                                    If we just signa an ditem dispertion or a conso dispertion we dont have to read the pos and size values
//                                    float sizeX = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_x").getNodeValue());
//                                    float sizeY = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_y").getNodeValue());
//
//                                    if (!nodeName.equals("img_Items") && !nodeName.equals("img_Consos")) {
//                                             String name = element.getAttribute("name");
//                                             float posX = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_x").getNodeValue());
//                                             float posY = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_y").getNodeValue());
//
//                                             switch (nodeName) {
//                                                      case "img_fixe": {
//                                                               String pngName = element.getAttribute("png_name");
//                                                               layers[i] = new LayerFixed(name, posX, posY, sizeX, sizeY, pngName);
//                                                               break;
//                                                      }
//                                                      case "img_color": {
//                                                               String pngName = element.getAttribute("png_name");
//                                                               String colorID = element.getElementsByTagName("Color").item(0).getAttributes().getNamedItem("ID").getNodeValue();
//                                                               layers[i] = new LayerColor(name, posX, posY, sizeX, sizeY, pngName, colorID);
//                                                               break;
//                                                      }
//                                                      case "img_custom": {
//                                                               layers[i] = new LayerCustom(name, posX, posY, sizeX, sizeY);
//                                                               break;
//                                                      }
//                                                      case "img_custom_shape": {
//                                                               String color = element.getElementsByTagName("Color").item(0).getAttributes().getNamedItem("color").getNodeValue();
//                                                               layers[i] = new LayerShape(name, posX, posY, sizeX, sizeY, color);
//                                                               break;
//                                                      }
//                                                      default:
//                                                               break;
//                                             }
//                                    } else if (nodeName.equals("img_Consos")) {
//                                             layers[i] = new LayerConsosBuilder("Consos", 0, 0, this.size_x, this.size_y, sizeX, sizeY);
//
//                                    } else if (nodeName.equals("img_Items")) {
//                                             layers[i] = new LayerItemsBuilder("Items", 0, 0, this.size_x, this.size_y, sizeX, sizeY);
//
//                                    }
//
//                                    i++;
//                           }
//                  }
//
//         }
        public String getName() {
                return name;
        }

}
