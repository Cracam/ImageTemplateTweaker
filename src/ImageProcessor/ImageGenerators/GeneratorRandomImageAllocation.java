package ImageProcessor.ImageGenerators;

import AppInterface.Interfaces.InterfaceRandomSubImageAllocation;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import ImageProcessor.ImageGenerator;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.extractSingleElement;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.SpinnerValueFactory;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
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

                //  System.out.println("THE RANDOM ALLOCATION is CREATED : MaxSIzeX : "+maxSizeX+"  MAx sizeY : "+maxSizeY);
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
                                int weight = ((InterfaceRandomSubImageAllocation) DN.getLinkedinterface()).getNumberSelectorValue();
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

//                                System.out.println("printed at : " + pixelPosSize.toString());
//                                try {
//                                        File outputfile = new File("C:\\BACKUP\\ENSE3\\Foyer\\Programme_Java\\Batcher_Foyer\\test_data\\saved_image.png");
//                                        ImageIO.write(itemImage, "png", outputfile);
//                                        System.out.println("Image enregistrée avec succès !");
//                                } catch (IOException e) {
//                                        System.err.println("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
//                                }
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
                        GeneratorRandomSubImageAllocation DN = createGenerator(GeneratorRandomSubImageAllocation.class, this, interfaceLoaderElement);
                        DN.setDim(this.x_size, this.y_size);
                        return DN;
                } catch (XMLErrorInModelException ex) {
                        Logger.getLogger(GeneratorRandomImageAllocation.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
        }

        public SpinnerValueFactory.IntegerSpinnerValueFactory getSpinnerFactory() {
                return new SpinnerValueFactory.IntegerSpinnerValueFactory(minNumber, maxNumber, defaultNumber);
        }

}
