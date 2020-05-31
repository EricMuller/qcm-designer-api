package com.emu.apps.qcm.infrastructure.adapters.jpa.repositories;

import com.emu.apps.qcm.infrastructure.adapters.jpa.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfile, Integer> {
    UserProfile findByPrincipalId(String principalId);
    UserProfile findByUsername(String userName);
}