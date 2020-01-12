package it.polito.tdp.meteo;

import java.time.Month;

import it.polito.tdp.meteo.bean.Citta;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		Citta citta = new Citta("Milano") ;
		System.out.println(m.getLeCitta());
		System.out.println(m.getUmiditaMedia( Month.JUNE, citta));
		
		System.out.println(m.trovaSequenza(5));
		
//		System.out.println(m.trovaSequenza(4));
	}

}
