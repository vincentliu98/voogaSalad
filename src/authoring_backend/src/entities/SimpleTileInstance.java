package entities;

import grids.Point;
import groovy.api.BlockGraph;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SimpleTileInstance implements TileInstance {
    private ReadOnlyStringWrapper className;
    private ReadOnlyIntegerWrapper instanceId;
    private ObservableSet<Point> points;
    private ObservableMap<String, String> propertiesMap;
    private Consumer<EntityInstance> returnInstanceIdFunc;


    SimpleTileInstance(String className, Set<Point> points, ObservableMap<String, String> properties, Consumer<EntityInstance> returnInstanceIdFunc) {
        this.className = new ReadOnlyStringWrapper();
        this.className.set(className);
        this.points = FXCollections.observableSet();
        this.points.addAll(points);
        this.propertiesMap = properties;
        this.returnInstanceIdFunc = returnInstanceIdFunc;
    }


    @Override
    public ReadOnlyIntegerProperty getInstanceId() {
        return instanceId.getReadOnlyProperty();
    }

    @Override
    public void setInstanceId(Consumer<SimpleIntegerProperty> setFunc) {
        setFunc.accept(instanceId);
    }

    @Override
    public ReadOnlyStringProperty getClassName() {
        return className.getReadOnlyProperty();
    }

    public Consumer<EntityInstance> getReturnInstanceIdFunc() {
        return returnInstanceIdFunc;
    }

    @Override
    public boolean addProperty(String propertyName, String defaultValue) {
        return false;
    }

    @Override
    public boolean removeProperty(String propertyName) {
        return false;
    }
}
