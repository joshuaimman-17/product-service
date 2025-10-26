package T.Gopi.Textiles.com.product_service.Entity;

public class Product {
    private String id;
    private String name;
    private String barcode;
    private String category;
    private double price;
    private double gst;
    private int stockQuantity;
    private String barcodeUrl;

    public Product() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getGst() { return gst; }
    public void setGst(double gst) { this.gst = gst; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getBarcodeUrl() { return barcodeUrl; }
    public void setBarcodeUrl(String barcodeUrl) { this.barcodeUrl = barcodeUrl; }
}
