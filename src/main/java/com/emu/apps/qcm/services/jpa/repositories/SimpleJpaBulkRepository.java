package com.emu.apps.qcm.services.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface SimpleJpaBulkRepository<T, ID extends Serializable>
  extends JpaRepository<T, ID> {

  void bulkSave(Iterable<T> entities, int batchSize);
}