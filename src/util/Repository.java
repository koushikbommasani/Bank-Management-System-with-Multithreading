package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// Unit III: generic class - T is a placeholder for any type.
// In this project we use Repository<Account>, but it could store any kind of object.
// Unit IV: items are stored in a HashMap (key = id, value = the item).
public class Repository<T> {

    // Unit IV: HashMap gives fast lookup of an item by its id
    private HashMap<String, T> items;

    // Constructor - starts with an empty HashMap
    public Repository() {
        items = new HashMap<String, T>();
    }

    // Adds an item with the given id (replaces any old item with the same id)
    public void add(String id, T item) {
        items.put(id, item);
    }

    // Returns the item with this id, or null if there is no such item
    public T findById(String id) {
        return items.get(id);
    }

    // Returns true if an item with this id exists
    public boolean exists(String id) {
        return items.containsKey(id);
    }

    // Returns all items in a new ArrayList
    public ArrayList<T> getAll() {
        return new ArrayList<T>(items.values());
    }

    // Returns how many items are stored
    public int size() {
        return items.size();
    }

    // Unit III: generic method - <E> means it works for a collection of any type.
    // Prints every item of the collection on its own line.
    public static <E> void printAll(Collection<E> collection) {
        for (E item : collection) {
            System.out.println(item);
        }
    }
}
