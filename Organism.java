import java.util.Random;


/**
 * @author Kyle
 *
 *	The organism class represents an individual, and keeps track of its genotype.
 */
public class Organism {
		private int genotype;
		//1 = Homo A1
		//2 = Hetero
		//3 = Homo A2
		
	/**Constructs an organism based upon allele frequency.
	 * Genotype stored as integer to reduce running time (many 'if' statements when working purely with alleles)
	 * 
	 * @param a1Freq	is the initial frequency of the A1 allele
	 */
	public Organism(double a1Freq){
		Random rand = new Random();
		
		//Allele 1
		double tempDouble = rand.nextDouble();
		double tempDouble2 = rand.nextDouble();
		
		if(tempDouble < a1Freq && tempDouble2 < a1Freq){	//If HomozygoteA1
			genotype = 1;
		}
		else if(tempDouble > a1Freq && tempDouble2 > a1Freq){	//If HomozygoteA2
			genotype = 3;
		}
		else{													//If Heterozygote
			genotype = 2;
		}
	}
	
	/**Constructs an organism with the specified alleles
	 * 
	 * @param al1	allele 1
	 * @param al2	allele 2
	 */
	public Organism(int al1, int al2){
		if(al1 == 1 && al2 == 1){
			genotype = 1;
		}
		else if(al1 == 0 && al2 == 0){
			genotype = 3;
		}
		else {
			genotype = 2;
		}
	}
		
	/**
	 * @return	gets the state (genotype) of the organism
	 */
	public int getGenotype(){
		return this.genotype;
	}
	
	public String toString(){
		String output = null;
		
		if(genotype == 1){
			output = "A1A1";
			return output;
		}
		else if(genotype == 2){
			output = "A1A2";
			return output;
		}
		else{
			output = "A2A2";
			return output;
		}
	}
	
	/**Creates an offspring from two organism 
	 * 
	 * @param a		the first parent
	 * @param b		the second parent
	 * @return		returns the resulting organism
	 */
	public static Organism reproduce(Organism a, Organism b){
		
		int par1Gam = a.gamete();
		int par2Gam = b.gamete();
		
		Organism offspring = new Organism(par1Gam, par2Gam);
		return offspring;
	}
	
	
	/**Gets a random allele based upon genotype
	 * @return	returns an allele
	 */
	private int gamete(){
		Random rand = new Random();
		
		if(genotype == 1){
			return 1;
		}
		else if(genotype == 3){
			return 0;
		}
		else{
			if(rand.nextInt(2) == 0){
				return 1;
			}
			else{
				return 0;
			}
		}
	}
	
	/**Applies mutation to an organism
	 * 
	 * @param mutRate	is the mutation rate
	 */
	public void mutate(double mutRate){
		Random rand = new Random();
		int genotype = getGenotype();
		if(genotype == 2){
			if(rand.nextDouble() < mutRate){
				genotype = 1;
			}
		}
		else if(genotype == 3){
			if(rand.nextDouble() < mutRate){
				genotype ++;
			} 
			if(rand.nextDouble() < mutRate){
				genotype++;
			}
		}
	}
}	

