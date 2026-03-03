package core.basesyntax;


import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V>{

    // ---------- Constants ----------
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // ---------- Fields ----------
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;

    // ---------- Node (Entry) ----------
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[capacity];
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.table = new Node[capacity];
    }

    public void put(K key, V value) {
        int index = getIndex(key);

        Node<K, V> head = table[index];

        // Check if key already exists
        while (head != null) {
            if (Objects.equals(head.key, key)) {
                head.value = value; // update value
                return;
            }
            head = head.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;

        size++;

        // Check load factor
        if ((float) size / capacity >= loadFactor) {
            resize();
        }
    }

    public V getValue(K key) {
        int index = getIndex(key);

        Node<K, V> head = table[index];

        while (head != null) {
            if (Objects.equals(head.key, key)) {
                return head.value;
            }
            head = head.next;
        }

        return null;
    }

    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }

        int hash = key.hashCode();
        return Math.abs(hash) % capacity;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] oldTable = table;

        table = new Node[newCapacity];
        capacity = newCapacity;
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
