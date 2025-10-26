package T.Gopi.Textiles.com.product_service.Service;

import T.Gopi.Textiles.com.product_service.Dto.ProductDto;
import T.Gopi.Textiles.com.product_service.Entity.Product;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit; // Import for setting download URL expiry

@Service
public class ProductService {

    // In a production app, replace this with a proper database (Firestore/JPA Repository)
    private final Map<String, Product> productDB = new HashMap<>();

    // Inject the Firebase Storage bucket name from application.properties
    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    // Create product
    public Product createProduct(ProductDto dto) {
        Product p = new Product();
        String id = UUID.randomUUID().toString();
        p.setId(id);
        p.setName(dto.getName());
        p.setBarcode(dto.getBarcode());
        p.setCategory(dto.getCategory());
        p.setPrice(dto.getPrice());
        p.setGst(dto.getGst());
        p.setStockQuantity(dto.getStockQuantity());
        productDB.put(id, p);
        return p;
    }

    // Update product
    public Optional<Product> updateProduct(String id, ProductDto dto) {
        Product existing = productDB.get(id);
        if (existing == null) return Optional.empty();
        existing.setName(dto.getName());
        existing.setBarcode(dto.getBarcode());
        existing.setCategory(dto.getCategory());
        existing.setPrice(dto.getPrice());
        existing.setGst(dto.getGst());
        existing.setStockQuantity(dto.getStockQuantity());
        // Note: barcodeUrl is not updated here, as it's generated on demand
        return Optional.of(existing);
    }

    // Get all
    public List<Product> getAllProducts() {
        return new ArrayList<>(productDB.values());
    }

    // Get by ID
    public Optional<Product> getProductById(String id) {
        return Optional.ofNullable(productDB.get(id));
    }

    // Delete
    public boolean deleteProduct(String id) {
        return productDB.remove(id) != null;
    }

    // Find by barcode
    public Optional<Product> getProductByBarcode(String barcode) {
        return productDB.values().stream()
                .filter(p -> barcode.equals(p.getBarcode()))
                .findFirst();
    }

    // Deduct stock
    public Optional<Product> deductStock(String id, int quantity) {
        Product p = productDB.get(id);
        if (p == null || p.getStockQuantity() < quantity) return Optional.empty();
        p.setStockQuantity(p.getStockQuantity() - quantity);
        return Optional.of(p);
    }

    // Generate barcode, upload to Firebase, and set URL (UPDATED LOGIC)
    public Optional<Product> generateBarcodeSticker(String id) throws Exception {
        Product p = productDB.get(id);
        if (p == null) return Optional.empty();

        // 1. Determine or generate the barcode value
        String barcodeValue = p.getBarcode() != null && !p.getBarcode().isEmpty()
                ? p.getBarcode()
                : UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        p.setBarcode(barcodeValue);

        // 2. Generate the barcode image as a byte array (in-memory)
        BitMatrix matrix = new Code128Writer().encode(barcodeValue, BarcodeFormat.CODE_128, 400, 100);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        
        // Write the matrix data to the in-memory stream as a PNG
        MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream);
        byte[] barcodeImageBytes = pngOutputStream.toByteArray();

        // 3. Upload the byte array to Firebase Storage
        String barcodeUrl = uploadBarcodeToFirebase(p.getId(), barcodeImageBytes);
        
        // 4. Update the Product entity with the public URL
        p.setBarcodeUrl(barcodeUrl);
        
        return Optional.of(p);
    }

    /**
     * Uploads the barcode image byte array to Firebase Storage.
     * @param productId The ID of the product (used as the file name).
     * @param imageBytes The PNG image data.
     * @return The public, time-limited URL of the uploaded image.
     */
    private String uploadBarcodeToFirebase(String productId, byte[] imageBytes) {
        // Get the Firebase Storage Bucket
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        
        // Define the file path within the bucket
        String fileName = "barcodes/" + productId + "_barcode.png";
        
        // Upload the image
        // The bucket.create() returns a Blob object representing the uploaded file
        Blob blob = bucket.create(fileName, imageBytes, "image/png");

        // Generate a public, signed URL that expires (7 days for security)
        return blob.signUrl(7, TimeUnit.DAYS).toString();
    }
}