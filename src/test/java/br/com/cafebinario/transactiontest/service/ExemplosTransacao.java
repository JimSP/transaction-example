package br.com.cafebinario.transactiontest.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExemplosTransacao {

	@Autowired
	private TransactionService transactionService;

	@Test
	public void testExemplo1() {
		transactionService.deleteAll();
		transactionService.exemploTransacao1();
		final Long count = transactionService.count();
		Assert.assertEquals(2L, count.longValue());
	}
	
	@Test
	public void testExemplo11() {
		transactionService.deleteAll();
		transactionService.exemploTransacao11();
		final Long count = transactionService.count();
		Assert.assertEquals(2L, count.longValue());
	}
	
	@Test
	public void testExemplo12() {
		transactionService.deleteAll();
		try {
			transactionService.exemploTransacao12();
		}catch (RuntimeException e) {
			e.printStackTrace();
		}
		final Long count = transactionService.count();
		Assert.assertEquals(0L, count.longValue());
	}
	
	@Test
	@Transactional
	public void testExemplo2() {
		transactionService.deleteAll();
		try {
			transactionService.exemploTransacao2();
		}catch (RuntimeException e) {
			e.printStackTrace();
		}
		final Long count = transactionService.count();
		Assert.assertEquals(0L, count.longValue());
	}
	
	@Test
	public void testExemploWithErrorASaveB() {
		transactionService.deleteAll();
		try {
			transactionService.saveAWithError();
		}catch (RuntimeException e) {
			transactionService.saveB();
		}
		final Long count = transactionService.count();
		Assert.assertEquals(1L, count.longValue());
	}
}
