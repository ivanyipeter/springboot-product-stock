package hu.hibridlevel.restproduct.repository;

import hu.hibridlevel.restproduct.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT a FROM Product a WHERE a.count > 0")
    List<Product> getAvailableProducts();

    @Query("select a.id from Product a where a.name = :name")
    Integer getIdByProductName(@Param("name") String name);

    Optional<Product> getProductByName(String name);

    @Query("SELECT a FROM Product a WHERE stock_id = :id")
    List<Product> getProductsByStockId(@Param("id") Integer id);

    @Query("SELECT a FROM Stock b JOIN b.products a WHERE b.id = :id")
    List<Product> getAllProductsFromStockWithStockId(@Param("id") Integer id);



}
