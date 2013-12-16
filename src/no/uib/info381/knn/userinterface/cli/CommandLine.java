package no.uib.info381.knn.userinterface.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import no.uib.info381.knn.KNN;
import no.uib.info381.knn.convenience.CSVNormaliser;
import no.uib.info381.knn.dataloaders.CSVData;
import au.com.bytecode.opencsv.CSVReader;

public class CommandLine {
	
	ArrayList<String[]> dataset;
	private HashMap<String, CLICommand> lookup;
	protected Boolean done = Boolean.FALSE;
	protected Scanner kb;
	Stack<String> commands;
	private int classificationIndex = -1;
	
	public CommandLine(){
		dataset = new ArrayList<>();
		lookup = new HashMap<>();
		lookup.put(":skript", new CLICommand() {
			
			@Override
			public void execute(String[] args, CommandLine env) {
				try {
					// Gidder ikke lukke denne. Men det er et teoretisk problem hvis en bruker mer enn noen få skriptfiler.
					@SuppressWarnings("resource")
					Scanner reader = new Scanner(new File(args[0])).useDelimiter("\\Z");
					env.runScript(reader.next().split("\n"));
					reader.close();
				} catch (FileNotFoundException e) {
					System.out.printf("File \"%s\" not found.%n", args[0]);
				}				
			}
		});
		
		lookup.put(":drop", new CLICommand() {
			@Override
			public void execute(String[] args, CommandLine env) {
				env.setClassificationIndex(-1);
				env.dataset = new ArrayList<>(); // Kinda bad, but who cares.
			}
		});

				
		lookup.put(":classify", new CLICommand() {
			@Override
			public void execute(String[] args, CommandLine env) {
				/* Sjekker at vi har korrekt mengde argumenter. Vi kunne sjekket mer, men dette får holde. */
				if(args.length != env.dataset.get(0).length + 1){
					System.out.printf("Improper amount of arguments. Expected %d, got %d", env.dataset.get(0).length + 1, args.length);
				}
				
				/* Sjekker at vi vet hvor mange som skal stemme. */
				Integer numVotes = -1;
				try{
					numVotes = Integer.parseInt(args[0]);
				} catch(NumberFormatException e){
					System.out.printf("%s is not a valid integer.%n", args[0]);
					return;
				}
				System.out.printf("Classifying dataset based on %d votes,%n", numVotes);
				
				/* Skal vi skrive ut eller legge til? */
				boolean add = false;
				boolean print = false;
				String option = args[args.length - 1];
				if(option.contains("add")){
					add = true;
				}
				if(option.contains("print")){
					print = true;
				}
				
				/* kopierer over dataene */
				String[] row= new String[args.length -1]; // There are two arguments that are not part of the row, and the classification is not given when entered.
				for(int i = 0; i < env.getClassificationIndex(); ++i){
					row[i] = args[+1]; // The first element is the number of voters, which has nothing to do with the data itself.
				}
				row[env.getClassificationIndex()] = "Unknown (null-value)";
				for(int i = getClassificationIndex() + 1; i < row.length-1; ++i){ // We don't want the last part because that's what we do with the array.
					row[i] = args[i];
				}
				
				KNN knn = env.getKNN();
				CSVData data = new CSVData(row, env.getClassificationIndex());
				String classification = knn.classify(data, env.getClassificationIndex());
				
				if(print){
					System.out.printf("The council of %d has spoken, and concluded that the correct classification is %s%n.", numVotes, classification);
				}
				if(add){
					row[env.getClassificationIndex()] = classification;
					env.dataset.add(row);
				}
				
			}
		});
		lookup.put(":save", new CLICommand() {
			
			@Override
			public void execute(String[] args, CommandLine env) {
				System.out.printf("Command save is not implemented yet, so I'm not saving to file %s%n", args[0]);
			}
		});
		
		lookup.put(":load", new CLICommand() {
			
			@Override
			public void execute(String[] args, CommandLine env) {
				if(args.length != 1){
					System.out.println("Invalid number of arguments.");
					return;
				}
				File csvFile = new File(args[0]);
				System.out.printf("Reading file %s%n", csvFile.getAbsolutePath());
				
				if(!csvFile.exists()){
					System.out.printf("Could not find file%n");
					return;
				}
				if(!csvFile.canRead()){
					System.out.printf("Not allowed to read file.%nPlease ensure you have proper permissions set.%n");
					return;
				}
				if(env.getClassificationIndex() < 0){
					System.out.println("Classification index not set.");
				}
				
				try {
					CSVReader reader = new CSVReader(new FileReader(csvFile));
					env.dataset.addAll(reader.readAll());
					reader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.printf("Could not find file %s.%n", csvFile.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Something went wrong while reading.");
				}
			}
		});
		
		lookup.put(":exit", new CLICommand() {		
			@Override
			public void execute(String[] args, CommandLine env) {
				env.done = Boolean.TRUE;
			}
		});
		
		lookup.put(":peek", new CLICommand() {
			@Override
			public void execute(String[] args, CommandLine env) {
				if(env.dataset.size() == 0){
					System.out.println("No data available to print");
					return;
				}
				int peekNum = 0;
				if(args.length == 0){
					peekNum = 1;
				}
				try{
					peekNum = Integer.parseInt(args[0]);
				} catch (NumberFormatException e){
					System.out.printf("%s is not a proper number.%n", args[0]);
				}
				if(peekNum < 0){
					peekNum = env.dataset.size();
				}
				for(int i = 0; i < peekNum; ++i){
					StringBuilder sb = new StringBuilder();
					for(String s : env.dataset.get(i)){
						sb.append(s).append(',');
					}
					String s = sb.substring(0, sb.length()-1);
					System.out.println(s);
					}
				}
		});
		
		lookup.put(":classification-index", new CLICommand() {
			
			@Override
			public void execute(String[] args, CommandLine env) {
				if(args.length != 1){
					System.out.println("Improper amount of arguments. One and only one, please.");
				}
				try{
					env.setClassificationIndex(Integer.parseInt(args[0]));
				} catch(NumberFormatException e){
					System.out.printf("%s is not a proper integer.%n", args[0]);
				}
				
			}
		});
		
		lookup.put(":help", new CLICommand() {
			
			@Override
			public void execute(String[] args, CommandLine env) {
				System.out.println("Legal commands are:");
				int width = 16;
				System.out.printf("%-"+width+"s - %s\n",":help","prints this helpful message");
				System.out.printf("%-"+width+"s - %S\n",":exit","exits the program");
				System.out.printf("%-"+width+"s - %s\n",":load [filename] [classification-index]","loads a csv-file to the dataset. If it's not compatible with the data already saved, the program will crash. (NOT DONE YET)");
				System.out.printf("%-"+width+"s - %s\n",":save","unimplemented. would save your current dataset to disk.");
				System.out.println(":script [filename] – runs the script found in filename.");
				System.out.println(":peek [number of datapoints]– Shows you the first n datapoints. If the argument is negative, all data is shown.");
				System.out.println(":drop – deletes the currently loaded dataset.");
				System.out.println("(WIP) :classify-file [filename] [classification index] [number of voters] [-add|-print|-replace] – ");
				System.out.println("               Reads a file and classifies every single row. Then either adds them to the set, prints them or replaces the current set.");
				System.out.println(":classify [number of voters] [data as doubles, separated by spaces] [-add|-print|-printadd] starts a dialogue in which you create a sample datapoint and classify it.");
				System.out.println("          If no add or print option is given, no feedback will be given to the user, and no new data will be added. If printadd is given, the result will be printed,");
				System.out.println("          and the new data will be added as well.");
				System.out.println(":classification-index [index] - Sets the classification index. It's zero-based, so the first is 0 and the second is 1 and so on.");
				
			}
		});
	}
	
	public void start(){
		Scanner kb = new Scanner(System.in);
		while(!done){
			System.out.printf("%d Command: ", dataset.size());
			String command = kb.next().trim();
			String[] args = kb.nextLine().trim().split(" ");
			/* MEMO: Hvis jeg ikke har noen argumenter kommer args til å være et array med et element som er en tom streng. Siden det som var igjen av strengen var "\n" etter trim(). */
			handle(command, args);
		}
		kb.close();
	}

	private void handle(String command, String[] args) {
		if(lookup.containsKey(command)){
			lookup.get(command).execute(args, this);
		}
		else{
			System.out.printf("[ERROR] Command %s not found. Try \":help\" for a list of available commands.%n", command);
		}
	}
	
	/**
	 * The size of the dataset that's currently loaded.
	 * @return The number of objects loaded.
	 */
	public Integer size(){
		return dataset.size();
	}
	
	public void setClassificationIndex(int index){
		assert(index >= 0);
		this.classificationIndex = index;
	}
	
	public int getClassificationIndex(){
		return this.classificationIndex;
	}
	
	/**
	 * Fetches a KNN object based on the loaded object and the defined dataset.
	 * @return a KNN object, that has been naively normalized based on CSVNormaliser's method.
	 */
	public KNN getKNN(){
		List<CSVData> csvList = new LinkedList<>();
		CSVNormaliser norman = new CSVNormaliser(dataset, classificationIndex);
		for(String[] row : norman.fillBlanks().normalize().getList()){
			csvList.add(new CSVData(row, classificationIndex));
		}
		return new KNN(csvList);
	}
	/**
	 * Kjører et skript fra et array av strenger, slik at vi slipper å kopiere inn fire millioner linjer for å gjøre ting.
	 * @param script Er bare et array av strenger. Enkelt å greit, bare kommandoene skrevet ned. Tomme linjer, og linjer som begynner med # blir ignorert.
	 */
	public void runScript(String[] script){
		Scanner parser = null;
		for(String line : script){
			/* Lar oss ha kommentarer og tomme linjer. */
			if(line.startsWith("#") || line.length() == 0){
				continue;
			}
			parser = new Scanner(line);
			String command = parser.next();
			String[] args = parser.nextLine().trim().split(" ");
			handle(command, args);
			parser.close();			
			
		}
	}
	
	public void runScript(File script) throws FileNotFoundException{
		Scanner readFile = new Scanner(script);
		this.runScript(readFile.useDelimiter("\\Z").next().split("\n"));
		readFile.close();
	}
}
