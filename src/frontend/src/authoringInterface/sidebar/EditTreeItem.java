package authoringInterface.sidebar;

import api.SubView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * This interface abstracts the meaning of draggable components of the graphical interface. Dragable simply means the user can press down the mouse key on an element and drag the element along the program which displays a defined preview of the elements being dragged.
 *
 * @author Haotian Wang
 */
public interface EditTreeItem<T extends Node> {
    /**
     * @return Return a preview of the elements being dragged.
     */
    T getPreview();

    /**
     * @return The type of the element being dragged.
     */
    TreeItemType getType();

    /**
     * @return The String name of this edit item entry.
     */
    String getName();
}
