package com.management_system.GroceryStoreManagementSystem;



public class CartList {
    private CartNode head;
    private int size;
    private LinkedListStack<CartAction> undoStack;

    public CartList() {
        head = null;
        size = 0;
        undoStack = new LinkedListStack<>();
    }

    public void addItem(Product p, int qty) {
        CartNode existing = findItem(p.getId());
        if (existing != null) {                       /// if existing on the cart then just update the quantity
            existing.quantity += qty;
        } else {
            CartNode newNode = new CartNode(p, qty);
            if (head == null) {
                head = newNode;
            } else {
                CartNode temp = head;
                while (temp.next != null) temp = temp.next;                    // Traverse to end of list(single linked list)
                temp.next = newNode; // Add new node at the end hahaha -> O(n)
            }
            size++;
        }
        // Temporarily reduce stock and add to undo stack 
        p.setStock(p.getStock() - qty);
        undoStack.push(new CartAction(p, qty));
        System.out.println("Item added to cart.");
    }

    public boolean removeItem(int productId) {
        if (head == null) return false;                               // if we do not have any item in the cart
        
        if (head.product.getId() == productId) {      // if we have just a single item
            // Restore stock back to inventory 
            head.product.setStock(head.product.getStock() + head.quantity); 
            head = head.next;
            size--;
            return true;
        }
        
        CartNode curr = head;
        while (curr.next != null) {
            if (curr.next.product.getId() == productId) {           // we are looking for the node just before the one we want to remove.
                curr.next.product.setStock(curr.next.product.getStock() + curr.next.quantity);
                curr.next = curr.next.next;
                size--;
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    public void updateQuantity(int productId, int newQty) {
        CartNode node = findItem(productId);
        if (node == null) {
            System.out.println("Product not in cart.");
            return;
        }
        
        int difference = newQty - node.quantity; 
        if (difference > 0 && node.product.getStock() < difference) {
            System.out.println("Insufficient inventory to increase quantity.");
            return;
        }
        
        // Adjust stock in inventory accordingly 
        node.product.setStock(node.product.getStock() - difference);
        node.quantity = newQty;
        
        if (node.quantity <= 0) {
            // Remove completely without double restoring stock
            int id = node.product.getId();
            if (head.product.getId() == id) head = head.next;
            else {
                CartNode temp = head;
                while(temp.next != null && temp.next.product.getId() != id) temp = temp.next;
                if(temp.next != null) temp.next = temp.next.next;
            }
            size--;
        }
        System.out.println("Cart updated.");
    }

    public CartNode findItem(int productId) {
        CartNode temp = head;
        while (temp != null) {
            if (temp.product.getId() == productId) return temp;
            temp = temp.next;
        }
        return null; // Return CartNode or null 
    }

    public void displayCart() {
        if (head == null) {
            System.out.println("\nCart is empty.");
            return;
        }
        System.out.println("\n--- Shopping Cart ---");
        CartNode temp = head;
        while (temp != null) {
            double subtotal = temp.product.getPrice() * temp.quantity;
            System.out.printf("%-15s | Qty: %-3d | Unit Price: $%-7.2f | Subtotal: $%.2f\n", 
                              temp.product.getName(), temp.quantity, temp.product.getPrice(), subtotal);
            temp = temp.next;
        }
    }

    public double calculateTotal() {
        double total = 0;
        CartNode temp = head;
        while (temp != null) {
            total += temp.product.getPrice() * temp.quantity;
            temp = temp.next;
        }
        return total;
    }

    public void clear() {
        CartNode temp = head;
        while (temp != null) {
            // Restore all stock back to inventory
            temp.product.setStock(temp.product.getStock() + temp.quantity);
            temp = temp.next;
        }
        head = null;
        size = 0;
        undoStack.clear();
        System.out.println("Cart cleared. Stock restored.");
    }

    // Clear cart for checkout (Does NOT restore stock because it's permanently bought)
    public void checkoutClear() {
        head = null;
        size = 0;
        undoStack.clear();
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        CartAction lastAction = undoStack.pop(); // Pop last action 
        
        // Restore stock in inventory 
        lastAction.product.setStock(lastAction.product.getStock() + lastAction.quantity);
        
        // Remove from cart visual representation
        CartNode node = findItem(lastAction.product.getId());
        if (node != null) {
            node.quantity -= lastAction.quantity;
            if (node.quantity <= 0) {
                // Hard remove to bypass automatic restore in removeItem
                if (head == node) head = head.next;
                else {
                    CartNode curr = head;
                    while(curr.next != node) curr = curr.next;
                    curr.next = curr.next.next;
                }
                size--;
            }
        }
        System.out.println("Undo successful: Removed " + lastAction.quantity + "x " + lastAction.product.getName());
    }

    public int getSize() { return size; }
    public boolean isEmpty() { return head == null; }
}
