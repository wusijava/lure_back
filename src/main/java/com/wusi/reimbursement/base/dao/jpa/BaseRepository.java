package com.wusi.reimbursement.base.dao.jpa;

import com.wusi.reimbursement.common.Identifiable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author wusi
 * @date 2019-01-19 11:19
 **/
@NoRepositoryBean
public interface BaseRepository<T extends Identifiable<ID>, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
