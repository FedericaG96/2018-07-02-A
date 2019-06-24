package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;
import javafx.scene.control.TextField;

public class Model {
	
	ExtFlightDelaysDAO dao;
	Graph<Airport, DefaultWeightedEdge> grafo;
	List<Airport> allAirports;
	Map<Integer, Airport> airportIdMap;
	List<Rotta> rotte;
	
	//ricorsione
	List<Airport> best;
	Double migliaMax = 0.0;	
	Double migliaConsumate ;
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>( DefaultWeightedEdge.class);
		
		airportIdMap = new HashMap<>();
		allAirports = dao.loadAllAirports(airportIdMap);
	}

	public void creaGrafo(Integer distanzaMinima) {
		
		rotte = dao.getRotte(distanzaMinima, airportIdMap);
		Graphs.addAllVertices(grafo, airportIdMap.values());
		
		for(Rotta r : rotte) {
			DefaultWeightedEdge e = grafo.getEdge(r.getSource(), r.getDestination());
			
			if(e == null) {
				Graphs.addEdge(grafo,r.getSource(), r.getDestination(),r.getDistanza());
			}
			else {
				double peso = (grafo.getEdgeWeight(e) + r.getDistanza() )/2;
				grafo.setEdgeWeight(e, peso);
			}
			
		}
		
	}
	
	public List<Airport> getAeroporti(){
		List<Airport> airports= new ArrayList<>();
		for(Airport a : this.airportIdMap.values()) {
			airports.add(a);
		}
		return airports;
	}

	public List<Rotta> getConnessi(Airport a) {
		List<Rotta> connessi= new ArrayList<>();
		
		for(Rotta r : rotte) {
			if(r.getSource().equals(a)) {
				connessi.add(r);
			}
		}
		Collections.sort(connessi);
		return connessi;
	}

	public List<Airport> getPercorso(Double migliaMax, Airport aeroporto) {
		migliaConsumate = 0.0;
		this.migliaMax = migliaMax;
		
		best = new ArrayList<>();
		List<Airport> parziale = new ArrayList<>();
		
		best.add(aeroporto);
		parziale.add(aeroporto);
		
		this.recursive(1, parziale, migliaConsumate);
		
		return best;
	}

	private void recursive(int livello, List<Airport> parziale, Double migliaConsumate) {
		
		boolean trovato = false;
		
		Airport ultimo = parziale.get(parziale.size()-1);
		List<Airport> vicini = Graphs.neighborListOf(grafo, ultimo);
		
		
		for(Airport a : vicini) {
			if(!parziale.contains(a)) {
				
				if(migliaConsumate + grafo.getEdgeWeight(grafo.getEdge(ultimo, a))<= migliaMax ) {
					trovato = true;
					parziale.add(a);
					migliaConsumate += grafo.getEdgeWeight(grafo.getEdge(ultimo, a));
					this.recursive(livello+1, parziale, migliaConsumate);
					parziale.remove(parziale.size()-1);
					migliaConsumate -= grafo.getEdgeWeight(grafo.getEdge(ultimo, a));
			}
		}
		}
		if(trovato == false ) {
			if(parziale.size() > best.size()) {
				best = new ArrayList<>(parziale);
			}
		}
		
	}
	
	public double getDistanzaPercorsa(){
		double distanzaPercorsa = 0.0;
		for(int i = 1; i< best.size(); i++) {
			distanzaPercorsa += grafo.getEdgeWeight(grafo.getEdge(best.get(i-1), best.get(i)));
		}
		return distanzaPercorsa;
	}

	public Integer getGrafoArchi() {
		// TODO Auto-generated method stub
		return this.grafo.edgeSet().size();
	}

}
