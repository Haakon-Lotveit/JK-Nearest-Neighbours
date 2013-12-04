package no.uib.info381.knn.dataloaders;

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
		System.err.printf("[ERROR] Standard constructuor of class %s called.%n", CSVData.class.toString());
	}
	
	/**
	 * En naiv konstruktør som ikke gjør noe form for dataskalering. Dette må i så fall gjøres før konstruktøren blir kalt.
	 * TODO: En fabrikkmetode som skalerer ned etter gitte kriterier hadde vært noe. Skal i så fall gjøres etterpå.
	 * @param csvData
	 */
	public CSVData(String[] csvData){
		this.attributes = new Double[csvData.length];
		for(int i = 0; i < csvData.length; ++i){
			
		}
	}
	
	// Felt
	
	/**
	 * Denne holder på attributtene til objektet. Alt er tall, så jeg tenkte at flyttall med dobbel presisjon får funke for nå.
	 */
	Double[] attributes;
	
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
}
