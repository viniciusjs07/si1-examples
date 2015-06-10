package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Estilo {

	@Id
	@GeneratedValue
	private Long id;

	@Transient
	private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false)
	private String nome;

	public Estilo() {
	}

	public Estilo(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

}
