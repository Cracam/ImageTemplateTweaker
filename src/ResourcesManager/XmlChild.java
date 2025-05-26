/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ResourcesManager;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Camille LECOURT
 */
public class XmlChild {
    private final String name;
    private final Map<String, String> attributes;

    public XmlChild(String name, Map<String, String> attributes) {
        this.name = name;
        this.attributes = new HashMap<>(attributes); // Create a copy to avoid external modifications
    }
    
    
    public XmlChild(String name) {
        this.name = name;
        this.attributes = new HashMap<>(); // Create a copy to avoid external modifications
    }
    

    public String getName() {
        return name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Adds or updates an attribute.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     */
    public void addAttribute(String attributeName, String attributeValue) {
        attributes.put(attributeName, attributeValue);
    }
}