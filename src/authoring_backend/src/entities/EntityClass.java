package entities;

import essentials.Replicable;
import groovy.api.BlockGraph;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface EntityClass {

    void setDefaultHeightWidth(int defaultHeight, int defaultWidth);

    ReadOnlyIntegerProperty getClassId();

    void setClassId(Consumer<SimpleIntegerProperty> setFunc);

    SimpleMapProperty getPropertiesMap();

    boolean addProperty(String propertyName, int defaultValue);

    boolean removeProperty(String propertyName);

    SimpleListProperty getImagePathList();

    void addImagePath(String path);

    boolean removeImagePath(int index);

    void setImageSelector(BlockGraph blockCode);

    BlockGraph getImageSelectorCode();

}
