package AppInterface;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Camille LECOURT
 */
public  class SubInterfaceContainer {

        private  ArrayList<SubInterfaceContainer> upperIN;
        private String name;
        private String uniqueID;

        // Constructeur prenant un seul upperDN
   

        public void loadInterfaceData(Element interfaceNode){
                
        }
        
         public Node saveInterfaceData( Document document){
                 
                return null;
                 
         }

        /**
         * This update all the lower elements
         */
        public void update() {
                for (SubInterfaceContainer upper : upperDN) {
                        upper.update();
                }
        }
        

    
       
    public String getUniqueID(){
            return uniqueID;
    }
        ///////////////////////////////////////////
        
        

}
