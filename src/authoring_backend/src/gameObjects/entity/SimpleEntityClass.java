package gameObjects.entity;

import authoringUtils.exception.GameObjectTypeException;
import authoringUtils.exception.InvalidIdException;
import authoringUtils.exception.InvalidOperationException;
import gameObjects.ThrowingBiConsumer;
import gameObjects.gameObject.GameObjectInstance;
import gameObjects.gameObject.GameObjectType;
import grids.Point;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleEntityClass implements EntityClass {

    private static final String CONST_CLASSNAME = "className";
    private static final String CONST_ID = "id";
    private static final String CONST_MOVABLE = "movable";
    private static final int DEFAULT_HEIGHT = 1;
    private static final int DEFAULT_WIDTH = 1;

    private ReadOnlyStringWrapper className;
    private ReadOnlyIntegerWrapper classId;
    private SimpleBooleanProperty movable;
    private ObservableList<String> imagePathList;
    private ObservableMap<String, String> propertiesMap;
    private String imageSelector;
    private SimpleIntegerProperty width;
    private SimpleIntegerProperty height;

    private EntityInstanceFactory myFactory;
    private ThrowingBiConsumer<String, String, InvalidOperationException> changeEntityClassNameFunc;
    private Function<String, Collection<GameObjectInstance>> getAllEntityInstancesFunc;
    private Function<Integer, Boolean> deleteEntityInstanceFunc;


    public SimpleEntityClass(String className) {
        this.className = new ReadOnlyStringWrapper(this, CONST_CLASSNAME, className);
        classId = new ReadOnlyIntegerWrapper(this, CONST_ID);
        movable = new SimpleBooleanProperty(this, CONST_MOVABLE);
        imagePathList = FXCollections.observableArrayList();
        propertiesMap = FXCollections.observableHashMap();
        imageSelector = "";
        width = new SimpleIntegerProperty(DEFAULT_WIDTH);
        height = new SimpleIntegerProperty(DEFAULT_HEIGHT);
    }

    public SimpleEntityClass(
            String className,
            EntityInstanceFactory entityInstanceFactory,
            ThrowingBiConsumer<String, String, InvalidOperationException> changeEntityClassNameFunc,
            Function<String, Collection<GameObjectInstance>> getAllEntityInstancesFunc,
            Function<Integer, Boolean> deleteEntityInstanceFunc) {
        this(className);
        this.myFactory = entityInstanceFactory;
        this.changeEntityClassNameFunc = changeEntityClassNameFunc;
        this.getAllEntityInstancesFunc = getAllEntityInstancesFunc;
        this.deleteEntityInstanceFunc = deleteEntityInstanceFunc;
    }


    @Override
    public ReadOnlyIntegerProperty getClassId() {
        return classId.getReadOnlyProperty();
    }

    @Override
    public void setClassId(Consumer<SimpleIntegerProperty> setFunc) {
        setFunc.accept(classId);
    }

    @Override
    public ReadOnlyStringProperty getClassName() {
        return className.getReadOnlyProperty();
    }


    @Override
    public void changeClassName(String newClassName)
            throws InvalidOperationException {
        changeEntityClassNameFunc.accept(className.getValue(), newClassName);
    }

    @Override
    public void setClassName(String newClassName) {
        className.setValue(newClassName);
    }

    @Override
    public ObservableMap<String, String> getPropertiesMap() {
        return propertiesMap;
    }

    @Override
    public boolean addProperty(String propertyName, String defaultValue) {
        if (propertiesMap.containsKey(propertyName)) {
            return false;
        }
        propertiesMap.put(propertyName, defaultValue);
        Collection<EntityInstance> entityInstances = getAllInstances();
        for (EntityInstance e : entityInstances) {
            e.addProperty(propertyName, defaultValue);
        }
        return true;
    }

    @Override
    public boolean removeProperty(String propertyName) {
        if (!propertiesMap.containsKey(propertyName)) {
            return false;
        }
        propertiesMap.remove(propertyName);
        Collection<EntityInstance> entityInstances = getAllInstances();
        for (EntityInstance e : entityInstances) {
            e.removeProperty(propertyName);
        }
        return true;
    }

    @Override
    public ObservableList<String> getImagePathList() {
        return imagePathList;
    }

    @Override
    public void addImagePath(String path) {
        imagePathList.add(path);
    }


    @Override
    public boolean removeImagePath(int index) {
        try {
            imagePathList.remove(index);
            return true;
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public void setImageSelector(String blockCode) {
        imageSelector = blockCode;
    }

    @Override
    public String getImageSelectorCode() {
        return imageSelector;
    }

    @Override
    public SimpleIntegerProperty getHeight() {
        return height;
    }

    @Override
    public SimpleIntegerProperty getWidth() {
        return width;
    }

    @Override
    public void setHeight(int newHeight) {
        height.set(newHeight);
    }

    @Override
    public void setWidth(int newWidth) {
        width.set(newWidth);
    }

    @Override
    public EntityInstance createInstance(int playerId, Point point)
            throws GameObjectTypeException, InvalidIdException {
        return myFactory.createInstance(this, playerId, point);
    }

    public boolean deleteInstance(int entityInstanceId) {
        return deleteEntityInstanceFunc.apply(entityInstanceId);
    }


    @Override
    public Collection<EntityInstance> getAllInstances() {
        ObservableSet<EntityInstance> s = FXCollections.observableSet();
        Collection<GameObjectInstance> instances = getAllEntityInstancesFunc.apply(getClassName().getValue());
        for (GameObjectInstance i : instances) {
            if (i.getType() == GameObjectType.ENTITY) {
                s.add((EntityInstance) i);
            }
        }
        return s;
    }

    @Override
    public SimpleBooleanProperty isMovable() {
        return movable;
    }

    @Override
    public void setMovable(boolean move) {
        movable.setValue(move);
    }
}
