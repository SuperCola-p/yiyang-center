package java.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class NursingItemArray implements Iterable<NursingItem> , Serializable {
    private ArrayList<NursingItem> items;
    public NursingItemArray() {
        items = new ArrayList<>();
    }
    public void addItem(NursingItem item) {
        items.add(item);
    }

    @Override
    public Iterator<NursingItem> iterator() {
        return items.iterator();
    }

    public ArrayList<NursingItem> getItems() {
        return items;
    }
    public void setItems(ArrayList<NursingItem> items) {
        this.items = items;
    }
    public void deleteItem(NursingItem item) {
        items.remove(item);
    }
    public void clearItems() {
        items.clear();
    }
    public boolean findItem(NursingItem item) {
        return items.contains(item);
    }

}
