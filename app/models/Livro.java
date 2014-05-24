package models;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.common.base.Objects;

@Entity(name = "Livro")
@Table(name = "TB_Livro")
public class Livro {

	@Id
	@SequenceGenerator(name = "LIVRO_SEQUENCE", sequenceName = "LIVRO_SEQUENCE", allocationSize = 1, initialValue = 0)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@ManyToMany
	private List<Autor> autores;

	@Column
	private String nome;

	public Livro() {
	}

	public Livro(Autor... autores) {
		this.setAutores(Arrays.asList(autores));

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Livro)) {
			return false;
		}
		Livro outroLivro = (Livro) obj;
		return Objects.equal(outroLivro.getNome(), this.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getNome());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
