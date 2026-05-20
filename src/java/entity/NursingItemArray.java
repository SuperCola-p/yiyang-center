package java.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NursingItemArray implements Iterable<NursingItem>, Serializable {
    private ArrayList<NursingItem> items;

    public NursingItemArray() {
        items = new ArrayList<>();
    }

    public void addItem(NursingItem item) {
        if (item != null && !findItem(item)) {
            items.add(item);
        }
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
        if (item == null) {
            return;
        }
        items.removeIf(existing -> existing != null && existing.getId() != null
                && existing.getId().equals(item.getId()));
    }

    public void deleteItemById(Long itemId) {
        items.removeIf(existing -> existing != null && itemId != null && itemId.equals(existing.getId()));
    }

    public void clearItems() {
        items.clear();
    }

    public boolean findItem(NursingItem item) {
        if (item == null || item.getId() == null) {
            return false;
        }
        return items.stream().anyMatch(existing -> existing != null && item.getId().equals(existing.getId()));
    }

    public List<Long> getItemIds() {
        List<Long> ids = new ArrayList<>();
        for (NursingItem item : items) {
            if (item != null && item.getId() != null) {
                ids.add(item.getId());
            }
        }
        return ids;
    }
}
