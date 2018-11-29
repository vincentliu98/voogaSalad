package authoringInterface.editor.editView;

import api.SubView;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class MainTabView implements SubView<StackPane> {
    private final StackPane addPane;
    public static final int ICON_SIZE = 550;
    private final Image LOGO_IMG =
            new Image(MainTabView.class.getClassLoader().getResourceAsStream("Groove_logo.png"));
    private ImageView myIcon;
    protected Button startBtn;

    public MainTabView() {
        addPane = new StackPane();
        addPane.getStyleClass().add("addPane");
        startBtn = new Button();
        startBtn.setText("START");
        startBtn.getStyleClass().add("startBtn");
        myIcon = new ImageView(LOGO_IMG);
        myIcon.setFitWidth(ICON_SIZE);
        myIcon.setFitHeight(ICON_SIZE);
//        addPane.getChildren().addAll(myIcon, startBtn);
        addPane.getChildren().addAll(myIcon);

    }


    @Override
    public StackPane getView() {
        return addPane;
    }

}
