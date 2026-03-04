package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int RESIZE_MULTIPLIER = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V> [] table;
    private int size;
    private int capacity;
    private final float loadFactor;

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
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
        final Node<K, V>[] oldTable = table;
        int newCapacity = capacity * RESIZE_MULTIPLIER;
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
