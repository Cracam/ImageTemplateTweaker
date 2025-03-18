/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import Exeptions.ResourcesFileErrorException;
import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class have the only function of generating the image out from a file
 *
 * @author Camille LECOURT
 */
public class InterfaceMouvableImage extends InterfaceNode {

        @FXML
        private ImageView PreviewBox;

        @FXML
        private Slider slider_X;

        @FXML
        private Slider slider_Y;

        public InterfaceMouvableImage(InterfaceNode upperIN, String name) {
                super(upperIN, name);
               upperInterface.placeInterface(this);

        }

//        
//        
//
//        @Override
//        public Node saveInterfaceData(Document doc) {
//              XmlManager xmlManager = new XmlManager(doc);
//
//                XmlChild Xmloffset = new XmlChild("OffSet");
//                Xmloffset.addAttribute("X_Offset", String.valueOf(slider_X.getValue()));
//                Xmloffset.addAttribute("Y_Offset", String.valueOf(slider_Y.getValue()));
//                xmlManager.addChild(Xmloffset);
//                 
//                return xmlManager.createDesignParamElement("DesignParam", "InterfaceName", interfaceName);
//        }
//        
//        
//        
//
//        @Override
//        public void loadInterfaceData(Element dataOfTheLayer) {
//                slider_X.setValue( Double.parseDouble(dataOfTheLayer.getElementsByTagName("OffSet").item(0).getAttributes().getNamedItem("X_Offset").getNodeValue()));
//                slider_Y.setValue( Double.parseDouble(dataOfTheLayer.getElementsByTagName("OffSet").item(0).getAttributes().getNamedItem("Y_Offset").getNodeValue()));
//                refreshImagesBuilders();
//                
//        }
//        
        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceMovableImage.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                        slider_X.setMin(-0.999);
                        slider_X.setMax(0.999);
                        slider_X.setValue(0.0);
                        slider_X.setBlockIncrement(0.001);

                        slider_Y.setMin(-0.999);
                        slider_Y.setMax(0.999);
                        slider_Y.setValue(0.0);
                        slider_Y.setBlockIncrement(0.001);

                        PreviewBox.setImage(generateIndicator((float) this.slider_X.getValue(), (float) this.slider_Y.getValue()));

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @FXML
        private void refreshImagesBuilders() {
                this.updateLinkedDesignNodes();
                PreviewBox.setImage(generateIndicator((float) this.getSliderXValue(), (float) this.getSliderYValue()));
        }

        /**
         * return the vallue of the slider X
         *
         * @return
         */
        public float getSliderXValue() {
                return (float) this.slider_X.getValue();
        }

        /**
         * return the value of the slider y
         *
         * @return
         */
        public float getSliderYValue() {
                return (float) -this.slider_Y.getValue();
        }

        public static Image generateIndicator(float X, float Y) {
                // Taille du carré
                double squareSize = 200;

                // Création du carré
                Rectangle square = new Rectangle(squareSize, squareSize);
                square.setFill(Color.LIGHTGRAY);
                square.setStroke(Color.BLACK);

                // Mappage des coordonnées de [-1, 1] à [0, squareSize]
                double mappedX = mapRange(X, -1.15, 1.15, 0, squareSize);
                double mappedY = mapRange(Y, -1.15, 1.15, 0, squareSize);

                // Création du cercle
                Circle circle = new Circle(10, Color.RED);
                circle.setCenterX(mappedX);
                circle.setCenterY(mappedY);

                // Création du Pane et ajout des formes
                Pane pane = new Pane();
                pane.getChildren().addAll(square, circle);

                // Capture d'écran du Pane
                SnapshotParameters params = new SnapshotParameters();
                WritableImage image = pane.snapshot(params, null);

                return image;
        }

        // Fonction pour mapper une valeur d'un intervalle à un autre
        private static double mapRange(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
                return (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
        }

        @Override
        protected Element DRYLoadDesign(Element element, int index) throws XMLErrorInModelException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public Element DRYsaveDesign(Document doc) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

}
