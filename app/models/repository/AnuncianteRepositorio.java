package models.repository;

import models.Anunciante;

public class AnuncianteRepositorio extends GenericRepositoryImpl<Anunciante> {
	
private static AnuncianteRepositorio instance;
	
	private AnuncianteRepositorio(){
		super(Anunciante.class);
	}
	
	
	public static AnuncianteRepositorio getInstance() {
		if (instance == null) {
			instance = new AnuncianteRepositorio();
		}
		return instance;
	}

}
