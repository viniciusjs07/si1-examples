package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "TESTESI")
public class TesteSIHibernate {

	@Id
	@SequenceGenerator(name = "Token_generator", sequenceName = "test_sequence", allocationSize = 1, initialValue = 1)
	private long Id;

	@Column(name = "CI_TESTESI")
	public String nome;

	public TesteSIHibernate() {

	}
}
