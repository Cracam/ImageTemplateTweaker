package AppInterface;

import AppInterface.Interfaces.InterfaceCustomColor;
import AppInterface.Interfaces.InterfaceCustomImage;
import AppInterface.Interfaces.InterfaceCustomText;
import AppInterface.Interfaces.InterfaceFixedTextCustomStyle;
import AppInterface.Interfaces.InterfaceMouvableImage;
import AppInterface.Interfaces.InterfaceRandomImageAllocation;
import AppInterface.Interfaces.InterfaceRandomImageDispersion;
import AppInterface.Interfaces.InterfaceRandomSubImageAllocation;
import AppInterface.Interfaces.VoidInterface;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerators.GeneratorCustomImage;
import ImageProcessor.ImageGenerators.GeneratorCustomText;
import ImageProcessor.ImageGenerators.GeneratorFixedImage;
import ImageProcessor.ImageGenerators.GeneratorFixedTextCustomStyle;
import ImageProcessor.ImageGenerators.GeneratorRandomImageAllocation;
import ImageProcessor.ImageGenerators.GeneratorRandomImageDispersion;
import ImageProcessor.ImageGenerators.GeneratorRandomSubImageAllocation;
import ImageProcessor.ImagesTransformers.TransformerCustomColor;
import ImageProcessor.ImagesTransformers.TransformerInert;
import ImageProcessor.ImagesTransformers.TransformerMovableImage;
import ImageProcessor.Layer;
import ImageProcessor.VoidImage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Camille LECOURT
 */
public class DesignInterfaceLinker {

        private static final Map<String, DesignInterfacePair> linkMap;

        static {
                linkMap = new HashMap<>();
                linkMap.put("G_Fixed_Image", new DesignInterfacePair(GeneratorFixedImage.class, VoidInterface.class));
                linkMap.put("G_Custom_Image", new DesignInterfacePair(GeneratorCustomImage.class, InterfaceCustomImage.class));
                linkMap.put("G_Custom_Text", new DesignInterfacePair(GeneratorCustomText.class, InterfaceCustomText.class));
                linkMap.put("G_Fixed_Text_Custom_Color_Custom_Style", new DesignInterfacePair(GeneratorFixedTextCustomStyle.class, InterfaceFixedTextCustomStyle.class));
                  linkMap.put("G_Random_Image_Dispersion", new DesignInterfacePair(GeneratorRandomImageDispersion.class, InterfaceRandomImageDispersion.class));
                  linkMap.put("G_ONLY_USED_IN_CODE", new DesignInterfacePair(GeneratorRandomSubImageAllocation.class, InterfaceRandomSubImageAllocation.class));
                  linkMap.put("G_Random_Image_Allocation", new DesignInterfacePair(GeneratorRandomImageAllocation.class, InterfaceRandomImageAllocation.class));
                  
                  

                  linkMap.put("T_ONLY_USED_IN_CODE", new DesignInterfacePair(TransformerInert.class, VoidInterface.class));
                  linkMap.put("T_Custom_Color", new DesignInterfacePair(TransformerCustomColor.class, InterfaceCustomColor.class));
                 linkMap.put("T_Mouvable_Image", new DesignInterfacePair(TransformerMovableImage.class, InterfaceMouvableImage.class));
               
                 
                 linkMap.put("Void_Image", new DesignInterfacePair(VoidImage.class, VoidInterface.class));

                 linkMap.put("Layer", new DesignInterfacePair(Layer.class, LayerContainer.class));
        }

        public static Class<? extends InterfaceNode> getLinkedInterface(Class<? extends DesignNode> designNodeClass) {
                for (DesignInterfacePair pair : linkMap.values()) {
                        if (pair.designNodeClass.equals(designNodeClass)) {
                                return pair.interfaceNodeClass;
                        }
                }
                return null; // ou lancer une exception si l'élément n'est pas trouvé
        }

        public static Class<? extends DesignNode> getDesignNode(String identifier) {
                DesignInterfacePair pair = linkMap.get(identifier);
                return pair != null ? pair.designNodeClass : null; // ou lancer une exception si l'élément n'est pas trouvé
        }

        public static String getIdentifier(Class<?> clazz) {
                for (Map.Entry<String, DesignInterfacePair> entry : linkMap.entrySet()) {
                        if (clazz.isAssignableFrom(entry.getValue().designNodeClass) || clazz.isAssignableFrom(entry.getValue().interfaceNodeClass)) {
                                return entry.getKey();
                        }
                }
                return null; // ou lancer une exception si l'élément n'est pas trouvé
        }

        private static class DesignInterfacePair {

                Class<? extends DesignNode> designNodeClass;
                Class<? extends InterfaceNode> interfaceNodeClass;

                public DesignInterfacePair(Class<? extends DesignNode> designNodeClass, Class<? extends InterfaceNode> interfaceNodeClass) {
                        this.designNodeClass = designNodeClass;
                        this.interfaceNodeClass = interfaceNodeClass;
                }
        }
}
