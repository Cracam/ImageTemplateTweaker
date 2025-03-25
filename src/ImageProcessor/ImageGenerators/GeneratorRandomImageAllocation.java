package ImageProcessor.ImageGenerators;

import AppInterface.InterfaceNode;
import AppInterface.Interfaces.InterfaceRandomImageAllocation;
import AppInterface.Interfaces.InterfaceRandomSubImageAllocation;
import AppInterface.LayerContainer;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import ImageProcessor.ImageGenerator;
import static ImageProcessor.ImageGenerator.createGenerator;
import static ImageProcessor.ImageTransformer.createTransformer;
import ImageProcessor.ImagesTransformers.TransformerInert;
import ImageProcessor.SubClasses.QuadrupletFloat;
import ImageProcessor.SubClasses.QuadrupletInt;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getDirectChildByTagName;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.SpinnerValueFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static staticFunctions.StaticImageEditing.ResizeImage;
import static staticFunctions.StaticImageEditing.createBufferedImage;
import static staticFunctions.StaticImageEditing.overlayImages;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorRandomImageAllocation extends ImageGenerator {

        private ArrayList<QuadrupletFloat> positions;
        private float maxSizeX;
        private float maxSizeY;

        private ArrayList<QuadrupletInt> pixelPositions;

        private Element interfaceLoaderElement; //the element info to load every 

        private int minNumber;
        private int maxNumber;
        private int defaultNumber;

        private DesignNode commonDN;
        private ImageGenerator gen;

        public GeneratorRandomImageAllocation(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                positions = new ArrayList<>();
                pixelPositions = new ArrayList<>();

                Element subElt = extractSingleElement(elt.getElementsByTagName("Param"));
                minNumber = XmlManager.getIntAttribute(subElt, "minNumber", 1);
                maxNumber = XmlManager.getIntAttribute(subElt, "maxNumber", 20);
                defaultNumber = XmlManager.getIntAttribute(subElt, "defaultNumber", 5);

//Loading each positions
                subElt = extractSingleElement(elt.getElementsByTagName("PosSizes"));

                NodeList subElements = subElt.getElementsByTagName("PosSize");
                float pos_x, pos_y, size_x, size_y;
                Element subElement;

                for (int i = 0; i < subElements.getLength(); i++) {
                        subElement = (Element) subElements.item(i);
                        pos_x = XmlManager.getFloatAttribute(subElement, "pos_x", 0);
                        pos_y = XmlManager.getFloatAttribute(subElement, "pos_y", 0);
                        size_x = XmlManager.getFloatAttribute(subElement, "size_x", 0);
                        size_y = XmlManager.getFloatAttribute(subElement, "size_y", 0);
                        positions.add(new QuadrupletFloat(pos_x, pos_y, size_x, size_y));

                        if (maxSizeX < size_x) {
                                maxSizeX = size_x;
                        }
                        if (maxSizeY < size_y) {
                                maxSizeY = size_y;
                        }

                }

                subElt = extractSingleElement(elt.getElementsByTagName("Common_Componments"));
                Element subSubSubElt;

                if (subElt != null) {
                        DesignNode currentUpperDN = new TransformerInert(null, null);
                        commonDN = currentUpperDN;

                        Element subSubElt = extractSingleElement(subElt.getElementsByTagName("Generator"));
                        if (subSubElt != null) {

                                subSubElt = getDirectChildByTagName(subElt, "Transformers");
                                if (subSubElt != null) {
                                        NodeList nodeTransformersList = subSubElt.getChildNodes();

                                        //running in the inverse 
                                        for (int i = nodeTransformersList.getLength() - 1; i >= 0; i--) {
                                                if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node

                                                        subSubSubElt = (Element) nodeTransformersList.item(i);
                                                        String key = subSubSubElt.getNodeName(); // key for defining the layer and the Interface
                                                        currentUpperDN = createTransformer(key, currentUpperDN, subSubSubElt);
                                                }

                                        }
                                }

                                ////////
                                subSubElt = getDirectChildByTagName(subElt, "Generator");

                                if (subSubElt == null) {
                                        throw new XMLErrorInModelException("Le Layer " + this.name + "n'a pas de bloc: Generator valide");
                                }

                                subSubSubElt = extractSingleElement(subSubElt.getChildNodes());

                                if (subSubElt == null) {
                                        throw new XMLErrorInModelException("Le bloc generator du Layer " + this.name + "n'a pas de sous générateur valide\n\n " + subSubElt.getChildNodes().getLength());
                                }

                                String key = subSubSubElt.getNodeName();

                                gen = createGenerator(key, currentUpperDN, subSubSubElt);//mettre 
                                
                              
                        }
                }
                interfaceLoaderElement = extractSingleElement(elt.getElementsByTagName("SubLayer"));
        }
        
        
        

        @Override
        public void DRYUpdate() {
                ArrayList<DesignNode> DNs = this.getLowersDN();
                ArrayList<DesignNode> DNsWeighted = new ArrayList<>(DNs);
                BufferedImage imageGet = createBufferedImage(this.x_p_size, this.y_p_size);

                // Add every element to a weighted list (to be able to count them)
                if (!DNs.isEmpty()) {
                        for (DesignNode DN : DNs) {
                                int weight = ((InterfaceRandomSubImageAllocation) DN.getLinkedinterface()).getNumberSelectorValue()-1;
                                for (int i = 0; i < weight; i++) {
                                        DNsWeighted.add(DN);
                                }
                        }

                        // Find an image for each position until all the positions or all the weighted list is empty
                        BufferedImage itemImage;
                        Random random = this.getUpperDN(ImageBuilder.class).getDesignBuilder().getRandom();
                        Iterator<QuadrupletInt> pixelPositionIterator = pixelPositions.iterator();

                        while (pixelPositionIterator.hasNext()) {
                                QuadrupletInt pixelPosSize = pixelPositionIterator.next();

                                if (DNsWeighted.isEmpty()) {
                                        System.out.println("Pas Assez d'éléments pour la dispersion d'éléments suivant : " + this.name);
                                        break;
                                }

                                int randomIndex = random.nextInt(DNsWeighted.size());
                                itemImage = DNsWeighted.get(randomIndex).getImageOut();
                                DNsWeighted.remove(randomIndex); // Remove from the list

                                itemImage = ResizeImage(itemImage, pixelPosSize.getSize_x(), pixelPosSize.getSize_y());
                                imageGet = overlayImages(imageGet, itemImage, pixelPosSize.getPos_x(), pixelPosSize.getPos_y());
                        }
                        this.imageOut = imageGet;
                }
        }

        @Override
        public void DRYRefreshDPI() {
                float factor = ((ImageBuilder) getUpperOrHimselfDN(ImageBuilder.class)).getDesignBuilder().getPixelMmFactor();
                System.out.println("taille du facteur" + factor);
                this.x_p_size = (int) (this.x_size * factor);
                this.y_p_size = (int) (this.y_size * factor);
                this.imageOut = new BufferedImage(x_p_size, x_p_size, BufferedImage.TYPE_INT_ARGB);
                pixelPositions.clear();

                for (QuadrupletFloat posSize : positions) {
                        pixelPositions.add(new QuadrupletInt(posSize, factor));
                }

        }

        public GeneratorRandomSubImageAllocation createSubImageAllocationBuilder() {
                try {
                        GeneratorRandomSubImageAllocation DN = new GeneratorRandomSubImageAllocation(this, interfaceLoaderElement, commonDN);

                        ((ImageGenerator) DN).setDim(maxSizeX, maxSizeY);
                        if (gen != null) {
                                gen.setDim(maxSizeX, maxSizeY);
                        }

                        return DN;
                } catch (XMLErrorInModelException ex) {
                        Logger.getLogger(GeneratorRandomImageAllocation.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
        }

        public SpinnerValueFactory.IntegerSpinnerValueFactory getSpinnerFactory() {
                return new SpinnerValueFactory.IntegerSpinnerValueFactory(minNumber, maxNumber, defaultNumber);
        }
 
 
     @Override
        public InterfaceNode createLinkedInterface(InterfaceNode upperInter) {
                InterfaceNode inter = new InterfaceRandomImageAllocation(upperInter, name);
                inter.addDesignNode(this);
                
                if(commonDN!=null){
                        ((InterfaceRandomImageAllocation) inter).createCommomInterface(commonDN);
                }

                return inter;
        }
}
