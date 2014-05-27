package models;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.google.common.base.Objects;

// Entidade que representa uma Tabela no Banco de Dados
@Entity(name = "Autor")
public class Autor {

	@Id
	// Gerador de Sequencia para o Id
	@SequenceGenerator(name = "AUTOR_SEQUENCE", sequenceName = "AUTOR_SEQUENCE", allocationSize = 1, initialValue = 0)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	// Nome do Autor dos Livros
	@Column
	private String nome;

	// Relação Muitos para Muitos
	@ManyToMany
	@JoinTable
	private List<Livro> livros;

	// Construtor Vazio para o Hibernate criar os objetos
	public Autor() {
	}

	public Autor(Livro... livros) {
		this.livros = Arrays.asList(livros);
	}

	public List<Livro> getLivros() {
		return livros;
	}

	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
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
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Autor)) {
			return false;
		}
		Autor outroAutor = (Autor) obj;
		return Objects.equal(outroAutor.getNome(), this.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.nome);
	}
}
