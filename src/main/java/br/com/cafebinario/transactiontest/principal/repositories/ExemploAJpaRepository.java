package br.com.cafebinario.transactiontest.principal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cafebinario.transactiontest.pricipal.entities.ExemploAEntity;

public interface ExemploAJpaRepository extends JpaRepository<ExemploAEntity, Long>{

}
