package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.google.common.base.Objects;

// Entidade que representa uma Tabela no Banco de Dados
// Sempre por o nome na Entidade, pois é esse nome que se vai usar nas pesquisas do Banco de Dados
@Entity
public class Autor {

	// Gerador de Sequencia para o Id
	@Id
	@GeneratedValue
	private Long id;

	// Nome do Autor dos Livros
	@Column(unique = true)
	private String nome;

	// Relação Muitos para Muitos
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable
	private List<Livro> livros;

	// Construtor Vazio para o Hibernate criar os objetos
	public Autor() {
		this.livros = new ArrayList<Livro>();
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
