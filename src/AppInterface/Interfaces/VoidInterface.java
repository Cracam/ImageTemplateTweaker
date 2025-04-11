/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import AppInterface.DesignBuilder;
import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ResourcesManager.XmlChild;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class VoidInterface extends InterfaceNode {

        public VoidInterface(InterfaceNode upperIN,String name) {
                super(upperIN,name);
           //      upperInterface.placeInterface(this);
        }



        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {//no interface
        }
        
        @Override
           public int loadDesign(Element element,int index) throws XMLErrorInModelException {
//                this.DRYLoadDesign((Element) element.getChildNodes().item(index));
//                index=index+1;

                try {
                        for (InterfaceNode lInter : this.getLowerInterfaces()) {
                               index = lInter.loadDesign(element,index);
                        }
                } catch (XMLErrorInModelException ex) {
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
                return index;
        }

        @Override
        public XmlChild DRYsaveDesign() {
            return null;
            //return  new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
        }

        @Override
        protected void initialiseInterface() {//no interface
        }
        
}
