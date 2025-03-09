/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.GetAttributeValueException;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class FixedImageInterface extends InterfaceNode {

        public FixedImageInterface(InterfaceNode upperIN) {
                super(upperIN);
        }



        @Override
        protected Element DRYLoadDesign(Element element, int index) throws GetAttributeValueException {
                return element;
        }

        @Override
        public Element DRYsaveDesign() {
                return null;
        }
        
}
