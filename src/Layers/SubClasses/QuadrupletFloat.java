/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers.SubClasses;

/**
 *
 * @author camil
 */
public class QuadrupletFloat {

        private final float  pos_x ;
        private final float  pos_y ;
        private final float   size_x ;
        private final float  size_y ;

        public QuadrupletFloat(float pos_x, float pos_y, float size_x, float size_y) {
                this.pos_x = pos_x;
                this.pos_y = pos_y;
                this.size_x = size_x;
                this.size_y = size_y;
        }

        public float getPos_x() {
                return pos_x;
        }

        public float getPos_y() {
                return pos_y;
        }

        public float getSize_x() {
                return size_x;
        }

        public float getSize_y() {
                return size_y;
        }

        
        
        
}
