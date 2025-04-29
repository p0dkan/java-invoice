package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class Invoice {
    //    private Collection<Product> products = new ArrayList<>();
    private Map<Product, Integer> productQuantity = new LinkedHashMap<>();
    private String invoiceNumber = "0";

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        productQuantity.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : productQuantity.keySet()) {
            BigDecimal quantity = new BigDecimal(productQuantity.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : productQuantity.keySet()) {
            BigDecimal quantity = new BigDecimal(productQuantity.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public String getInvoiceAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Faktura: ").append(invoiceNumber).append("\n");

        for (Map.Entry<Product, Integer> entry : productQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();

            sb.append(product.getName())
                    .append(", ").append(quantity)
                    .append(" szt., ").append(product.getPrice().setScale(2))
                    .append("\n");
        }
        sb.append("Liczba pozycji: ").append(productQuantity.size());

        return sb.toString();
    }

    public static void main(String[] args){
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("ASD");
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        System.out.println(invoice.getInvoiceAsString());
    }
}
