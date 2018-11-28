package authoringInterface.editor.editView;

import api.DraggingCanvas;
import api.SubView;
import authoringInterface.sidebar.SideViewInterface;
import authoringInterface.subEditors.*;
import gameObjects.gameObject.GameObjectInstance;
import gameObjects.gameObject.GameObjectType;
import gameplay.Entity;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * EditGridView Class (ScrollPane)
 *      - Representation of the game's grid setting
 *      - It should support Zoom in and zoom out
 *
 * @author Amy Kim, Haotian Wang
 */
public class EditGridView implements SubView<ScrollPane>, DraggingCanvas {
    private GridPane gridScrollView;
    private ScrollPane scrollPane;
    private SideViewInterface sideView;
    private Map<Node, GameObjectInstance> nodeToGameObjectInstanceMap;

    public EditGridView(SideViewInterface sideView) {
        this.sideView = sideView;
        scrollPane = new ScrollPane();
        nodeToGameObjectInstanceMap = new HashMap<>();
        gridScrollView = new GridPane();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                StackPane cell = new StackPane();
                cell.setPrefWidth(100);
                cell.setPrefHeight(100);
                gridScrollView.add(cell, i, j);
            }
        }
        gridScrollView.setGridLinesVisible(true);
        setupDraggingCanvas();
        scrollPane = new ScrollPane(gridScrollView);
    }

    /**
     * This method opens an editor window if the user wishes to double click on an object already dropped on the grid.
     *
     * @param event: A MouseEvent event, which is a double click.
     * @param targetNode: The JavaFx Node where this double click occurs.
     */
    private void handleDoubleClick(MouseEvent event, Node targetNode) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            GameObjectInstance userObject = nodeToGameObjectInstanceMap.get(targetNode);
            GameObjectType type = userObject.getType();
            Stage dialogStage = new Stage();
            AbstractGameObjectEditor editor = null;
            switch (type) {
                case ENTITY:
                    editor = new EntityEditor();
                    editor.readObject(userObject);
                case SOUND:
                    editor = new SoundEditor();
                    editor.readObject(userObject);
                case TILE:
                    editor = new TileEditor();
                    editor.readObject(userObject);
                case CATEGORY:
                    editor = new CategoryEditor();
                    editor.readObject(userObject);
            }
            editor.editNode(targetNode, nodeToObjectMap);
            dialogStage.setScene(new Scene(editor.getView(), 500, 500));
            dialogStage.show();
        }
    }

    @Override
    public ScrollPane getView() {
        return scrollPane;
    }

    /**
     * Setup the dragging canvas event filters.
     */
    @Override
    public void setupDraggingCanvas() {
        gridScrollView.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
            if (e.getGestureSource() instanceof TreeCell) {
                TreeItem<String> item = ((TreeCell<String>) e.getGestureSource()).getTreeItem();
                if (!item.isLeaf()) {
                    return;
                }
                StackPane intersected = (StackPane) e.getTarget();
                EditTreeItem object = sideView.getObject(item.getValue());
                TreeItemType type = object.getType();
                switch (type) {
                    case ENTITY:
                        if (((Entity) object).getSprite() == null) {
                            Text deploy = new Text(object.getName());
                            deploy.setOnMouseClicked(e1 -> handleDoubleClick(e1, deploy));
                            intersected.getChildren().add(deploy);
                            nodeToObjectMap.put(deploy, object);
                        } else {
                            ImageView deploy = new ImageView(((Entity) object).getSprite());
                            deploy.setOnMouseClicked(e1 -> handleDoubleClick(e1, deploy));
                            intersected.getChildren().add(deploy);
                            nodeToObjectMap.put(deploy, object);
                        }
                        break;
                }
            }
        });
    }
}
