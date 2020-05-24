package com.bookstore.repos;

import com.bookstore.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo  extends JpaRepository<Inventory,Integer> {

    public Inventory findByBookId(Integer bookId);
}
