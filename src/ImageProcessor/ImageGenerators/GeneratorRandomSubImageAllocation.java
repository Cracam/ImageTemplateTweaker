
package ImageProcessor.ImageGenerators;

import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerator;
import static ImageProcessor.ImageGenerator.createGenerator;
import ImageProcessor.ImageTransformer;
import static ImageProcessor.ImageTransformer.createTransformer;
import ImageProcessor.Layer;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getDirectChildByTagName;
import static ResourcesManager.XmlManager.getStringAttribute;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorRandomSubImageAllocation extends ImageGenerator {
        
        private ImageGenerator gen;
        
        public GeneratorRandomSubImageAllocation(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }
        

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                               String key;
                Element subElt, subSubElt;
                DesignNode currentUpperDN;

                this.name = getStringAttribute(elt, "name", "ERROR");

                

                
                currentUpperDN = this;
                subElt = getDirectChildByTagName(elt, "Transformers");
                if (subElt != null) {
                        NodeList nodeTransformersList = subElt.getChildNodes();

                        //running in the inverse 
                        for (int i = nodeTransformersList.getLength() - 1; i >= 0; i--) {
                                if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node

                                        subSubElt = (Element) nodeTransformersList.item(i);
                                        key = subSubElt.getNodeName(); // key for defining the layer and the Interface
                                        currentUpperDN = createTransformer(key, currentUpperDN, subSubElt);
                                }
                        }
                }

                ////////
                subElt = getDirectChildByTagName(elt, "Generator");

                if (subElt == null) {
                        throw new XMLErrorInModelException("Le SubLayer " + this.name + "n'a pas de bloc: Generator valide");
                }

                subSubElt = extractSingleElement(subElt.getChildNodes());

                if (subSubElt == null) {
                        throw new XMLErrorInModelException("Le bloc generator du SubLayer " + this.name + "n'a pas de sous générateur valide\n\n " + subElt.getChildNodes().getLength());
                }

                key = subSubElt.getNodeName();

                gen = createGenerator(key, currentUpperDN, subSubElt);//mettre 
               // ((ImageGenerator) currentUpperDN).setDim(this.x_size,this.y_size);
              
        }


        @Override
        public void DRYUpdate() {
                 try {
                        // Getting the right imageGet
                        DesignNode lowerDN = this.getLowerDNForChilds(ImageTransformer.class);
                        if (lowerDN == null) {
                                lowerDN = this.getLowerDNForChilds(ImageGenerator.class);
                        }
                        if (lowerDN == null) {
                                throw new DesingNodeLowerNodeIsAnormalyVoidException("The Layer : " + this.name + " does not have the ImageGenerator");
                        }
                        imageOut = lowerDN.getImageOut();
//                                try {
//                                        File outputfile = new File("C:\\BACKUP\\ENSE3\\Foyer\\Programme_Java\\Batcher_Foyer\\test_data\\saved_image.png");
//                                        ImageIO.write(imageOut, "png", outputfile);
//                                        System.out.println("Image enregistrée avec succès !");
//                                } catch (IOException e) {
//                                        System.err.println("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
//                                }

                } catch (DesingNodeLowerNodeIsAnormalyVoidException ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        
        @Override
        public void setDim(float  x_size,float y_size){
         this.x_size=x_size;
         this.y_size=y_size;
        gen.setDim(x_size,y_size);
         DRYRefreshDPI();
 }
        
  
}
