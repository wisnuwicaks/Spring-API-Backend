package com.weekendtask.crudproduct.dao;

import com.weekendtask.crudproduct.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {
}
