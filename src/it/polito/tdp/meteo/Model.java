package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private List<Citta> leCitta;
	private List<Citta> best;
	
	public List<Citta> getLeCitta(){
		return leCitta;
	}
	public Model() {

		MeteoDAO dao = new MeteoDAO();
		this.leCitta = dao.getAllCitta();
	}

	public Double getUmiditaMedia(Month mese, Citta citta) {

		MeteoDAO dao = new MeteoDAO();
		return dao.getUmiditaMedia(mese, citta);
		
	}
	//Ricorsione pubblica
	public List<Citta> calcolaSequenza(Month mese) {
		
		List<Citta> parziale = new ArrayList<Citta>();
		this.best = null;
		
		MeteoDAO dao = new MeteoDAO();
		
		for(Citta c: leCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c));
		}
		
		cerca(parziale, 0);
		
		return best;
		
	}
	 
	//Ricorsione privata
	private void cerca(List<Citta> parziale, int livello) {
		
		//Caso terminale: numero di gg massimo
		if(livello == NUMERO_GIORNI_TOTALI) {
			
			//Deve calcolare il costo
			Double costo = calcolaCosto(parziale);
			
			if(best == null || costo < calcolaCosto(best))			
				best = new ArrayList<Citta>(parziale);
			
			//Aggiornamento del costo ottimale
			
		} else {
		
		 //Ricorsione
			for(Citta prova: leCitta) {
				
				//Controllo se la citta va bene
				if(aggiuntaValida(prova, parziale)) {
					
					//Aggiungo la citta alla lista
					parziale.add(prova);
					//Ricorsione vera e propria
					cerca(parziale, livello+1);
					//Backtrack
					parziale.remove(parziale.size()-1);
					
				}
				
			}
		}
		
		
		
	}
	
	public Double calcolaCosto(List<Citta> parziale) {
		
		double costo = 0.0;
		
		for(int giorno = 1; giorno <= NUMERO_GIORNI_TOTALI; giorno ++) {
			
			//Dove mi trovo?
			Citta c = parziale.get(giorno-1);
			
			//Che umidita c'è in quella citta quel giorno?
			double umidita = c.getRilevamenti().get(giorno-1).getUmidita();
			
			costo += umidita;
			
		}
		
		for(int giorno = 2;  giorno <= NUMERO_GIORNI_TOTALI; giorno ++) {
			
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo += COST;
			}
		}
		
		return costo;
	}
	

	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		
		int conta = 0;
		
		//Verifico i giorni massimi di una citta
		
		for(Citta precedente: parziale) {
			if(precedente.equals(prova)) 
				conta++; //Conto quante volte è stata selezionata una citta di seguito
		}
		
		if(conta >= NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		//Verifico i giorni minimi
		
		if(parziale.size() == 0) //primo giorno
			return true;
		
		if(parziale.size() == 1 || parziale.size() == 2) //Secondo e terzo giorno
			//Non posso cambiare la scelta
			return parziale.get(parziale.size()-1).equals(prova);
		
		if(parziale.get(parziale.size()-1).equals(prova)) { //Giorni successivi, posso rimanere
			return true;
		}
		
		
		//Controllo se posso cambiare citta
		
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && 
			parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3))) {
			
			return true;
		}
			
		return false;
		
		
		
	}
	
		
	
	public String trovaSequenza(int mese) {

		return "TODO!";
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

}
