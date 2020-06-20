package com.weekendtask.crudproduct.controller;

import com.weekendtask.crudproduct.dao.ProductRepo;
import com.weekendtask.crudproduct.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private Product product;

    @GetMapping("/allproducts")
    public Iterable<Product> getProducts(){
        return productRepo.findAll();
    }

    @PostMapping("/addproduct")
    public Product addProduct(@RequestBody Product product){
        return productRepo.save(product);
    }

    @PutMapping("/updateproduct/{productId}")
    public Product updateProduct(@RequestBody Product newProduct, @PathVariable int productId ) {
        Product findProduct = productRepo.findById(productId).get();// jika tidak ada object yg sesuai maka otomatis return no vlaue present
        return productRepo.save(newProduct);
//        Cara kedua dengan map
//        return productRepo.findById(productId)
//                .map(employee -> {
//                    product.setProductName(newProduct.getProductName());
//                    product.setPrice(newProduct.getPrice());
//                    product.setImageLink(newProduct.getImageLink());
//                    return productRepo.save(product);
//                })
//                .orElseGet(() -> {
//                    product.setId(productId);
//                    return productRepo.save(newProduct);
//                });
    }

}
