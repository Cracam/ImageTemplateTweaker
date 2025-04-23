package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceContainer;
import Exeptions.ResourcesFileErrorException;
import AppInterface.InterfaceNode;
import Exceptions.InvalidLinkbetweenNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerators.GeneratorRandomImageAllocation;
import ImageProcessor.ImageGenerators.GeneratorRandomSubImageAllocation;
import ImageProcessor.ImagesTransformers.TransformerInert;
import ResourcesManager.XmlChild;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceRandomImageAllocation extends InterfaceContainer {

        @FXML
        private Label MinimumNumberLabel;

        @FXML
        private VBox SubInterfaceContainer;

        @FXML
        private VBox CommonContainer;

        private InterfaceNode commonInterface;

        public InterfaceRandomImageAllocation(InterfaceNode upperIN, String name) {
                super(upperIN, name);

                setContainerVBox(SubInterfaceContainer);
                 if(upperIN!=null){
                          upperInterface.placeInterface(this);
                }
        }

        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceRandomAllocation.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @FXML
        public void uptadeInterface() {
                this.updateLinkedDesignNodes();
        }

        /**
         * this function create a new subimageBuilder
         */
        @FXML
        public void createNewSubImgBuilder() {

                // Exécuter le code sur le premier élément
                DesignNode DN = linkedDesignNodes.get(0);
                GeneratorRandomImageAllocation imgAll = (GeneratorRandomImageAllocation) DN;
                GeneratorRandomSubImageAllocation subDN = imgAll.createSubImageAllocationBuilder();

                InterfaceRandomSubImageAllocation newInter = (InterfaceRandomSubImageAllocation) subDN.createInterfaceTreeFromNodeTree(this, TransformerInert.class);

                for (int i = 1; i < linkedDesignNodes.size(); i++) {
                        try {
                                DN = linkedDesignNodes.get(i);
                                imgAll = (GeneratorRandomImageAllocation) DN;
                                subDN = imgAll.createSubImageAllocationBuilder();
                                subDN.linkDesignNodeToInterfaceNodes(newInter);
                        } catch (InvalidLinkbetweenNode ex) {
                                Logger.getLogger(InterfaceRandomImageAllocation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                //  this.updateLinkedDesignNodes();
                DN.updateLower();
                uptadeInterface();
        }

        public void createCommomInterface(DesignNode DN) {
                this.setContainerVBox(CommonContainer);
                commonInterface = DN.createInterfaceTreeFromNodeTree(this);
                this.setContainerVBox(SubInterfaceContainer);
                // CommonContainer.getChildren().add(interNode);
        }

        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {

                //we ,ot load anything but we count to add or delete the right number of subRandom interface
                int numberOfSubAllocator = countElementsByName(element, DesignInterfaceLinker.getIdentifier(InterfaceRandomSubImageAllocation.class));

                if (this.getLowerInterfaces().size() > numberOfSubAllocator) {
                        int numberToDelete = numberOfSubAllocator - this.getLowerInterfaces().size();

                        for (int i = 0; i < numberToDelete; i++) {
                                ((InterfaceRandomSubImageAllocation) this.getLowerInterfaces().getLast()).deleteThis();
                        }
                }

                if (this.getLowerInterfaces().size() < numberOfSubAllocator) {
                        int numberToCreate = this.getLowerInterfaces().size() - numberOfSubAllocator;

                        for (int i = 0; i < numberToCreate; i++) {
                                this.createNewSubImgBuilder();
                        }
                }
        }

        public static int countElementsByName(Element parentElement, String elementName) {
                NodeList childNodeList = parentElement.getElementsByTagName(elementName);
                return childNodeList.getLength();
        }

        @Override
        public XmlChild DRYsaveDesign() {
                XmlChild Xmloffset = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
                return Xmloffset;
        }

        /**
         * those modifation are needed to have a unique identifier not depending
         * on the number of subinterface
         *
         * @return
         */
        public String ComputeUniqueID() {
                String ret = this.DRYComputeUniqueID();
                int nbInterToID;
                if (commonInterface != null) {
                        nbInterToID = 2;
                } else {
                        nbInterToID = 1;
                }

                for (int i = 0; i < nbInterToID; i++) {
                        InterfaceNode lInter = this.getLowerInterfaces().get(i);
                        if (lInter != null) {
                                ret = ret + lInter.ComputeUniqueID();
                        }
                }

                return ret;
        }
        
        @Override
        public  String DRYConcatenateSubElementNames(Element rootElement) {
                NodeList childNodes = rootElement.getChildNodes();
                boolean foundFirst = false;
                StringBuilder concatenatedNames = new StringBuilder();

                // Vérifie si le premier élément est "G_Random_Image_Allocation"
                for (int i = 0; i < childNodes.getLength(); i++) {
                        Node childNode = childNodes.item(i);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                if ("G_Random_Image_Allocation".equals(childNode.getNodeName())) {
                                        foundFirst = true;
                                        break;
                                }
                        }
                }

                // Si le premier élément n'est pas "G_Random_Image_Allocation", retourne null
                if (!foundFirst) {
                        return "";
                }

                // Concatène les noms des nœuds jusqu'au deuxième "G_Random_Sub_Image_Allocation"
                int subImageAllocationCount = 0;
                for (int i = 0; i < childNodes.getLength(); i++) {
                        Node childNode = childNodes.item(i);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                if ("G_Random_Sub_Image_Allocation".equals(childNode.getNodeName())) {
                                        subImageAllocationCount++;
                                        if (subImageAllocationCount == 2) {
                                                break;
                                        }
                                }
                                concatenatedNames.append(childNode.getNodeName());
                        }
                }

                return concatenatedNames.toString();
        }
}
