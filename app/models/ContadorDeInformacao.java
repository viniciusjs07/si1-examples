package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "ContadorDeInformacao")
public class ContadorDeInformacao {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private int resolvido, naoResolvido;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ContadorDeInformacao() {
		this.resolvido = 0;
		this.naoResolvido = 0;

	}

	public int getNaoResolvido() {
		return naoResolvido;
	}

	public int getResolvido() {
		return resolvido;
	}

	public void setNaoResolvido(int naoResolvido) {
		this.naoResolvido = naoResolvido;
	}

	public void setResolvido(int resolvido) {
		this.resolvido = resolvido;
	}

}
