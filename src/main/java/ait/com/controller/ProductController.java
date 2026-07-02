package ait.com.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ait.com.model.Product;
import ait.com.serviceImpl.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Display all products
    @GetMapping
    public String listProducts(Model model, @RequestParam(required = false) String search) {
        List<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productService.searchByName(search);
            model.addAttribute("search", search);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        return "product/list";
    }

    // Show create form
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        return "product/form";
    }

    // Save new product
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("message", "Product created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("categories", productService.getAllCategories());
            return "product/form";
        }
        redirectAttributes.addFlashAttribute("error", "Product not found!");
        return "redirect:/products";
    }

    // Update product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // View product details
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product/view";
        }
        redirectAttributes.addFlashAttribute("error", "Product not found!");
        return "redirect:/products";
    }

    // Home page redirect
    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }
}
