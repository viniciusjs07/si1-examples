package models;

import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;

public class GenericDAO {

	public boolean salvar(Object e) {
		JPA.em().persist(e);
		return true;
	}

	public void espelhar() {
		JPA.em().flush();
	}

	public void atualizar(Object e) {
		JPA.em().merge(e);
	}

	public <T> T buscarPeloId(Class<T> clazz, Long id) {
		return JPA.em().find(clazz, id);
	}

	public <T> List<T> bucarTodosPelaClasse(String clazz) {
		String hql = "FROM " + clazz;
		Query hqlQuery = JPA.em().createNamedQuery(hql);
		return hqlQuery.getResultList();
	}

	public void deletaPeloId(Class classe, Long id) {
		JPA.em().remove(JPA.em().find(classe, id));
	}

	public void deleta(Object objeto) {
		JPA.em().remove(objeto);
	}
}
