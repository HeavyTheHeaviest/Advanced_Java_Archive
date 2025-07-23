package ism.repository;

import ism.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {}