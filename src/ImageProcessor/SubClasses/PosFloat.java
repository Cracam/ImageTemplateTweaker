/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.SubClasses;

/**
 *
 * @author camil
 */
public class PosFloat {

        private  float  pos_x ;
        private  float  pos_y ;


        public PosFloat(float pos_x, float pos_y) {
                this.pos_x = pos_x;
                this.pos_y = pos_y;

        }

        public float getPos_x() {
                return pos_x;
        }

        public float getPos_y() {
                return pos_y;
        }

 
        public void add(PosFloat otherPosFloat){
                this.pos_x = pos_x+otherPosFloat.getPos_x();
                this.pos_y = pos_y+otherPosFloat.getPos_y();
        }

        public void setPos_x(float pos_x) {
                this.pos_x = pos_x;
        }

        public void setPos_y(float pos_y) {
                this.pos_y = pos_y;
        }
        
        
        
}
