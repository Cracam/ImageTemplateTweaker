/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class VoidInterface extends InterfaceNode {

        public VoidInterface(InterfaceNode upperIN) {
                super(upperIN);
           //      upperInterface.placeInterface(this);
        }



        @Override
        protected Element DRYLoadDesign(Element element, int index) throws XMLErrorInModelException {
                return element;
        }

        @Override
        public Element DRYsaveDesign(Document doc) {
                return null;
        }

        @Override
        protected void initialiseInterface() {//no interface
        }
        
}
