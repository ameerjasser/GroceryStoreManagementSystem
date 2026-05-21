package com.management_system.GroceryStoreManagementSystem;



import java.util.Scanner;
import java.util.ArrayList;

public class GroceryStoreSystem {
    private static InventoryManager inv = new InventoryManager();
    private static CartList cart = new CartList();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inv.loadFromFile("inventory.txt"); // Load at start 
        boolean running = true;

        while (running) {
            System.out.println("\n=================================");
            System.out.println(" Grocery Store Management System ");
            System.out.println("=================================");
            System.out.println("1.  Display all products");
            System.out.println("2.  Search product by ID");
            System.out.println("3.  Search product by name");
            System.out.println("4.  Add new product");
            System.out.println("5.  Remove product");
            System.out.println("6.  Update stock");
            System.out.println("7.  Add item to cart");
            System.out.println("8.  View cart");
            System.out.println("9.  Remove item from cart");
            System.out.println("10. Update item quantity in cart");
            System.out.println("11. Undo last cart addition");
            System.out.println("12. Clear cart");
            System.out.println("13. Generate Bill / Checkout");
            System.out.println("14. Save and Exit");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1 -> inv.displayAll();
                    case 2 -> searchById();
                    case 3 -> searchByName();
                    case 4 -> addProduct();
                    case 5 -> removeProduct();
                    case 6 -> updateStock();
                    case 7 -> addToCart();
                    case 8 -> cart.displayCart();
                    case 9 -> removeFromCart();
                    case 10 -> updateCartQty();
                    case 11 -> cart.undo();
                    case 12 -> cart.clear();
                    case 13 -> checkout();
                    case 14 -> {
                        inv.saveToFile("inventory.txt"); // Save on exit 
                        running = false;
                        System.out.println("System exiting. Goodbye!");
                    }
                    default -> System.out.println("Invalid selection. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void searchById() {
        System.out.print("Enter Product ID: ");
        int id = Integer.parseInt(sc.nextLine());
        Product p = inv.searchById(id);
        if (p != null) 
            System.out.println("Found: " + p);
        else 
            System.out.println("Product not found.");
    }

    private static void searchByName() {
        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();
        ArrayList<Product> results = inv.searchByName(name);
        if (results.isEmpty()) 
            System.out.println("No products found.");
        else 
            for (Product p : results) System.out.println(p);
    }

    private static void addProduct() {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Enter Stock: ");
        int stock = Integer.parseInt(sc.nextLine());
        
        if (inv.addProduct(new Product(id, name, price, stock))) {
            System.out.println("Product added.");
        }
    }

    private static void removeProduct() {
        System.out.print("Enter Product ID to remove: ");
        int id = Integer.parseInt(sc.nextLine());
        if (inv.removeProduct(id)) System.out.println("Product removed.");
        else System.out.println("Product not found.");
    }

    private static void updateStock() {
        System.out.print("Enter Product ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Enter new stock quantity: ");
        int stock = Integer.parseInt(sc.nextLine());
        inv.updateStock(id, stock);
    }

    private static void addToCart() {
        System.out.print("Enter Product ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Enter Quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        
        if (inv.isAvailable(id, qty)) {
            cart.addItem(inv.getProductById(id), qty);
        } else {
            System.out.println("Error: Insufficient stock or product not found.");
        }
    }

    private static void removeFromCart() {
        System.out.print("Enter Product ID to remove from cart: ");
        int id = Integer.parseInt(sc.nextLine());
        if (cart.removeItem(id)) System.out.println("Item removed from cart.");
        else System.out.println("Item not found in cart.");
    }

    private static void updateCartQty() {
        System.out.print("Enter Product ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Enter new quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        cart.updateQuantity(id, qty);
    }

    private static void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout.");
            return;
        }
        System.out.println("\n======= RECEIPT =======");
        cart.displayCart();
        System.out.printf("\nTOTAL BILL: $%.2f\n", cart.calculateTotal());
        System.out.println("=======================");
        cart.checkoutClear(); // Clears cart and stack permanently
        System.out.println("Checkout complete. Final stock reduced permanently.");
    }
}
