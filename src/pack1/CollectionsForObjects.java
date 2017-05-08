package pack1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * Created by IvanOP on 26.04.2017.
 */
public class CollectionsForObjects {
    private static ArrayList<AbstractBee> abstractBeeArrayList = new ArrayList<>();
    private static HashSet<Long> abstractBeeHashSet = new HashSet<>(100);
    private static TreeMap<Long, Long> longLongTreeMap = new TreeMap<>();
    private static volatile CollectionsForObjects instance;

    public static CollectionsForObjects getInstance() {
        CollectionsForObjects localInstance = instance;
        if (localInstance == null) {
            synchronized (CollectionsForObjects.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CollectionsForObjects();
                }
            }
        }
        return localInstance;
    }

    public void addObject(AbstractBee tempObj) {
        abstractBeeArrayList.add(tempObj);
    }

    ArrayList<AbstractBee> getAbstractBeeArrayList() {
        return abstractBeeArrayList;
    }

    public static void setAbstractBeeArrayList(ArrayList<AbstractBee> abstractBeeArrayList) {
        CollectionsForObjects.abstractBeeArrayList = abstractBeeArrayList;
    }

    TreeMap<Long, Long> getLongLongTreeMap() {
        return longLongTreeMap;
    }

    HashSet<Long> getAbstractBeeHashSet() {
        return abstractBeeHashSet;
    }

}


