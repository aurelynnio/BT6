package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductWebController {

    private final ProductService productService;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public ProductWebController(ProductService productService) {
        this.productService = productService;
    }

    // Danh sách sản phẩm
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    // Form thêm sản phẩm
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("mode", "create");
        return "products/form";
    }

    // Xử lý thêm sản phẩm
    @PostMapping
    public String create(@Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        // Validate file upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String originalFilename = imageFile.getOriginalFilename();
            if (originalFilename != null && originalFilename.length() > 200) {
                result.rejectValue("image", "Size", "Tên hình ảnh không quá 200 ký tự");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("mode", "create");
            return "products/form";
        }

        // Upload hình ảnh
        if (imageFile != null && !imageFile.isEmpty()) {
            String savedFileName = saveFile(imageFile);
            product.setImage(savedFileName);
        }

        productService.addProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công!");
        return "redirect:/products";
    }

    // Form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        return productService.getProductById(id).map(product -> {
            model.addAttribute("product", product);
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("mode", "edit");
            return "products/form";
        }).orElse("redirect:/products");
    }

    // Xử lý cập nhật sản phẩm
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        // Validate file upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String originalFilename = imageFile.getOriginalFilename();
            if (originalFilename != null && originalFilename.length() > 200) {
                result.rejectValue("image", "Size", "Tên hình ảnh không quá 200 ký tự");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("mode", "edit");
            return "products/form";
        }

        // Upload hình ảnh mới nếu có
        if (imageFile != null && !imageFile.isEmpty()) {
            String savedFileName = saveFile(imageFile);
            product.setImage(savedFileName);
        }

        productService.updateProduct(id, product);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công!");
        return "redirect:/products";
    }

    // Xóa sản phẩm
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        return "redirect:/products";
    }

    // Lưu file upload
    private String saveFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + e.getMessage(), e);
        }
    }
}
