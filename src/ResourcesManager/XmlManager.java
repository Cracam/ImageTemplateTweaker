/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ResourcesManager;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents a child element with its attributes.
 */
public class XmlManager {

        private ArrayList<XmlChild> childs = new ArrayList<>();
        private Document doc;

        public XmlManager(Document doc) {
                this.doc = doc;
        }

        /**
         * Creates the DesignParam element with multiple child elements, each
         * with its own set of attributes.
         *
         * @param name The name of the DesignParam element.
         * @param attributeName
         * @param attributeValue
         * @return the DesignParam element
         */
        public Element createDesignParamElement(String name, String attributeName, String attributeValue) {

                // Create the DesignParam element
                Element designParamElement = doc.createElement(name);
                designParamElement.setAttribute(attributeName, attributeValue);

                // Create and append each child element
                for (XmlChild childElement : childs) {
                        System.out.println("CHILD"+childElement.getName());
                        Element element = doc.createElement(childElement.getName());
                        for (Map.Entry<String, String> entry : childElement.getAttributes().entrySet()) {
                                element.setAttribute(entry.getKey(), entry.getValue());
                        }
                        designParamElement.appendChild(element);
                }

                childs.clear();
                return designParamElement;

        }

        public void addChild(String name, Map<String, String> attributes) {
                childs.add(new XmlChild(name, attributes));
        }

        public void addChild(XmlChild child) {
                childs.add(child);
        }

        /**
         * Helper function to safely retrieve and parse a float attribute from
         * an element.
         *
         * @param element The element from which to retrieve the attribute.
         * @param attributeName The name of the attribute to retrieve.
         * @param ret
         * @return The parsed float value of the attribute, or null if the
         * attribute is not found.
         */
        static public Float getFloatAttribute(Element element, String attributeName, float ret) {
                try {
                        return Float.valueOf(getAttributeValue(element, attributeName));
                } catch (NumberFormatException | XMLErrorInModelException e) {
                    System.out.println("Invalid value or XML loading error  for attribute " + attributeName + "--- remplacing by default value");
                    return ret;
                }
        }
        
        
        /**
 * Helper function to safely retrieve and parse a double attribute from
 * an element.
 *
 * @param element The element from which to retrieve the attribute.
 * @param attributeName The name of the attribute to retrieve.
 * @param ret The default value to return if the attribute is not found or is invalid.
 * @return The parsed double value of the attribute, or the default value if the
 * attribute is not found or is invalid.
 */
static public Double getDoubleAttribute(Element element, String attributeName, double ret) {
    try {
        return Double.valueOf(getAttributeValue(element, attributeName));
    } catch (NumberFormatException | XMLErrorInModelException e) {
        System.out.println("Invalid value or XML loading error for attribute " + attributeName + "--- replacing by default value");
        return ret;
    }
}




        /**
 * Helper function to safely retrieve and parse an integer attribute from
 * an element.
 *
 * @param element The element from which to retrieve the attribute.
 * @param attributeName The name of the attribute to retrieve.
 * @param ret The default value to return if the attribute is not found or invalid.
 * @return The parsed integer value of the attribute, or the default value if the
 * attribute is not found or invalid.
 */
static public Integer getIntAttribute(Element element, String attributeName, int ret) {
    try {
        return Integer.valueOf(getAttributeValue(element, attributeName));
    } catch (NumberFormatException | XMLErrorInModelException e) {
        System.out.println("Invalid value or XML loading error for attribute " + attributeName + "--- replacing by default value ___ element : "+element);
        return ret;
    }
}

        /**
         * Helper function to safely retrieve and parse a string attribute from
         * an element.
         *
         * @param element The element from which to retrieve the attribute.
         * @param attributeName The name of the attribute to retrieve.
         * @param ret The default value to return if the attribute is not found
         * or is invalid.
         * @return The value of the attribute as a string, or the default value
         * if the attribute is not found or is invalid.
         */
        public static String getStringAttribute(Element element, String attributeName, String ret) {

                try {
                        String attributeValue = getAttributeValue(element, attributeName);
                        if (attributeValue != null && !attributeValue.isEmpty()) {
                                return attributeValue;
                        } else {
                                System.out.println("Invalid value for attribute " + attributeName + "--- remplacing by default value___ element : "+element);
                                return ret;
                        }
                } catch (XMLErrorInModelException ex) {
                        System.out.println("XML loading error  for attribute " + attributeName + "--- remplacing by default value ___ element : "+element);
                                return ret;
                }
        }

        
        

        private static String getAttributeValue(Element element, String attributeName) throws XMLErrorInModelException {
                if (element == null) {
                        throw new XMLErrorInModelException("Element that qhould be linked to " + attributeName + " is null.");
                }
           //    System.out.println( "=================="+element.getAttributes().getNamedItem(attributeName).getFirstChild().getNodeValue());
                Node attributeNode = element.getAttributes().getNamedItem(attributeName);
                if (attributeNode != null) {
                        return attributeNode.getNodeValue();
                } else {
                        throw new XMLErrorInModelException("Attribute " + attributeName + " is missing.");
                }
        }     
        
        
        
   // Méthode pour extraire le premier élément de type ELEMENT_NODE d'une NodeList
    public static Element extractSingleElement(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) node;
            }
        }
        return null;
    }
    

        // Méthode pour obtenir le premier enfant direct d'un élément parent avec un nom de balise spécifique
    public static Element getDirectChildByTagName(Element parentElement, String tagName) {
        NodeList allChildren = parentElement.getChildNodes();

        for (int i = 0; i < allChildren.getLength(); i++) {
            Node childNode = allChildren.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(tagName)) {
                return (Element) childNode;
            }
        }

        return null; // Retourne null si aucun enfant direct correspondant n'est trouvé
    }

}
