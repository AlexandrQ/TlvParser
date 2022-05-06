package main.java.models;

import main.java.TypeApplicable;
import main.java.enums.Tag;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


public class ItemModel implements Serializable, TypeApplicable {
    private String name;
    private Long price;
    private BigDecimal quantity;
    private Long sum;

    @Override
    public void applyValueByType(Tag tag, Object value) {
        switch (tag) {
            case PRODUCT -> setName((String) value);
            case PRICE -> setPrice((Long) value);
            case QUANTITY -> setQuantity((BigDecimal) value);
            case TOTAL_PRICE -> setSum((Long) value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemModel itemModel = (ItemModel) o;
        return Objects.equals(name, itemModel.name) && Objects.equals(price, itemModel.price) && Objects.equals(quantity, itemModel.quantity) && Objects.equals(sum, itemModel.sum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, quantity, sum);
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", price:" + price +
                ", quantity:" + quantity +
                ", sum:" + sum +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
}
