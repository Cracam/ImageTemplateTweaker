/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageBuilder;

import Layers.Layer;

import designBuilder.DesignBuilder;
import java.util.List;
import javafx.scene.image.ImageView;
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
         private final float size_x;
         private final float size_y;
         

         private Layer[] layers;

         

         public ImageBuilder(DesignBuilder batcher,String name, float size_x, float size_y) {
                  this.batcher=batcher;
                  this.name=this.batcher.getIndex()+"_"+name;

                  this.size_x = size_x;
                  this.size_y = size_y;
         }



         public float get_pixel_mm_Factor() {
                  return (float) ((float) (this.batcher.getDPI() )/ 24.5);
         }
         //need to have a static list of active layer (on one interface)
         
         
         
         
         

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
