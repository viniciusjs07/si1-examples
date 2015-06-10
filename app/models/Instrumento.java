package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.common.base.Objects;

@Entity(name = "Instrumento")
public class Instrumento  {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "name", nullable = false)
	private String nome;

	public Instrumento() {

	}

	public Instrumento(String nome) {
		this.nome = nome;
	}

	public String getInstrumento() {
		return nome;
	}

	public void setInstrumento(String nome) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Instrumento)) {
			return false;
		}
		Instrumento novoInstrumento = (Instrumento) obj;

		return getInstrumento().equals(novoInstrumento.getInstrumento());
	}
	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.nome);
	}

}
