package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import Exeptions.ResourcesFileErrorException;
import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
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

public class InterfaceResizableImage extends InterfaceNode {

    @FXML
    private ImageView PreviewBox;

    @FXML
    private Slider slider_scale;

    public InterfaceResizableImage(InterfaceNode upperIN, String name) {
        super(upperIN, name);
        if (upperIN != null) {
            upperInterface.placeInterface(this);
        }
    }

    @Override
    protected void initialiseInterface() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceResizableImage.fxml"));
            if (fxmlLoader == null) {
                throw new ResourcesFileErrorException();
            }
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);

            fxmlLoader.load();

            slider_scale.setMin(0.0);
            slider_scale.setMax(1.0);
            slider_scale.setValue(0.5);
            slider_scale.setBlockIncrement(0.01);

            PreviewBox.setImage(generateIndicator((float) this.slider_scale.getValue()));

        } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
            Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void refreshImagesBuilders() {
        this.updateLinkedDesignNodes();
        PreviewBox.setImage(generateIndicator((float) this.getSliderValue()));
    }

    public float getSliderValue() {
        return (float) this.slider_scale.getValue();
    }

    public static Image generateIndicator(float scale) {
        double squareSize = 200;

        Rectangle square = new Rectangle(squareSize, squareSize);
        square.setFill(Color.LIGHTGRAY);
        square.setStroke(Color.BLACK);

        double mappedScale = mapRange(scale, 0, 1, 0, squareSize);
        double circleSize = mappedScale;

        Circle circle = new Circle(circleSize / 2, Color.RED);
        circle.setCenterX(squareSize / 2);
        circle.setCenterY(squareSize / 2);

        Pane pane = new Pane();
        pane.getChildren().addAll(square, circle);

        SnapshotParameters params = new SnapshotParameters();
        WritableImage image = pane.snapshot(params, null);

        return image;
    }

    private static double mapRange(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
    }

    @Override
    protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {
        slider_scale.setValue(XmlManager.getDoubleAttribute(element, "Scale", 0.5));
    }

    @Override
    public XmlChild DRYsaveDesign() {
        XmlChild xmlScale = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
        xmlScale.addAttribute("Scale", String.valueOf(slider_scale.getValue()));

        return xmlScale;
    }
}
