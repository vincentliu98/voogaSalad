package authoringInterface.editor.editView;

import api.DraggingCanvas;
import api.SubView;
import authoringInterface.subEditors.*;
import gameObjects.crud.GameObjectsCRUDInterface;
import gameObjects.entity.EntityClass;
import gameObjects.entity.EntityInstance;
import gameObjects.gameObject.GameObjectClass;
import gameObjects.gameObject.GameObjectInstance;
import gameObjects.gameObject.GameObjectType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * EditGridView Class (ScrollPane)
 *      - Representation of the game's grid setting
 *      - It should support Zoom in and zoom out
 *
 * @author Amy Kim
 * @author Haotian Wang
 */
public class EditGridView implements SubView<ScrollPane> {
    private GridPane gridScrollView;
    private ScrollPane scrollPane;
    private GameObjectsCRUDInterface gameObjectManager;
    private Map<Node, GameObjectInstance> nodeToGameObjectInstanceMap;

    public EditGridView(int row, int col, GameObjectsCRUDInterface manager) {
        gameObjectManager = manager;
        scrollPane = new ScrollPane();
        nodeToGameObjectInstanceMap = new HashMap<>();
        gridScrollView = new GridPane();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                StackPane cell = new StackPane();
                cell.setPrefWidth(100);
                cell.setPrefHeight(100);
                gridScrollView.add(cell, i, j);
                cell.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> cell.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY))));
                cell.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> cell.setBackground(Background.EMPTY));
            }
        }
        gridScrollView.setGridLinesVisible(true);
        setupDraggingCanvas();
        scrollPane = new ScrollPane(gridScrollView);
    }

    public void updateDimension(int width, int height) {
        gridScrollView.getChildren().removeIf(c -> c instanceof StackPane);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                StackPane cell = new StackPane();
                cell.setPrefWidth(100);
                cell.setPrefHeight(100);
                gridScrollView.add(cell, i, j);
                cell.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> cell.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY))));
                cell.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> cell.setBackground(Background.EMPTY));
            }
        }
        gameObjectManager.getEntityInstances().clear();
        gameObjectManager.getTileInstances().clear();
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
                    editor = new EntityEditor(gameObjectManager);
                    break;
                case SOUND:
                    editor = new SoundEditor(gameObjectManager);
                    break;
                case TILE:
                    editor = new TileEditor(gameObjectManager);
                    break;
                case CATEGORY:
                    editor = new CategoryEditor(gameObjectManager);
                    break;
            }
            editor.editNode(targetNode, userObject);
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
    public void setupDraggingCanvas() {
        gridScrollView.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
            if (e.getGestureSource() instanceof TreeCell) {
                TreeItem<String> item = ((TreeCell<String>) e.getGestureSource()).getTreeItem();
                if (!item.isLeaf()) {
                    return;
                }
                if (!(e.getTarget() instanceof StackPane)) {
                    return;
                }
                StackPane intersected = (StackPane) e.getTarget();
                GameObjectClass objectClass = gameObjectManager.getGameObjectClass(item.getValue());
                GameObjectType type = objectClass.getType();
                switch (type) {
                    case ENTITY:
                        if (objectClass.getImagePathList().isEmpty()) {
                            Text deploy = new Text(objectClass.getClassName().getValue());
                            deploy.setOnMouseClicked(e1 -> handleDoubleClick(e1, deploy));
                            intersected.getChildren().add(deploy);
                            // TODO: get tile id
                            EntityInstance objectInstance = ((EntityClass) objectClass).createInstance(0);
                            nodeToGameObjectInstanceMap.put(deploy, objectInstance);
                        } else {
                            ImageView deploy = new ImageView(new Image(objectClass.getImagePathList().get(0)));
                            deploy.setOnMouseClicked(e1 -> handleDoubleClick(e1, deploy));
                            intersected.getChildren().add(deploy);
                            // TODO: get tile id
                            EntityInstance objectInstance = ((EntityClass) objectClass).createInstance(0);
                            nodeToGameObjectInstanceMap.put(deploy, objectInstance);
                        }
                        break;
                    case SOUND:
                        // TODO
                        break;
                    case TILE:
                        break;
                }
            }
        });
    }
}
