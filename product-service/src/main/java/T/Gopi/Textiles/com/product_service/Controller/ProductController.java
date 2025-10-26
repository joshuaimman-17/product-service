package T.Gopi.Textiles.com.product_service.Controller;

import T.Gopi.Textiles.com.product_service.Dto.ProductDto;
import T.Gopi.Textiles.com.product_service.Entity.Product;
import T.Gopi.Textiles.com.product_service.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public String status() {
        return "✅ Product Service running successfully!";
    }

    @PostMapping("/create")
    public ResponseEntity<Product> create(@RequestBody ProductDto dto) {
        return new ResponseEntity<>(service.createProduct(dto), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody ProductDto dto) {
        return service.updateProduct(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.deleteProduct(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id) {
        return service.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getByBarcode(@PathVariable String barcode) {
        return service.getProductByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/deduct-stock/{qty}")
    public ResponseEntity<Product> deductStock(@PathVariable String id, @PathVariable int qty) {
        return service.deductStock(id, qty)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/{id}/generate-barcode")
    public ResponseEntity<Product> generateBarcode(@PathVariable String id) {
        try {
            return service.generateBarcodeSticker(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
