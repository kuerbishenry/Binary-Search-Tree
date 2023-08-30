/******************************************************************************
 *  Compilation:  javac BasicBST.java
 *  Execution:    java BasicBST
 *  Dependencies: StdIn.java StdOut.java Queue.java
 *
 *  A  basic binary search tree, based on the Princeton Algs4 code
 *  https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BST.java.html,
 *  and modified by the CS121 course staff.
 *
 ******************************************************************************/


import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * The {@code BasicBST} class represents an ordered symbol table of generic
 * key-value pairs.
 * It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 * <em>size</em>, and <em>is-empty</em> methods.
 * It also provides ordered methods for finding the <em>minimum</em>,
 * <em>maximum</em>.
 * It also provides a <em>keys</em> method for iterating over all of the keys.
 * A symbol table implements the <em>associative array</em> abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike {@link java.util.Map}, this class uses the convention that
 * values cannot be {@code null}—setting the
 * value associated with a key to {@code null} is equivalent to deleting the key
 * from the symbol table.
 * <p>
 * This implementation uses an (unbalanced) binary search tree. It requires that
 * the key type implements the {@code Comparable} interface and calls the
 * {@code compareTo()} and method to compare two keys. It does not call either
 * {@code equals()} or {@code hashCode()}.
 * <p>
 * The <em>put</em>, <em>contains</em>,<em>minimum</em>,<em>maximum</em>,
 * operations each take linear time in the worst case, if the tree
 * becomes unbalanced.
 * The <em>size</em>, and <em>is-empty</em> operations take constant time.
 * Construction takes constant time.
 * <p>
 * For additional information on BST,
 * see <a href="https://algs4.cs.princeton.edu/32BST">Section 3.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

public class BasicBST<Key extends Comparable<Key>, Value> {
    private Node root;             // root of BasicBST

    private class Node {
        private Key key;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public BasicBST() {
        root = null;
    }

    /**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

    // return number of key-value pairs in BasicBST rooted at x
    private int size(Node x) { //used to be private, needed in BSTExercise
        if (x == null) return 0;
        else return x.size;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null)
            throw new IllegalArgumentException("calls put() with a null key");
        if (val == null)
            throw new IllegalArgumentException(
                    "calls put() with a null value, delete not implemented");

        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }


    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else return max(x.right);
    }

    /**
     * Returns the keys in the BasicBST in level order.
     *
     * @return the keys in the BasicBST in level order traversal
     */
    public Iterable<Key> levelOrder() {
        Queue<Key> keys = new Queue<Key>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * If the tree is empty, return an iterable with no key in it.
     * <p>
     * This method allows the user to iterate over all of the keys
     * in a symbol table, e.g. with a variable name {@code st}, by
     * using the foreach notation: {@code for (Key key : st.keys())}.
     * <p>
     *
     * @return all keys in the symbol table
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<Key>();
        return keys(min(), max());
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return all keys in the symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null)
            throw new IllegalArgumentException("first argument lo to keys() is null");
        if (hi == null)
            throw new IllegalArgumentException("second argument hi to keys() is null");

        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }


    /**
     * Returns the height of the BasicBST tree.
     *
     * @return the height of the BasicBST tree (a 1-node tree has height 0)
     */
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    /**
     * Returns the height of the specified key in the BasicBST tree.
     * if the key does not exist in the tree, return -1;
     * if the key is in a leaf node, return 0.
     * For a non-leaf node, its height is one more than its
     * taller child.
     *
     * @return the height of the specified key in the BasicBST tree
     */

    public int height(Key key) {
        // TODO Implement this method
        return height2(root, key);
    }
    private int height2(Node x, Key i) {

        if (x == null) {
            return -1;
        }
        int cmp = i.compareTo(x.key);
        if (cmp < 0) {
            return height2(x.left, i);
        }
        if (cmp > 0) {
            return height2(x.right, i);
        }
            return 1 + Math.max(height(x.left), height(x.right));
        }


    /**
     * Returns all keys of the leaves in the BasicBST as an {@code Iterable}.
     * If the tree is empty, return an iterable with no key in it.
     *
     * @return all keys in the leaves of the BasicBST, in the order from
     * the smallest leaf key to the largest leaf key
     */


    public Iterable<Key> leaves() {
        Queue<Key> queue = new Queue<Key>();
        inorder(root, queue);
        return queue;
    }
        // TODO: write code to add the leaf keys to the queue
    private void inorder(Node x, Queue<Key> q) {
        if (x == null) {
            return;
        }
        if (x.left == null && x.right == null) {
            q.enqueue(x.key);
        }
        inorder(x.left, q);
        inorder(x.right, q);
    }

        /**
         * Calls  methods in  the {@code BasicBST} and print tree information
         *
         * @param args the command-line arguments
         */
    public static void main(String[] args) {
        BasicBST<String, Integer> st = new BasicBST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        StdOut.println("Tree printed in level order, showing keys and heights:");
        for (String key : st.levelOrder())
            StdOut.println(key + " " + st.height(key));
        StdOut.println();

        StdOut.println("Tree printed in order, showing keys and heights:");
        for (String key : st.keys())
            StdOut.println(key + " " + st.height(key));
        StdOut.println();

        StdOut.println("Leaf keys printed in order: ");
        for (String key : st.leaves())
            StdOut.print(key + " ");
        StdOut.println();
    }
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
