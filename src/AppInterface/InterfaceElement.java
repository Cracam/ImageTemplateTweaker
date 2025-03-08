/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface;

import ImageProcessor.DesignNode;
import java.util.ArrayList;
import javafx.scene.layout.VBox;

/**
 *
 * @author Camille LECOURT
 */
public abstract class InterfaceElement extends VBox {
       private ArrayList<InterfaceElement> upperInterfaces;
       private ArrayList<InterfaceElement> lowerInterfaces;
       private ArrayList<DesignNode> linkedDesignNodes;
               
       
}
