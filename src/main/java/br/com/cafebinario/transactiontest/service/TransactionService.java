package br.com.cafebinario.transactiontest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.cafebinario.transactiontest.pricipal.entities.ExemploAEntity;
import br.com.cafebinario.transactiontest.principal.repositories.ExemploAJpaRepository;
import br.com.cafebinario.transactiontest.secundary.entities.ExemploBEntity;
import br.com.cafebinario.transactiontest.secundary.repositories.ExemploBJpaRepository;

@Service
public class TransactionService {

	@Autowired
	private ExemploAJpaRepository exemploAJpaRepository;

	@Autowired
	private ExemploBJpaRepository exemploBJpaRepository;

	@Transactional
	public void exemploTransacao1() {
		saveA();
		saveB();
	}

	@Transactional
	public void exemploTransacao11() {
		saveA();
		saveA1();
	}

	@Transactional
	public void exemploTransacao12() {
		saveA1();
		saveAWithError();
	}

	@Transactional(propagation = Propagation.NESTED, rollbackFor = RuntimeException.class)
	public void exemploTransacao2() {
		saveA();
		saveBWithError();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveA() {
		exemploAJpaRepository.save(new ExemploAEntity(1L, "exemploA"));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveA1() {
		exemploAJpaRepository.save(new ExemploAEntity(2L, "exemploA1"));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void saveB() {
		exemploBJpaRepository.save(new ExemploBEntity(1L, "exemploB"));
	}

	@Transactional(propagation = Propagation.NESTED)
	public void saveAWithError() {
		saveA();
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.NESTED)
	public void saveBWithError() {
		saveB();
		throw new RuntimeException();
	}

	@Transactional
	public void deleteAll() {
		exemploAJpaRepository.deleteAll();
		exemploBJpaRepository.deleteAll();
	}

	public Long count() {
		final Long countA = exemploAJpaRepository.count();
		final Long countB = exemploBJpaRepository.count();
		System.out.println("countA=" + countA + ", countB=" + countB);
		return countA + countB;
	}
}
