package com.bookstore.repos;

import com.bookstore.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

@Repository
public interface InventoryRepo  extends JpaRepository<Inventory,Integer> {

    public Inventory findByBookId(Integer bookId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query(value = "select inv from Inventory  inv where inv.bookId  = ?1")
    public Inventory findByBookIdWithLock(Integer id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    public Inventory save(Inventory inventory);
}
