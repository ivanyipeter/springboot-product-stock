package hu.hibridlevel.restproduct.repository;

import hu.hibridlevel.restproduct.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    Optional<Stock> getStockByName(String name);

    @Query("SELECT DISTINCT a FROM Stock a JOIN FETCH a.products")
    List<Stock> getAllStockWithJoinFetchForTesting();
}
