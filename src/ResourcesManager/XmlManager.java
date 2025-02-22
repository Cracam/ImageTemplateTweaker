/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ResourcesManager;

import java.util.ArrayList;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents a child element with its attributes.
 */
public class XmlManager {

        private ArrayList<XmlChild> childs = new ArrayList<>();
        private Document doc;
        
        public XmlManager(Document doc) {
                this.doc=doc;
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
                if (element == null) {
                        System.err.println("Element is null.");
                        return ret;
                }

                Node attributeNode = element.getAttributes().getNamedItem(attributeName);
                if (attributeNode != null) {
                        try {
                                return Float.parseFloat(attributeNode.getNodeValue());
                        } catch (NumberFormatException e) {
                                System.err.println("Invalid value for attribute " + attributeName + ": " + attributeNode.getNodeValue());
                                return ret;
                        }
                } else {
                        System.err.println("Attribute " + attributeName + " is missing.");
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
                if (element == null) {
                        System.err.println("Element is null.");
                        return ret;
                }

                Node attributeNode = element.getAttributes().getNamedItem(attributeName);
                if (attributeNode != null) {
                        String attributeValue = attributeNode.getNodeValue();
                        if (attributeValue != null && !attributeValue.isEmpty()) {
                                return attributeValue;
                        } else {
                                System.err.println("Invalid value for attribute " + attributeName + ": " + attributeValue);
                                return ret;
                        }
                } else {
                        System.err.println("Attribute " + attributeName + " is missing.");
                        return ret;
                }
        }

}
