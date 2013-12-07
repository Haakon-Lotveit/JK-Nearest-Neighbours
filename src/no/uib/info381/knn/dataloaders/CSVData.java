package no.uib.info381.knn.dataloaders;

import java.util.LinkedList;
import java.util.List;

/**
 * Denne klassen er en basisklasse for å lage objekter av rader fra CSVfiler.
 * Den er ment til å være en basisklasse andre klasser kan arve fra.
 * 
 * @author haakon
 *
 */
public class CSVData {
	// Konstruktører
	
	/**
	 * Denne er ikke ment til å brukes, men gir ingen feilmeldinger.
	 * Grunnen er at når lager et objekt vha. denne metoden, får en ikke noe data i objektet, og den er verdiløs for videre bruk.
	 * Derfor skriver den ut en feilmelding når den blir kalt, men du får lov til å bruke den.
	 */
	protected CSVData(){
		System.err.printf("[ERROR] Standard constructuor of class %s called.%n", this.getClass().toString());
	}
	
	/**
	 * En naiv konstruktør som ikke gjør noe form for dataskalering. Dette må i så fall gjøres før konstruktøren blir kalt.
	 * TODO: En fabrikkmetode som skalerer ned etter gitte kriterier hadde vært noe. Skal i så fall gjøres etterpå.
	 * @param csvData
	 */
	public CSVData(String[] csvData, int classificationIndex){
		this.attributes = new Double[csvData.length - 1];
		this.classification = csvData[classificationIndex];
		
		for(int i = 0; i < classificationIndex; ++i){
			attributes[i] = Double.parseDouble(csvData[i]);
		}
		for(int i = classificationIndex; i < attributes.length; ++i){
			attributes[i] = Double.parseDouble(csvData[i+1]);
		}
	}
	
	
	// Felt
		
	/**
	 * Denne holder på attributtene til objektet. Alt er tall, så jeg tenkte at flyttall med dobbel presisjon får funke for nå.
	 */
	private Double[] attributes;
	/**
	 * Klassifiseringen av dette objektet.
	 */
	private String classification;
	

	// Metoder
	
	/**
	 * Hent ut attributtet gitt ved index.
	 * Attributtene er indekserte på 0.
	 * @param index
	 * @throws IndexOutOfBoundsException hvis indeks er ugyldig (mindre enn 0 eller ikke mindre enn størrelsen.
	 * @return attributtet som en Double.
	 */
	public Double getAttribute(int index){
		if(index < 0 || index >= this.attributes.length){
			throw new IndexOutOfBoundsException(String.format("Valid bounds are %d to %d inclusive. Argument of %d is out of bounds.", 0, this.attributes.length - 1, index));
		}
		else{
			return attributes[index]; /* Double er immutabel, så dette går fint: Double kan ikke endres etter at du har fått den. */
		}
	}
	
	public int size() {
		return attributes.length;
	}
	
	public String classification(){
		return classification;
	}
	

	public Double distanceTo(CSVData thisGuy, Integer[] onTheseIndexes){
		if(thisGuy.size() != this.size()) throw new IllegalArgumentException("These datapoints are not compatible. To calculate distances, they must be of equal size.");
		
		Double accumulator = new Double(0.0);
		for(Integer index : onTheseIndexes){
			accumulator += Math.pow(this.getAttribute(index) - thisGuy.getAttribute(index), 2.0);
		}
		
		return Math.sqrt(accumulator);
	}
	public Double distanceTo(CSVData thisGuy){
		if(thisGuy.size() != this.size()) throw new IllegalArgumentException("These datapoints are not compatible. To calculate distances, they must be of equal size.");
		
		Double accumulator = new Double(0.0);
		for(int i = 0; i < this.size(); ++i){
			accumulator += Math.pow(this.getAttribute(i) - thisGuy.getAttribute(i), 2.0);			
		}
		return Math.sqrt(accumulator);
	}
	@Override
	public boolean equals(Object o){
		if(!(o instanceof CSVData)){
			return false;
		}
		CSVData cand = (CSVData) o;
		if(cand.size() != this.size()){
			return false;
		}
		
		for(int i = 0; i < cand.size(); ++i){
			if(this.getAttribute(i) != cand.getAttribute(i)){
				return false;
			}
		}
		return true;
	}
	

	@Override
	public String toString(){
		return String.format("CSVData, classified as %s, %d number of fields.", classification(), size());
	}
	
	@Override
	public int hashCode() {
		Integer accumulator = 0;
		for(int i = 0; i < attributes.length; ++i){
			accumulator += attributes[i].hashCode() << i;
		}
		return new Integer(accumulator * 31).hashCode();
	}
	
	// Factory methods
	/**
	 * This is a simple factory that creates a bunch of objects from
	 * @param csvRows
	 * @param classificationIndex
	 * @return
	 */
	public static List<CSVData> createFromList(List<String[]> csvRows, int classificationIndex){
		List<CSVData> created = new LinkedList<>();
		
		for(String[] row : csvRows){
			created.add(new CSVData(row, classificationIndex));
		}
		
		return created;
	}
	

}

