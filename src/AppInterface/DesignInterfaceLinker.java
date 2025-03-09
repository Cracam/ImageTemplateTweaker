/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface;

import AppInterface.Interfaces.FixedImageInterface;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerators.GeneratorCustomImage;
import ImageProcessor.ImageGenerators.GeneratorCustomText;
import ImageProcessor.ImageGenerators.GeneratorFixedImage;
import ImageProcessor.ImageGenerators.GeneratorFixedTextCustomStyle;
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
                linkMap.put("G_Fixed_Image", new DesignInterfacePair(GeneratorFixedImage.class, FixedImageInterface.class));
                linkMap.put("G_Custom_Image", new DesignInterfacePair(GeneratorCustomImage.class, FixedImageInterface.class));
                linkMap.put("G_Custom_Text", new DesignInterfacePair(GeneratorCustomText.class, FixedImageInterface.class));
                linkMap.put("G_Fixed_Text_Custom_Color_Custom_Style", new DesignInterfacePair(GeneratorFixedTextCustomStyle.class, FixedImageInterface.class));
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
