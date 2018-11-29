package authoringInterface.editor.menuBarView.subMenuBarView;


import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.Optional;
import static java.lang.Integer.parseInt;

/**
 * @author Amy
 */
//TODO: Refactor this stuff
public class ResizeGridView{
    private Dialog<Pair<Integer, Integer>> dialog;

    public ResizeGridView() {
        dialog = new Dialog<>();

        dialog.setTitle("Resize Grid of the Game");
        dialog.setHeaderText("This action will remove all entities on the grid.");

        ButtonType resizeBtn = new ButtonType("Resize", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(resizeBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField col = new TextField();
        col.setPromptText("Column");
        TextField row = new TextField();
        row.setPromptText("Row");

        grid.add(new Label("Column of the Grid:"), 0, 0);
        grid.add(col, 1, 0);
        grid.add(new Label("Row of the Grid:"), 0, 1);
        grid.add(row, 1, 1);


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            try {
                if (dialogButton == resizeBtn) {
                    return new Pair<>(parseInt(col.getText()), parseInt(row.getText()));
                }
            }
            catch(NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong input values");
                alert.setContentText("Only Integer values are acceptable");
                alert.showAndWait();
            }
            return null;
        });
    }

    public Optional<Pair<Integer, Integer>> showAndWait() {
        return dialog.showAndWait();
    }
}