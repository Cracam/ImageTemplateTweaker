package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class DesignNode {

        private final ArrayList<DesignNode> upperDN;
        protected String name;
        private ArrayList<DesignNode> lowersDN;
        BufferedImage imageOut;

        // Constructeur prenant un seul upperDN
        public DesignNode(DesignNode upperDE, Element elt) throws GetAttributeValueException {
                this.upperDN = new ArrayList<>();
                if (upperDE != null) {
                        this.upperDN.add(upperDE);
                        upperDE.addLowerDN(this);
                }
                generateFromElement(elt);
        }

        // Constructeur prenant une ArrayList d'upperDN
        public DesignNode(ArrayList<DesignNode> upperDEs, Element elt) throws GetAttributeValueException {
                this.upperDN = new ArrayList<>(upperDEs);
                for (DesignNode upperDE : upperDEs) {
                        if (upperDE != null) {
                                upperDE.addLowerDN(this);
                        }
                }
                generateFromElement(elt);
        }

        abstract void generateFromElement(Element elt) throws GetAttributeValueException;

        /**
         * Thes update program consserning the element
         */
        public abstract void DRYUpdate();

        /**
         * This update all the lower elements
         */
        public void update() {
                DRYUpdate();
                for (DesignNode upper : upperDN) {
                        upper.update();
                }
        }

        public void updateLower() {
                for (DesignNode designNode : lowersDN) {
                        designNode.updateLower();
                }
        }

        public void addLowerDN(DesignNode lowerDN) {
                if (this.lowersDN == null) {
                        this.lowersDN = new ArrayList<>();
                }
                this.lowersDN.add(lowerDN);
        }

        public BufferedImage getImageOut() {
                return imageOut;
        }

        public void setImageOut(BufferedImage imageOut) {
                this.imageOut = imageOut;
        }

        /**
         * Return the first class of the
         *
         * @param nodeClass
         * @return
         */
        public DesignNode getLowerDN(Class<?> nodeClass) {
                if (lowersDN == null) {
                        return null;
                }
                for (DesignNode designNode : lowersDN) {
                        if (designNode.getClass() == nodeClass) {
                                return designNode;
                        }
                }
                return null; // Retourne null si aucun élément correspondant n'est trouvé
        }

        public DesignNode getUpperDN(Class<?> nodeClass) {
                if (upperDN == null) {
                        return null;
                }

                for (DesignNode upper : upperDN) {
                        if (upper.getClass() == nodeClass) {
                                return upper;
                        } else {
                                DesignNode result = upper.getUpperDN(nodeClass);
                                if (result != null) {
                                        return result;
                                }
                        }
                }
                return null;
        }

        public DesignNode getUpperOrHimselfDN(Class<?> nodeClass) {
                if(this.getClass()==nodeClass) return this;
                return this.getUpperDN(nodeClass);
        }
}
