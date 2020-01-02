package hu.hibridlevel.restproduct.dto;

import java.util.List;
import java.util.Objects;

public class StockDto {

    private Integer id;

    private String name;

    private List<ProductDto> products;

    public StockDto(){

    }

    public StockDto(Integer id, String name, List<ProductDto> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public StockDto(String name, List<ProductDto> products) {
        this.name = name;
        this.products = products;
    }

    public StockDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockDto stockDto = (StockDto) o;
        return Objects.equals(id, stockDto.id) &&
                Objects.equals(name, stockDto.name) &&
                Objects.equals(products, stockDto.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, products);
    }

    @Override
    public String toString() {
        return "StockDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productDtos=" + products +
                '}';
    }
}
