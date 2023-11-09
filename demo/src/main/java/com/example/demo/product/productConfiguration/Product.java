package com.example.demo.product.productConfiguration;

import jakarta.persistence.*;

@Entity
@Table(name="products")
public class Product {

    @Id
    @SequenceGenerator(
            name="product_sequence",
            sequenceName="product_sequence",
            allocationSize=1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )

    private Long id;
    private String name;
    private String description;
    private double price;
    private String unit;

    //презен конструктор без който базата данни даваше грешка
    public Product() {
    }

    public Product(long id, String name, String description, double price, String unit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.unit = unit;
    }

    public Product(String name, String description, double price, String unit) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                '}';
    }
}
