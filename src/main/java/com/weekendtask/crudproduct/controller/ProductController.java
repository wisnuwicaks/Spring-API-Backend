package com.weekendtask.crudproduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weekendtask.crudproduct.dao.ProductRepo;
import com.weekendtask.crudproduct.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private String uploadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";

    @Autowired
    private ProductRepo productRepo;


    @GetMapping("/allproducts")
    public Iterable<Product> getProducts() {
        return productRepo.findAll();
    }

    @PostMapping("/addproduct")
    public Product addProduct(@RequestBody Product product) {
        return productRepo.save(product);
    }

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userData") String productString) throws JsonMappingException, JsonProcessingException {
        Date date = new Date();

        Product product = new ObjectMapper().readValue(productString, Product.class);

        // Register / POST product ke database, beserta dengan link ke imageLink

        String fileExtension = file.getContentType().split("/")[1];
        String newFileName = "PROD-" + date.getTime() + "." + fileExtension;

        // Get file's original name || can generate our own
        String fileName = StringUtils.cleanPath(newFileName);

        // Create path to upload destination + new file name
        Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/products/download/")
                .path(fileName).toUriString();

        product.setImageLink(fileDownloadUri);

        productRepo.save(product);

        // http://localhost:8080/documents/download/PROD-123456.jpg

        return fileDownloadUri;
    }


    @PutMapping("/updateproduct/{productId}")
    public Product updateProduct(@PathVariable int productId,@RequestParam("userData") String productString) throws JsonMappingException, JsonProcessingException {
        Product findProduct = productRepo.findById(productId).get();// jika tidak ada object yg sesuai maka otomatis return no vlaue present
        Product product = new ObjectMapper().readValue(productString, Product.class);
        return productRepo.save(product);

    }

    @PutMapping("/edit/{productId}")
    public Product editProduct(@RequestBody Product newProduct, @PathVariable int productId) {
        Product findProduct = productRepo.findById(productId).get();// jika tidak ada object yg sesuai maka otomatis return no vlaue present
        return productRepo.save(newProduct);
//        Cara kedua dengan map
//        return productRepo.findById(productId)
//                .map(product -> {
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
    @DeleteMapping("/delete/{deleteId}")
    public void deleteProduct(@PathVariable int deleteId) {
        Product findProduct = productRepo.findById(deleteId).get();
        productRepo.deleteById(deleteId);
    }


}
