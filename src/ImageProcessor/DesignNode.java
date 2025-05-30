package ImageProcessor;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import Exceptions.InvalidLinkbetweenNode;
import Exceptions.ThisInterfaceDoesNotExistException;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ResourcesManager.ResourcesManager;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class DesignNode extends VBox {

        private  ArrayList<DesignNode> upperDN;
        protected String name;
        private ArrayList<DesignNode> lowersDN;
        protected BufferedImage imageOut;
        protected InterfaceNode linkedinterface;
        protected Element elt;

        // Constructeur prenant un seul upperDN
        public DesignNode(DesignNode upperDE, Element eltConstruct) throws XMLErrorInModelException {
                this.upperDN = new ArrayList<>();
                this.lowersDN = new ArrayList<>();
                if (upperDE != null) {
                        this.upperDN.add(upperDE);
                        upperDE.addLowerDN(this);
                }
                this.elt = eltConstruct;
        }

        // Constructeur prenant une ArrayList d'upperDN
        public DesignNode(ArrayList<DesignNode> upperDEs, Element eltConstruct) throws XMLErrorInModelException {
                this.upperDN = new ArrayList<>(upperDEs);
                this.lowersDN = new ArrayList<>();
                for (DesignNode upperDE : upperDEs) {
                        if (upperDE != null) {
                                upperDE.addLowerDN(this);
                        }
                }
                this.elt = eltConstruct;
        }

        protected abstract void generateFromElement() throws XMLErrorInModelException;

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

        /**
         * Thes update program consserning the element
         */
        public abstract void DRYRefreshDPI();
        
        /**
         * This update all the lower elements
         */
        public void  RefreshDPI() {
                DRYRefreshDPI();
                DRYUpdate();
                for (DesignNode upper : upperDN) {
                        upper.RefreshDPI();
                }
        }
        
        
        /**
         * This update the size of all the element and after refresh everything
         * 
         * refresh from the botom
         */
        public void refreshDPIFromDown() {
                if (lowersDN.isEmpty()) {
                        RefreshDPI();
                } else {
                        for (DesignNode lower : lowersDN) {
                                lower.refreshDPIFromDown();
                        }
                }
        }
        
        /**
         * Update from down
         */
        public void updateFromDown() {
                if (lowersDN.isEmpty()) {
                        //        System.out.println("TTTTTTTTTTT : "+this.getClass());
                        update();

                } else {
                        for (DesignNode lower : lowersDN) {
                                lower.updateFromDown();
                        }
                }
        }

        public void addLowerDN(DesignNode lowerDN) {
                if (this.lowersDN == null) {
                        this.lowersDN = new ArrayList<>();
                }
                this.lowersDN.add(lowerDN);
        }
        
               public void addUpperDN(DesignNode upperDN) {
                if (this.upperDN == null) {
                        this.upperDN = new ArrayList<>();
                }
                this.upperDN.add(upperDN);
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
         * @param <T>
         * @param nodeClass
         * @return
         */
        public <T extends DesignNode> T getLowerDN(Class<T> nodeClass) {
                if (lowersDN == null) {
                        return null;
                }
                for (DesignNode designNode : lowersDN) {
                        if (designNode.getClass() == nodeClass) {
                                return (T) designNode;
                        }
                }
                return null; // Retourne null si aucun élément correspondant n'est trouvé
        }

        public <T extends DesignNode> T getUpperDN(Class<T> nodeClass) {
                if (upperDN == null) {
                        //          System.out.println("### case 1");
                        return null;
                }

                for (DesignNode upper : upperDN) {
                        if (upper.getClass() == nodeClass) {
                                return (T) upper;
                        } else {
                                T result = upper.getUpperDN(nodeClass);
                                if (result != null) {
                                        return result;
                                }
                        }
                }
                return null;
        }

        /**
         * this return all the class that are sub classes of the entry
         *
         * @param <T>
         * @param nodeClass
         * @return
         */
        public <T extends DesignNode> T getLowerDNForChilds(Class<T> nodeClass) {
                if (lowersDN == null) {
                        return null;
                }
                for (DesignNode designNode : lowersDN) {
                        if (nodeClass.isAssignableFrom(designNode.getClass())) {
                                return (T) designNode;
                        }
                }
                return null; // Retourne null si aucun élément correspondant n'est trouvé
        }

        /**
         * this return all the class that are sub classes of the entry
         *
         * @param <T>
         * @param nodeClass
         * @return
         */
        public <T extends DesignNode> T getUpperDNForChilds(Class<T> nodeClass) {
                if (upperDN == null) {
                        // System.out.println("### case 1");
                        return null;
                }

                for (DesignNode upper : upperDN) {
                        if (nodeClass.isAssignableFrom(upper.getClass())) {
                                return (T) upper;
                        } else {
                                T result = upper.getUpperDN(nodeClass);
                                if (result != null) {
                                        return (T) result;
                                }
                        }
                }
                return null;
        }

        public <T extends DesignNode> T getUpperOrHimselfDN(Class<T> nodeClass) {
                if (this.getClass() == nodeClass) {
                        return (T) this;
                }
                return this.getUpperDN(nodeClass);
        }

        public InterfaceNode createLinkedInterface(InterfaceNode upperInter) {

                try {

                        Class<? extends InterfaceNode> subclass = DesignInterfaceLinker.getLinkedInterface(this.getClass());
                        if (subclass == null || subclass.isAssignableFrom(InterfaceNode.class)) {
                                throw new ThisInterfaceDoesNotExistException("this interface node dont exist yet for " + this.getClass().getName());
                        }
                        Constructor<? extends InterfaceNode> constructor = subclass.getConstructor(InterfaceNode.class, String.class);
                        constructor.newInstance(upperInter, "").addDesignNode(this);//Set the link between the interface and the layer in both directions
                        return linkedinterface;

                } catch (ThisInterfaceDoesNotExistException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(InterfaceNode.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace(); // Print the stack trace
                        System.out.println("ERROR for " + this.getClass().getName());
                        return null;
                }
        }

        public void setLinkedInterface(InterfaceNode node) {
                linkedinterface = node;
        }

        public InterfaceNode createInterfaceTreeFromNodeTree(InterfaceNode upperIN, Class<?> stopClass) {
                InterfaceNode InterfaceRoot = this.createLinkedInterface(upperIN);
                //   System.out.println("CCCCCCCCCCC---- :  "+InterfaceRoot.getClass().getName());
                for (DesignNode lowerDN : lowersDN) {
                        if (stopClass != lowerDN.getClass()) {
                                lowerDN.createInterfaceTreeFromNodeTree(InterfaceRoot, stopClass);
                        }
                }
                return InterfaceRoot;
        }

        public InterfaceNode createInterfaceTreeFromNodeTree(InterfaceNode upperIN, List<Class<?>> stopClasses) {
                InterfaceNode InterfaceRoot = this.createLinkedInterface(upperIN);
                //   System.out.println("CCCCCCCCCCC---- :  "+InterfaceRoot.getClass().getName());
                for (DesignNode lowerDN : lowersDN) {
                        if (!stopClasses.contains(lowerDN.getClass())) {
                                lowerDN.createInterfaceTreeFromNodeTree(InterfaceRoot, stopClasses);
                        }
                }
                return InterfaceRoot;
        }
        
        public InterfaceNode createInterfaceTreeFromNodeTree(InterfaceNode upperIN) {
                return createInterfaceTreeFromNodeTree(upperIN, (Class) null);
        }

        /**
         * This program take as root a interface node and link the corresponding
         * InterfaceNode
         *
         * @param interNode
         * @throws InvalidLinkbetweenNode
         */
        public void linkDesignNodeToInterfaceNodes(InterfaceNode interNode) throws InvalidLinkbetweenNode {
                if (DesignInterfaceLinker.getLinkedInterface(this.getClass()) != interNode.getClass()) {
                        throw new InvalidLinkbetweenNode("the DesignNode of class : " + this.getClass().getName() + " is not compatible with this InterfaceNOde : " + interNode.getClass().getName());
                }
                interNode.addDesignNode(this);

                ArrayList<InterfaceNode> subInterfaceNodeList = interNode.getLowerInterfaces();
                if (subInterfaceNodeList.size() != getSizeExcludingClass(lowersDN,Layer.class)) {
       
                        for (DesignNode dn : lowersDN) {
                                System.out.println("################" + dn.getClass().getName());
                        }
                        for (InterfaceNode dn : interNode.getLowerInterfaces()) {
                                System.out.println("#########---#######" + dn.getClass().getName());
                        }
                        throw new InvalidLinkbetweenNode("The number of sub DesignNode ( " + getSizeExcludingClass(lowersDN, Layer.class) + " ) and  InterfaceNode ( " + interNode.getLowerInterfaces().size() + " ) are not equal");
                }

                for (int i = 0; i < subInterfaceNodeList.size(); i++) {
                        if (lowersDN.get(i).getClass() != Layer.class ){
                                lowersDN.get(i).linkDesignNodeToInterfaceNodes(subInterfaceNodeList.get(i));
                        }
                }
        }

        public static int getSizeExcludingClass(ArrayList<?> list, Class<?> excludeClass) {
                int count = 0;
                for (Object element : list) {
                        if (element != null && !excludeClass.isInstance(element)) {
                                count++;
                        }
                }
                return count;
        }


        public String ComputeUniqueID(Class<?> stopClass) {
                String ret = this.DRYComputeUniqueID();
                for (DesignNode lowerDN : lowersDN) {
                        if (stopClass != lowerDN.getClass()) {
                                // System.out.println("------------- Intermadiate calculation : "+ lowerDN.ComputeUniqueID(stopClass));
                                ret = ret + lowerDN.ComputeUniqueID(stopClass);
                        }
                }
                return ret;
        }

        public String DRYComputeUniqueID() {
                String ret = DesignInterfaceLinker.getIdentifier(this.getClass());
                if (ret == null) {
                        return "";
                }
                return ret;
        }

        public ResourcesManager getModelRessources() {
                return ((ImageBuilder) this.getUpperDN(ImageBuilder.class)).getDesignBuilder().getModelResources();
        }

        public ArrayList<DesignNode> getAllDirectLowerDN() {
                return this.lowersDN;
        }

        public InterfaceNode getLinkedinterface() {
                return linkedinterface;
        }

        @Override
        public String toString() {
                return toString("");
        }

        public String toString(String deb) {
                deb = deb + this.DRYtoString();
                for (DesignNode lInter : lowersDN) {
                        deb = "\n" + deb + lInter.getClass().getName() + "   " + lInter.toString();
                }
                return deb;
        }

        protected abstract String DRYtoString();

        /**
         * Récupère tous les nœuds inférieurs d'un certain type T.
         *
         * @param <T> Le type des nœuds à récupérer.
         * @param nodeClass La classe des nœuds à récupérer.
         * @return Une liste de nœuds de type T.
         */
        public <T extends DesignNode> ArrayList<T> getAllLowerDNOff(Class<T> nodeClass) {
                ArrayList<T> result = new ArrayList<>();
                if (lowersDN != null) {
                        for (DesignNode designNode : lowersDN) {
                                if (nodeClass.isInstance(designNode)) {
                                        result.add(nodeClass.cast(designNode));
                                }
                                result.addAll(designNode.getAllLowerDNOff(nodeClass));
                        }
                }
                return result;
        }

        public void delete(DesignNode DesignNodeToDelete) {
                if (lowersDN.contains(DesignNodeToDelete)) {
                        lowersDN.remove(DesignNodeToDelete);
                        System.out.println("DesignNodeToDelete a été supprimé de lowersDN.");
                } else {
                        System.out.println("DesignNodeToDelete n'est pas présent dans lowersDN.");
                }
        }

        public void destroyItSelf() {
                for (DesignNode designNode : upperDN) {
                        designNode.delete(this);
                }
        }

        public ArrayList<DesignNode> getLowersDN() {
                return lowersDN;
        }

        protected ArrayList<DesignNode> getLowestDN(ArrayList<DesignNode> DNs) {
                if (this.lowersDN.isEmpty()) {
                        DNs.add(this);
                        return DNs;
                }

                for (DesignNode DN : lowersDN) {
                        DNs = DN.getLowestDN(DNs);
                }
                return DNs;
        }

        public ArrayList<DesignNode> getLowestDN() {
                ArrayList<DesignNode> DNs = new ArrayList<>();
                return getLowestDN(DNs);
        }

        public String getName() {
                return name;
        }

}
