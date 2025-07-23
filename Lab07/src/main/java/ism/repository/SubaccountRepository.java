package ism.repository;

import ism.entity.Subaccount;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SubaccountRepository extends JpaRepository<Subaccount, Long> {}