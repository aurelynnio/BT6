package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryWebController {

    private final CategoryRepository categoryRepository;

    public CategoryWebController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("mode", "create");
        return "categories/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("category") Category category,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "create");
            return "categories/form";
        }
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm danh mục thành công!");
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        return categoryRepository.findById(id).map(category -> {
            model.addAttribute("category", category);
            model.addAttribute("mode", "edit");
            return "categories/form";
        }).orElse("redirect:/categories");
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("category") Category category,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "categories/form";
        }
        category.setId(id);
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật danh mục thành công!");
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
        return "redirect:/categories";
    }
}
