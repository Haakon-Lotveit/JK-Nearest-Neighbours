package no.uib.info381.knn.dataloaders;

/**
 * En enkel klasse for å laste inn data fra testdata/csv/adult-removed.csv
 * @author Haakon Løtveit
 */
public class Adult extends CSVData{
	/**
	 * Standardkonstruktøren står tom, men skal uansett ikke brukes til noe, så det går vel greit. :)
	 */
	private Adult(){
		super();		
	}

	public Adult(String[] data, int classificationIndex){
		super(data, classificationIndex);
		assert(this.size() == 7);
		
	}
	
	@Override
	public String toString() {
		/* Dette er CLOS formatet for objekter. Hvis du heller vil gjøre det annerledes, er ikke det noe stress. - Haakon */
		return String.format(
				"(:class no.uib.info381.knn.dataloaders :classified-as %s :age %f :sampling-weight %f :education-years %f :sex %f :capital-gain %f :capital-loss %f :hours-per-week %f)",
				this.classification(), this.getAge(), this.getSamplingWeight(), this.getEducationYears(), this.getSex(), this.getCapitalGain(), this.getCapitalLoss(), this.getHoursPerWeek());
	}

	@Override
	public int hashCode() {
		/**
		 * Dette er en dust måte å gjøre ting på, men det funker vel.
		 */
		return new Double(
				(getAge().hashCode() << 1) *
				(getSamplingWeight().hashCode() << 2) *
				(getEducationYears().hashCode() << 3) *
				(getSex().hashCode() << 4) *
				(getCapitalGain().hashCode() << 5) *
				(getCapitalLoss().hashCode() << 6) *
				(getHoursPerWeek().hashCode() << 7) *
				31).hashCode();
		
	}
	
	/**
	 * Age as of when the census was carried out
	 * @return
	 */
	public Double getAge(){
		return super.getAttribute(0);
	}
	
	/**
	 * If you wonder about the sampling weight, see {@link http://www.census.gov/sipp/weights.html}
	 * @return the final sampling weight
	 */
	public Double getSamplingWeight(){ 
		return super.getAttribute(1);
	}
	
	/**
	 * Assumed to be the number of years spent in education
	 * @return
	 */
	public Double getEducationYears(){
		return super.getAttribute(2);
	}
	
	/**
	 * The sex of the person
	 * @return 1 if male, 0 if female.
	 */
	public Double getSex(){
		return super.getAttribute(3);
	}
	
	public Double getCapitalGain(){
		return super.getAttribute(4);
	}
	public Double getCapitalLoss(){
		return super.getAttribute(5);
	}
	public Double getHoursPerWeek(){
		return super.getAttribute(6);
	}
	
	
	
}
