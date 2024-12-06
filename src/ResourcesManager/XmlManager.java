/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ResourcesManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents a child element with its attributes.
 */
public  class XmlManager {
        static ArrayList<ChildElement>childs= new ArrayList<>();

         public XmlManager(){
                 
         }
            /**
     * Creates the DesignParam element with multiple child elements, each with its own set of attributes.
     *
         * @param name
     * @return the DesignParam element
     */
    public  Element createDesignParamElement(String name ) {
                try {
                        // Create a DocumentBuilderFactory
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        // Create a DocumentBuilder
                        DocumentBuilder builder;
                        builder = factory.newDocumentBuilder();
                        
                        // Create a new Document
                        Document doc = builder.newDocument();
                        
                        
                        // Create the DesignParam element
                        Element designParamElement = doc.createElement(name);
                        
                        // Create and append each child element
                        for (ChildElement childElement : childs) {
                                Element element = doc.createElement(childElement.getName());
                                for (Map.Entry<String, String> entry : childElement.getAttributes().entrySet()) {
                                        element.setAttribute(entry.getKey(), entry.getValue());
                                }
                                designParamElement.appendChild(element);
                        }
                        
                        childs.clear();
                        return designParamElement;
                } catch (ParserConfigurationException ex) {
                        Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
                        childs.clear();
                        return null;
                }
    }
    
    
    /**
     * Creates the DesignParam element with multiple child elements, each with its own set of attributes.
     *
     * @param name     The name of the DesignParam element.
     * @param layerName The value of the layerName attribute.
     * @return the DesignParam element
     */
    public Element createDesignParamElement(String name, String attributeName ,String attributeValue) {
        try {
            // Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Create a DocumentBuilder
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();

            // Create a new Document
            Document doc = builder.newDocument();

            // Create the DesignParam element
            Element designParamElement = doc.createElement(name);
            designParamElement.setAttribute(attributeName, attributeValue);

            // Create and append each child element
            for (ChildElement childElement : childs) {
                Element element = doc.createElement(childElement.getName());
                for (Map.Entry<String, String> entry : childElement.getAttributes().entrySet()) {
                    element.setAttribute(entry.getKey(), entry.getValue());
                }
                designParamElement.appendChild(element);
            }

            childs.clear();
            return designParamElement;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlManager.class.getName()).log(Level.SEVERE, null, ex);
            childs.clear();
            return null;
        }
    }
        

    public  void addChild(String name, Map<String, String> attributes){
            childs.add(new ChildElement(name,attributes));       
    }
        
        
        
     /**
     * Represents a child element with its attributes.
     */
    private static class ChildElement {
        private final String name;
        private final Map<String, String> attributes;

        public ChildElement(String name, Map<String, String> attributes) {
            this.name = name;
            this.attributes = attributes;
            XmlManager.childs.add(this);
        }

        public String getName() {
            return name;
        }
        
        

        public Map<String, String> getAttributes() {
            return attributes;
        }
    }
    
    
    
}
