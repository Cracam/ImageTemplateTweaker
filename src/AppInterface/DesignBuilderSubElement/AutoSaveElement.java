/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.DesignBuilderSubElement;

import javafx.scene.control.Menu;

/**
 *
 * @author Camille LECOURT
 */
public abstract class AutoSaveElement extends Menu {
        
       abstract void updateAutoSaveList();
       
       /**
        * will load the interface of the sub menu
        */
       abstract void initialiseInterface();
}
