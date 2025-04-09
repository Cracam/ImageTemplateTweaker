/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ResourcesManager.XmlChild;
import org.w3c.dom.Document;
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
        public XmlChild DRYsaveDesign() {
                return null;
        }

        @Override
        protected void initialiseInterface() {//no interface
        }
        
}
