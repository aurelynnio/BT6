package com.example.demo;

import com.example.demo.model.Account;
import com.example.demo.model.AccountRole;
import com.example.demo.model.Category;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AccountRoleRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final BookService bookService;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(BookService bookService,
                           CategoryRepository categoryRepository,
                           RoleRepository roleRepository,
                           AccountRepository accountRepository,
                           AccountRoleRepository accountRoleRepository,
                           PasswordEncoder passwordEncoder) {
        this.bookService = bookService;
        this.categoryRepository = categoryRepository;
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
        System.out.println("Total Accounts: " + accountRepository.count());
        System.out.println("Total Roles: " + roleRepository.count());
        System.out.println("Mock Data Loaded Successfully!");
        System.out.println("========================================");
    }
}
