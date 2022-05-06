package main.java.models;

import main.java.TypeApplicable;
import main.java.enums.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DocModel implements Serializable, TypeApplicable {

    private String dateTime;
    private Long orderNumber;
    private String customerName;
    private List<ItemModel> items = new ArrayList<>();

    @Override
    public void applyValueByType(Tag tag, Object value) {
        switch (tag) {
            case DATETIME -> setDateTime((String) value);
            case ORDER_NUMBER -> setOrderNumber((Long) value);
            case CUSTOMER -> setCustomerName((String) value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocModel docModel = (DocModel) o;
        return Objects.equals(dateTime, docModel.dateTime) && Objects.equals(orderNumber, docModel.orderNumber) && Objects.equals(customerName, docModel.customerName) && Objects.equals(items, docModel.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, orderNumber, customerName, items);
    }

    @Override
    public String toString() {
        return "DocModel{" +
                "dateTime='" + dateTime + '\'' +
                ", orderNumber=" + orderNumber +
                ", customerName='" + customerName + '\'' +
                ", items=" + items +
                '}';
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}
