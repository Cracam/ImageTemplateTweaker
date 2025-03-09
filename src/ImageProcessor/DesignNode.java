package ImageProcessor;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import Exceptions.InvalidLinkbetweenNode;
import Exceptions.XMLExeptions.GetAttributeValueException;
import ResourcesManager.ResourcesManager;
import interfaces.Interface;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class DesignNode {

        private final ArrayList<DesignNode> upperDN;
        protected String name;
        private ArrayList<DesignNode> lowersDN;
       protected BufferedImage imageOut;
       protected InterfaceNode linkedinterface;

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
        
        
        /**
         * Thes update program consserning the element
         */
        public abstract void DRYRefreshDPI();

        /**
         * This update the size of all the element and after refresh everything
         */
        public void RefreshDPI() {
                DRYRefreshDPI();
                for (DesignNode lower : lowersDN) {
                        lower.RefreshDPI();
                }
                update();
        }
        

        public void updateLower() {
                for (DesignNode lower : lowersDN) {
                        lower.updateLower();
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
        
        
        public  InterfaceNode createLinkedInterface(InterfaceNode upperInter){
                  

                try {

                        Class<? extends InterfaceNode> subclass = DesignInterfaceLinker.getLinkedInterface(this.getClass());
                        Constructor<? extends InterfaceNode> constructor = subclass.getConstructor(InterfaceNode.class);

                        return constructor.newInstance(upperInter);

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace(); // Print the stack trace

                        return null;
                }
        }
        
        
        
        
  
        
        
        
         public  InterfaceNode createInterfaceTreeFromNodeTree(InterfaceNode upperIN){
                 InterfaceNode InterfaceRoot  = this.createLinkedInterface(upperIN);
                 for (DesignNode lowerDN : lowersDN) {
                        lowerDN.createInterfaceTreeFromNodeTree(InterfaceRoot);
                 }
                 return InterfaceRoot;
        }
          
          /**
           * This program take as root a desing node and create the corresponding InterfaceNode
           * @param interNode
           * @throws InvalidLinkbetweenNode 
           */
         public  void linkDesignNodeToInterfaceNodes(InterfaceNode interNode) throws InvalidLinkbetweenNode{
                  if(DesignInterfaceLinker.getLinkedInterface(this.getClass())!=interNode.getClass()){
                          throw new InvalidLinkbetweenNode("the DesignNode of class : "+this.getClass().getName()+" is not compatible with this InterfaceNOde : "+interNode.getClass().getName());
                  }
                interNode.addDesignNode(this);
                
                ArrayList<InterfaceNode> subInterfaceNodeList = interNode.getLowerInterfaces();
                if(subInterfaceNodeList.size()!=lowersDN.size()){
                        throw new InvalidLinkbetweenNode("The number of sub DesignNode ( "+lowersDN.size()+" ) and  InterfaceNode ( "+interNode.getLowerInterfaces().size()+" ) are not equal");
                }
                
                for(int i=0 ; i<subInterfaceNodeList.size();i++){
                        lowersDN.get(i).linkDesignNodeToInterfaceNodes(subInterfaceNodeList.get(i));
                }
                
                
          }
          
          
          
             public String ComputeUniqueID() {
                String ret = "";
                this.DRYComputeUniqueID();
                for (DesignNode lInter : lowersDN) {
                        ret = ret + lInter.ComputeUniqueID();
                }
                return ret;
        }
        
        public  String DRYComputeUniqueID(){
               return  DesignInterfaceLinker.getIdentifier(this.getClass());
        }

          
          
          public ResourcesManager getModelRessources(){
                 return  ((ImageBuilder)this.getUpperDN(ImageBuilder.class)).getDesignBuilder().getModelResources();
          }

}
          
        
