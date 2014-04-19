import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class Population {
	
	private ArrayList<Organism> population;
	private double  A1A1Freq;
	private double A1A2Freq;
	private double A2A2Freq;
	private double A1Freq;
	private double A2Freq;
	
	/**Constructs a population
	 * 
	 * @param size		the size of the population
	 * @param A1Freq	the frequency of the population
	 */
	public Population(int size, double A1Freq){
		population = new ArrayList<Organism>();
		Populate(size, A1Freq);
	}
	
	/**Adds organisms to the population based upon size and allele frequencies
	 * 
	 * @param size		the size of the population
	 * @param A1Freq	the frequency of the popluation
	 */
	private void Populate(int size, double A1Freq){
		for(int i = 1; i <= size; i++){
			Organism tempOrg = new Organism(A1Freq);
			population.add(tempOrg);
		}
		calcFreqs();
	}
	
	/**Applies selection to a population
	 * 
	 * @param a1a1		fitness of homozygoteA1 genotype
	 * @param a1a2		fitness of homozygoteA2 genotype
	 * @param a2a2		fitness of heterozygote genotype
	 */
	public void selection(double a1a1, double a1a2, double a2a2){
		Random rand = new Random();
		
		//for each organism
	   for(int i = 0; i < population.size(); i++){
		   int genotype = population.get(i).getGenotype();
		   
		   //if a1a1
		   if(genotype == 1){
			   if(rand.nextDouble() > a1a1){
				   population.remove(i);
			   }
		   }
		   
		   //if a1a2
		   else if(genotype == 2){
			   if(rand.nextDouble() > a1a2){
				   population.remove(i);
			   }
		   }
		   
		   //if a2a2
		   else
			   if(rand.nextDouble() > a2a2){
				   population.remove(i);
			   }
		   }  
	}
	
	/**Calculates the frequencies of alleles and genotypes.
	 * 
	 */
	public void calcFreqs(){
		
		//genotypes
		double A1A1 = 0;
		double A1A2 = 0;
		double A2A2 = 0;
		
		//alleles
		double A1 = 0;
		double A2 = 0;
		
		for(int i = 0; i < population.size(); i++){
			Organism tempOrg = population.get(i);
			int genotype = tempOrg.getGenotype();			///1 = Homo A1, 2 = Hetero, 3 = Homo A2
			
			//counting genotypes
			if(genotype == 1){
				A1A1++;
			} else if(genotype == 2){
				A1A2++;
			}
			else{
				A2A2++;
			}
			
			//calculating genotype frequencies
			A1A1Freq = (A1A1) / (A1A1 + A1A2 + A2A2);
			A1A2Freq = (A1A2) / (A1A1 + A1A2 + A2A2);
			A2A2Freq = (A2A2) / (A1A1 + A1A2 + A2A2);
			
			//counting alleles
			A1 = A1A1 + (0.5 * A1A2);
			A2 = A2A2 + (0.5 * A1A2);
			
			//calculating allele frequencies
			A1Freq = A1 / (A1 + A2);
			A2Freq = A2 / (A1 + A2);
			
		}
	}
	
	/**Transforms the previous generation into the generation entirely composed of offspring
	 * 
	 * @param popSize	is the desired population size of the resulting population.
	 */
	public void nextGen(int popSize){
		Random rand = new Random();
		List<Organism> backup = new ArrayList<Organism>(population.size());
		//make a population backup
		backup.addAll(population);
		
		ArrayList<Organism> newPop = new ArrayList<Organism>(popSize);
		
		//in order to maintain constant population size, this while loop is necessary		
		while(newPop.size() < popSize){
		
			while(population.size() >= 2){
				
				Organism parent1 = population.remove(rand.nextInt(population.size() - 1));
				
				//in case of odd remaining population sizes
					Organism parent2;
					if(population.size() > 1){
						parent2 = population.remove(rand.nextInt(population.size() - 1));
					}else{
						parent2 = population.remove(0);
					}
		
					newPop.add(Organism.reproduce(parent1, parent2));
					newPop.add(Organism.reproduce(parent1, parent2));
			}
			//if population is now size 1, this is needed
			if(population.size() == 1){
				newPop.add(population.get(0));
			}
			
			//breed again, to maintain constant population size (selection decreased it previously)
			if(newPop.size() < popSize){
				population.addAll(backup);
			}
		}
		
		newPop = new ArrayList<Organism>(newPop.subList(0, popSize));
		population = newPop;
	}
	
	/** applies mutation to every individual in the population
	 * 
	 * @param mutRate		is the mutation rate
	 */
	public void applyMutation(double mutRate){
		for(int i = 0; i < population.size(); i++){
			population.get(i).mutate(mutRate);
		}
	}
	
	
	/**
	 * @return		HomozygoteA1 frequency
	 */
	public double getA1A1Freq(){
		return A1A1Freq;
	}
	
	/**
	 * @return		Heterozygote frequency
	 */
	public double getA1A2Freq(){
		return A1A2Freq;
	}
	
	/**
	 * @return		HomozygoteA2 frequency
	 */
	public double getA2A2Freq(){
		return A2A2Freq;
	}
	
	/**
	 * @return		AlleleA1 frequency
	 */
	public double getA1Freq(){
		return A1Freq;
	}
	
	/**
	 * @return		AlleleA2 frequency
	 */
	public double getA2Freq(){
		return A2Freq;
	}
	
	/**
	 * @return		Returns a String representation of the population
	 */
	public String toString(){
		return population.toString();
	}
}
