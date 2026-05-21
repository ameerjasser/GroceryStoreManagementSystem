package com.management_system.GroceryStoreManagementSystem;


import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class InventoryManager {
    private ArrayList<Product> inventory;

    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    public void loadFromFile(String filename) {
        inventory.clear();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int stock = Integer.parseInt(parts[3].trim());
                    inventory.add(new Product(id, name, price, stock)); // Populate ArrayList 
                }
            }
            System.out.println("Inventory loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found. Starting with an empty inventory.");
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product p : inventory) {
                // Format: ID, Name, Price, Stock
                writer.println(p.getId() + "," + p.getName() + "," + p.getPrice() + "," + p.getStock());
            }
            System.out.println("Inventory saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public boolean addProduct(Product p) {
        if (searchById(p.getId()) != null) {
            System.out.println("Error: Duplicate Product ID."); // No duplicate ID allowed 
            return false; 
        }
        inventory.add(p);
        return true;
    }

    public boolean removeProduct(int id) {
        return inventory.removeIf(p -> p.getId() == id); // Removes by ID 
    }

    public Product searchById(int id) {
        for (Product p : inventory) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public ArrayList<Product> searchByName(String name) {
        ArrayList<Product> results = new ArrayList<>();
        for (Product p : inventory) {
            // Case-insensitive, partial match allowed 
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(p);
            }
        }
        return results;
    }

    public void updateStock(int id, int newStock) {
        Product p = searchById(id);
        if (p != null) {
            p.setStock(newStock);
            System.out.println("Stock updated successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    public void displayAll() {
        System.out.println("\n--- Inventory ---");
        if (inventory.isEmpty()) System.out.println("Inventory is empty.");
        for (Product p : inventory) {
            System.out.println(p); // Display formatted table
        }
    }

    public Product getProductById(int id) {
        return searchById(id); // Returns product for cart ops 
    }

    public boolean isAvailable(int id, int requestedQty) {
        Product p = searchById(id);
        return p != null && p.getStock() >= requestedQty;
    }
}