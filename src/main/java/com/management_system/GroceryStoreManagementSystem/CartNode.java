package com.management_system.GroceryStoreManagementSystem;



// Node for the Custom Stack
class StackNode<T> {
    T data;
    StackNode<T> next;
    public StackNode(T data) { this.data = data; }
}

// Custom Stack Implementation  
class LinkedListStack<T> {
    private StackNode<T> head;
    
    public void push(T item) {
        StackNode<T> newNode = new StackNode<>(item);
        newNode.next = head;
        head = newNode;
    }
    
    public T pop() {
        if (head == null) return null;
        T item = head.data;
        head = head.next;                               // Removing from head is O(1) 
        return item;
    }
    
    public boolean isEmpty() { return head == null; }
    public void clear() { head = null; }
}

// Object to store undo data
class CartAction {
    Product product;
    int quantity;
    public CartAction(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}

// Singly Linked List Node for Cart 
class CartNode {
    Product product;
    int quantity;
    CartNode next;

    public CartNode(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.next = null;
    }
}
