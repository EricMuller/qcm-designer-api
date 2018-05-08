package com.emu.apps.qcm.services.repositories;

import com.emu.apps.qcm.services.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileJpaRepository extends JpaRepository<UserProfile, Integer> {
    UserProfile findByPrincipalId(String principalId);
    UserProfile findByUsername(String userName);
}