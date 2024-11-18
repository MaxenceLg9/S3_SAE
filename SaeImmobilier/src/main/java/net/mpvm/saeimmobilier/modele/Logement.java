package net.mpvm.saeimmobilier.modele;

import eu.hansolo.tilesfx.skins.PercentageTileSkin;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Logement {
	private String lieuImmeuble;
	private Map<Locataire, Float> repartitionElectricite;
	private Map<Locataire, Float> repartitionOrduresMenageres;
	private Map<Locataire, Float> repartitionEntretien;
	private int idLocation;
	private ArrayList<Travaux> travaux;
	private ArrayList<Bail> baux;
	
	public Logement(int idLocation, String lieu) {
		this.idLocation=idLocation;
		this.lieuImmeuble=lieu;
		this.travaux=new ArrayList<>();
		this.baux=new ArrayList<>();
		this.repartitionOrduresMenageres = this.repartitionElectricite = this.repartitionEntretien = new TreeMap<>();
	}
	
	public String getLieuImmeuble() {
		return this.lieuImmeuble;
	}
	
	public int getIdLocation() {
		return this.idLocation;
	}

	public ArrayList<Travaux> getTravaux() {
		return this.travaux;
	}

	public ArrayList<Bail> getBaux() {
		return this.baux;
	}

	public Map<Locataire, Float> getRepartitionElectricite() {
		return this.repartitionElectricite;
	}

	public Map<Locataire, Float> getRepartitionEntretien() {
		return this.repartitionEntretien;
	}

	public Map<Locataire, Float> getRepartitionOrduresMenageres() {
		return this.repartitionOrduresMenageres;
	}
	
	public void setIdLocation(int idLocation) {
		this.idLocation=idLocation;
	}

	public void ajouterTravail(Travaux travail) {
		this.travaux.add(travail);
	}

	public void ajouterBail(Bail bail) {
		this.baux.add(bail);
	}

	public void setRepartitionElectricite(Float pourcentage, Locataire locataire) throws IllegalArgumentException{
		if (pourcentage<0 || pourcentage>1) {
			throw new IllegalPercentException("Le pourcentage doit être compris entre 0 et 1");
		}
		double sum = pourcentage.doubleValue();
		sum += this.repartitionElectricite.values().stream().mapToDouble(Float::doubleValue).sum();
		if(sum > 1F){
			throw new IllegalPercentException("La somme des valeurs de répartitions d'ordures ménagères ne doit pas dépasser 1");
		}
		this.repartitionElectricite.put(locataire, pourcentage);
	}

	public void setRepartitionOrduresMenageres(Float pourcentage, Locataire locataire) throws IllegalArgumentException{
		if (pourcentage<0 || pourcentage>1) {
			throw new IllegalPercentException("Le pourcentage doit être compris entre 0 et 1");
		}
		double sum = pourcentage.doubleValue();
		sum += this.repartitionOrduresMenageres.values().stream().mapToDouble(Float::doubleValue).sum();
		if(sum > 1F){
			throw new IllegalPercentException("La somme des valeurs de répartitions d'ordures ménagères ne doit pas dépasser 1");
		}
		this.repartitionOrduresMenageres.put(locataire, pourcentage);
	}

	public void setRepartitionEntretien(Float pourcentage, Locataire locataire) throws IllegalArgumentException{
		if (pourcentage<0 || pourcentage>1) {
			throw new IllegalPercentException("Le pourcentage doit être compris entre 0 et 1");
		}
		double sum = pourcentage.doubleValue();
		sum += this.repartitionEntretien.values().stream().mapToDouble(Float::doubleValue).sum();
		if(sum > 1F){
			throw new IllegalPercentException("La somme des valeurs de répartitions d'ordures ménagères ne doit pas dépasser 1");
		}
		this.repartitionEntretien.put(locataire, pourcentage);
	}

	public void setLieuImmeuble(String lieu) {
		this.lieuImmeuble=lieu;
	}

	private static class IllegalPercentException extends ArithmeticException{
		public IllegalPercentException(String message){
			super(message);
		}
	}
}
