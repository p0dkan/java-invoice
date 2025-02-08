package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
//    private Collection<Product> products = new ArrayList<>();
    private HashMap<Product, Integer> productQuantity = new HashMap<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must have a name");
        }
        this.productQuantity.put(product,1);
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
}
