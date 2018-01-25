package br.com.cafebinario.transactiontest.secundary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cafebinario.transactiontest.secundary.entities.ExemploBEntity;

public interface ExemploBJpaRepository extends JpaRepository<ExemploBEntity, Long>{

}
