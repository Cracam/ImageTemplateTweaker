/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers.SubClasses;

/**
 *
 * @author camil
 */
public class QuadrupletInt {

        private  int  pos_x ;
        private  int  pos_y ;
        private  int   size_x ;
        private  int  size_y ;

        public QuadrupletInt(int pos_x, int pos_y, int size_x, int size_y) {
                this.pos_x = pos_x;
                this.pos_y = pos_y;
                this.size_x = size_x;
                this.size_y = size_y;
        }
        
        public QuadrupletInt(QuadrupletFloat posSize,float pixelPerMilimeterFactor){
                computePixelPosSize(posSize,pixelPerMilimeterFactor);
        }

        public int getPos_x() {
                return pos_x;
        }

        public int getPos_y() {
                return pos_y;
        }

        public int getSize_x() {
                return size_x;
        }

        public int getSize_y() {
                return size_y;
        }

        public  void computePixelPosSize(QuadrupletFloat posSize,float pixelPerMilimeterFactor){
                  this.pos_x= (int) (posSize.getPos_x() * pixelPerMilimeterFactor);
                this.pos_y = (int) (posSize.getPos_y() * pixelPerMilimeterFactor);
                this.size_x = (int) (posSize.getSize_x() * pixelPerMilimeterFactor);
                this.size_y = (int) (posSize.getSize_y() * pixelPerMilimeterFactor);
        }
        
        
}
