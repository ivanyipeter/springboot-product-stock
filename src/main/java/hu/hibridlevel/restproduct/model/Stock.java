package hu.hibridlevel.restproduct.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Stock {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stock", orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Product> products;

    public Stock() {
    }

    public Stock(String name) {
        this.name = name;
    }

    public Stock(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setStock(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        for (Product p : products) {
            p.setStock(this);
        }
        this.products = products;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id) &&
                Objects.equals(name, stock.name) &&
                Objects.equals(products, stock.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, products);
    }


//    @Override
//    public String toString() {
//        return "Stock{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", products=" + products +
//                '}';
//    }

}
