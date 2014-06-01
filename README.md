Exemplo de uso Hibernate
=========

O Hibernate é um framework para o mapeamento objeto-relacional escrito na linguagem Java. Este framework facilita o mapeamento dos atributos entre uma base tradicional de dados relacionais e o modelo objeto de uma aplicação, sem uma única escrita SQL por parte do desenvolvedor.

> Instalação

Detalhes sobre a instalação podem ser vistos [aqui] [1]:


> Utilização

- Suas classes à serem persistidas devem conter um @Entity
- togos os atributos private devem possuir get e set
- a classe deve possuir um construtor vazio
- preencher a classe adequadamente com anotações @...
- exemplo de uma Entidade [aqui] [2]

##Relacionamentos

#### OneToOne - Relação de Um para Um

           
           
```sh 
@Entity
public class Pessoa {
    @OneToOne(mappedBy="pessoa", cascade=CascadeType.ALL)
    private Endereco endereco; // uma pessoa tem apenas um endereço
    ...
} 
@Entity
public class Endereco {
    @OneToOne
    private Pessoa pessoa;
    ...
}
```

#### OneToMany
```sh
@Entity
public class Aluno {
    @OneToMany(mappedBy = "aluno")  
    List<Livro> livros;  // um aluno tem vários livros, e um livro só pode pertencer a um aluno
    ...
}
@Entity
public class Livro {
    @ManyToOne  
    @JoinColumn(name = "aluno_id")  //livro será o dono da relação, então na tabela Livro terá a coluna aluno_id
    Aluno aluno;
    ...
}
```

#### ManyToMany 
- exemplo [aqui] [3]

Duvidas
=========
Utilizem o Piazza!





[1]:http://www.playframework.com/documentation/2.1.0/JavaJPA
[2]:https://github.com/marcosvcp/si1-examples/blob/master/app/models/Autor.java
[3]:https://github.com/marcosvcp/si1-examples/blob/master/app/models/Autor.java#L35

