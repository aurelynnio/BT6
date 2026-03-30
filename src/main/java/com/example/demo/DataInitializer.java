package com.example.demo;

import com.example.demo.model.Account;
import com.example.demo.model.AccountRole;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AccountRoleRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final BookService bookService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(BookService bookService,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           RoleRepository roleRepository,
                           AccountRepository accountRepository,
                           AccountRoleRepository accountRoleRepository,
                           PasswordEncoder passwordEncoder) {
        this.bookService = bookService;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Khởi tạo danh mục nếu chưa có
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("Điện tử"));
            categoryRepository.save(new Category("Thời trang"));
            categoryRepository.save(new Category("Thực phẩm"));
            categoryRepository.save(new Category("Đồ gia dụng"));
            categoryRepository.save(new Category("Sách & Văn phòng phẩm"));
            System.out.println(">> Đã thêm 5 danh mục mẫu.");
        }

        if (productRepository.count() == 0) {
            List<Category> categories = categoryRepository.findAll();
            Category electronics = categories.get(0);
            Category fashion = categories.get(1);
            Category food = categories.get(2);
            Category home = categories.get(3);
            Category stationery = categories.get(4);

            productRepository.saveAll(List.of(
                new Product(null, "iPhone 15 128GB", 9999000.0, null, electronics),
                new Product(null, "Samsung Galaxy S24", 9490000.0, null, electronics),
                new Product(null, "Tai nghe Sony WH-1000XM5", 7990000.0, null, electronics),
                new Product(null, "Laptop Dell Inspiron 15", 8990000.0, null, electronics),
                new Product(null, "Đồng hồ thông minh Xiaomi Watch 2", 3990000.0, null, electronics),

                new Product(null, "Áo sơ mi trắng basic", 349000.0, null, fashion),
                new Product(null, "Quần jean nam slim fit", 499000.0, null, fashion),
                new Product(null, "Giày sneaker đen tối giản", 890000.0, null, fashion),

                new Product(null, "Cà phê rang xay Arabica", 185000.0, null, food),
                new Product(null, "Socola hạnh nhân cao cấp", 129000.0, null, food),
                new Product(null, "Mật ong nguyên chất 500ml", 215000.0, null, food),

                new Product(null, "Nồi chiên không dầu 6L", 1690000.0, null, home),
                new Product(null, "Đèn bàn LED cảm ứng", 420000.0, null, home),
                new Product(null, "Bộ hộp đựng thực phẩm thủy tinh", 560000.0, null, home),

                new Product(null, "Sổ tay planner tối giản", 149000.0, null, stationery)
            ));
            System.out.println(">> Đã thêm 15 sản phẩm mẫu.");
        }

        // Khởi tạo role với prefix ROLE_ theo yêu cầu của Spring Security
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
            .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role userRole = roleRepository.findByName("ROLE_USER")
            .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        // Tạo account admin
        Account admin = accountRepository.findByLoginName("admin")
            .orElseGet(() -> accountRepository.save(
                new Account("admin", passwordEncoder.encode("admin123"), true)
            ));

        // Tạo account user1
        Account user1 = accountRepository.findByLoginName("user1")
            .orElseGet(() -> accountRepository.save(
                new Account("user1", passwordEncoder.encode("123456"), true)
            ));

        // Gán quyền cho admin và user1
        if (!accountRoleRepository.existsByAccountAndRole(admin, adminRole)) {
            accountRoleRepository.save(new AccountRole(admin, adminRole, true));
        }
        if (!accountRoleRepository.existsByAccountAndRole(user1, userRole)) {
            accountRoleRepository.save(new AccountRole(user1, userRole, true));
        }

        System.out.println("========================================");
        System.out.println("Total Books: " + bookService.getAllBooks().size());
        System.out.println("Total Categories: " + categoryRepository.count());
        System.out.println("Total Products: " + productRepository.count());
        System.out.println("Total Accounts: " + accountRepository.count());
        System.out.println("Total Roles: " + roleRepository.count());
        System.out.println("Mock Data Loaded Successfully!");
        System.out.println("========================================");
    }
}
