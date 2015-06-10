package models.repository;

import models.Anuncio;


public class AnuncioRepositorio  extends GenericRepositoryImpl<Anuncio> {
	
	
private static AnuncioRepositorio instance;
	
	private AnuncioRepositorio(){
		super(Anuncio.class);
	}
	
	
	public static AnuncioRepositorio getInstance() {
		if (instance == null) {
			instance = new AnuncioRepositorio();
		}
		return instance;
	}

}
