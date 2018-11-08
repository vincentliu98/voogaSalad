package authoringInterface.emptywindow;

import api.SubView;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

/**
 * GridScrollView Class (ScrollPane)
 *      - Representation of the game's grid setting
 *      - It should support Zoom in and zoom out
 *
 * @author Amy Kim
 */
public class GridScrollView implements SubView{
    private ScrollPane gridScrollView;

    private ScrollPane constructScrollPane(){
        var gridScrollView = new ScrollPane();

        return gridScrollView;
    }

    private void handleZoom(){}
    private void dragAndDrop(){}

    @Override
    public Node getView() {
        return gridScrollView;
    }
}
