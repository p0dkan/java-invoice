package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class Invoice {
    //    private Collection<Product> products = new ArrayList<>();
    private HashMap<Product, Integer> productQuantity = new HashMap<>();
    private String invoiceNumber = "0";

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must have a name");
        }
        this.productQuantity.put(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (quantity <= 0 || product == null) {
            throw new IllegalArgumentException("Product quantity cannot be zero and product must have a name");
        }
        this.productQuantity.put(product, quantity);
    }

    public BigDecimal getNetPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (Product product : productQuantity.keySet()) {

            total = total.add(product.getPrice().multiply(new BigDecimal(productQuantity.get(product))));
        }
        return total;
    }

    public BigDecimal getTax() {
        BigDecimal totalTax = new BigDecimal(0);
        for (Product product : productQuantity.keySet()) {
            totalTax = totalTax.add(product.getPrice().multiply(product.getTaxPercent()).multiply(new BigDecimal(productQuantity.get(product))));
        }
        return totalTax;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Product product : productQuantity.keySet()) {
            total = total.add(product.getPriceWithTax().multiply(new BigDecimal(productQuantity.get(product))));
        }
        return total;
    }

    public String getInvoiceAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Faktura: ").append(invoiceNumber).append("\n");

        for (Map.Entry<Product, Integer> entry : productQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            sb.append(product.getName())
                    .append(", ").append(quantity)
                    .append(" szt., ").append(product.getPrice().setScale(2))  // Cena z dwoma miejscami po przecinku
                    .append("\n");
        }
        sb.append("Liczba pozycji: ").append(productQuantity.size());

        return sb.toString();
    }

    public static void main(String[] args){
        Invoice invoice = new Invoice();
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        System.out.println(invoice.getInvoiceAsString());
    }
}
