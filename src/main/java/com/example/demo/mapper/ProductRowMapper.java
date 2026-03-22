package com.example.demo.mapper;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        product.setImage(rs.getString("image"));

        Category category = new Category();
        category.setId(rs.getLong("category_id"));
        category.setName(rs.getString("category_name"));
        product.setCategory(category);

        return product;
    }
}
