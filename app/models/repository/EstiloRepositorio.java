package models.repository;

import models.Estilo;


public class EstiloRepositorio extends GenericRepositoryImpl<Estilo>{
	
private static EstiloRepositorio instance;
	
	private EstiloRepositorio(){
		super(Estilo.class);
	}
	
	
	public static EstiloRepositorio getInstance() {
		if (instance == null) {
			instance = new EstiloRepositorio();
		}
		return instance;
	}


}
