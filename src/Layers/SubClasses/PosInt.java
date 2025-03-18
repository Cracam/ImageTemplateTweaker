/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers.SubClasses;

/**
 *
 * @author camil
 */
public class PosInt {

        private  int  pos_x ;
        private  int  pos_y ;


        public PosInt(int pos_x, int pos_y) {
                this.pos_x = pos_x;
                this.pos_y = pos_y;

        }
        
         public   PosInt(PosFloat posSize,float pixelPerMilimeterFactor){
                  this.pos_x= (int) (posSize.getPos_x() * pixelPerMilimeterFactor);
                this.pos_y = (int) (posSize.getPos_y() * pixelPerMilimeterFactor);
        }

        public int getPos_x() {
                return pos_x;
        }

        public int getPos_y() {
                return pos_y;
        }



        public  void computePixelPosSize(PosFloat posSize,float pixelPerMilimeterFactor){
                  this.pos_x= (int) (posSize.getPos_x() * pixelPerMilimeterFactor);
                this.pos_y = (int) (posSize.getPos_y() * pixelPerMilimeterFactor);
        }
        
        
}
