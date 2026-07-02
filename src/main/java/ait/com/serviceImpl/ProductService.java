package ait.com.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import ait.com.model.Product;

@Service
public class ProductService {

    private List<Product> products = new ArrayList<>();
    private AtomicLong idCounter = new AtomicLong(1);

    public ProductService() {
        // Initialize with sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        products.add(new Product(1L, "Laptop", "High-performance laptop", 999.99, 5, "Electronics"));
        products.add(new Product(2L, "Mouse", "Wireless mouse", 29.99, 50, "Electronics"));
        products.add(new Product(3L, "Keyboard", "Mechanical keyboard", 79.99, 30, "Electronics"));
        products.add(new Product(4L, "Monitor", "4K Monitor", 399.99, 10, "Electronics"));
        products.add(new Product(5L, "Desk Lamp", "LED desk lamp", 49.99, 25, "Furniture"));
        idCounter.set(6);
    }

    // Create
    public Product saveProduct(Product product) {
        if (product.getId() == null) {
            product.setId(idCounter.getAndIncrement());
            products.add(product);
        }
        return product;
    }

    // Read All
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    // Read by ID
    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    // Search by name
    public List<Product> searchByName(String name) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    // Search by category
    public List<Product> searchByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    // Update
    public Product updateProduct(Long id, Product productDetails) {
        return getProductById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            product.setCategory(productDetails.getCategory());
            return product;
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Delete
    public void deleteProduct(Long id) {
        products.removeIf(p -> p.getId().equals(id));
    }

    // Get all categories
    public List<String> getAllCategories() {
        return products.stream()
                .map(Product::getCategory)
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .toList();
    }
}

