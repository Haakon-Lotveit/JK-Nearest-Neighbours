package no.uib.info381.knn.userinterface.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import no.uib.info381.knn.KNN;
import no.uib.info381.knn.dataloaders.CSVData;
import au.com.bytecode.opencsv.CSVReader;

public class CommandLine {
	
	ArrayList<CSVData> dataset;
	private HashMap<String, CLICommand> lookup;
	protected Boolean done = Boolean.FALSE;
	protected Scanner kb;
	Stack<String> commands;
	
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
				env.dataset = new ArrayList<>(); // Kinda bad, but who cares.
			}
		});

		/* Copypasta from :load and :classify */
		lookup.put(":classify-file", new CLICommand() {
			@Override
			public void execute(String[] args, CommandLine env) {
				String opt = args[args.length - 1];
				
				ArrayList<CSVData> classifieds = new ArrayList<>();
				
				KNN knn = new KNN(env.dataset);
				if(!opt.startsWith("-")){
					opt = "";
				}
				
				List<String[]> datas = null;
				
				FileReader fr = null;
				CSVReader cr = null;
				
				try{
					File csv = new File(args[0]);
					fr = new FileReader(csv);
					cr = new CSVReader(fr);
					datas = cr.readAll();
				} 
				catch (FileNotFoundException e) {
					System.out.println("[ERROR] File not found. No files loaded.");
				} 
				catch (IOException e) {
					System.out.println("[ERROR] Could not read CSV File. Technical error message commencing:");
					e.printStackTrace();
					System.out.println("[ERROR] Technical error message complete.");
				}
				finally{
					try{
						cr.close();
						fr.close();
					}
					catch(Exception e){
						System.out.println("[ERROR] Could not close resources. Exiting.");
						e.printStackTrace(System.out);
						System.exit(1);
					}
				}
				
				for(String[] row :datas){
					CSVData newGuy = new CSVData(row, Integer.parseInt(args[1]));
					String[] newRow = new String[row.length];
					
					for(int i = 0; i < newGuy.size(); ++i){
						newRow[i] = newGuy.getAttribute(i).toString();
					}
					newRow[newRow.length - 1] = knn.classify(newGuy, Integer.parseInt(args[2]));
					classifieds.add(new CSVData(newRow, newRow.length - 1));
				}
				
				if(opt.equals("-print")){
					for(CSVData datum : classifieds){
						System.out.println(datum.toString());
					}
				}
				else if(opt.equals("-add")){
					for(CSVData datum : classifieds){
						env.dataset.add(datum);
					}
				}
				else if(opt.equals("-replace")){
						env.dataset = classifieds;
				}
			}
		});
	
		lookup.put(":classify", new CLICommand() {
			
			@Override
			public void execute(String[] args, CommandLine env) {
				if(dataset.size() == 0){
					System.out.println("No data in classifier. Sorry");
				}
				System.out.println(":classify is WIP. Sorry.");

				String[] input = new String[args.length - 2];
				for(int i = 0; i < input.length; ++i){
					input[i] = args[i+1];
				}
				String[] datas = new String[input.length + 1];
				for(int i = 0; i < input.length; ++i){
					datas[i] = input[i];
				}
				datas[input.length] = "UNCLASSIFIED";
				 
				CSVData classifyMe = new CSVData(datas, input.length);
				KNN knn = new KNN(dataset);
				datas[input.length] =  knn.classify(classifyMe, Integer.parseInt(args[0]));
				
				CSVData classification = new CSVData(datas, input.length);
				if(args.length >= 2){
					if(args[args.length - 1].equals("-add")){
						dataset.add(classification);
					}
					else if(args[args.length - 1].equals("-print")){
						System.out.println(classification.toString());
					}
					else if(args[args.length - 1].equals("-printadd")){
						System.out.println(classification.toString());
						dataset.add(classification);
					}
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
			/*
			 * Nesten alt dette er bare sjekking om at alt er bra.
			 * Det eneste relevante er at vi lager en CSVReader, leser inn til en liste av streng-arrayer, og sender dem til CSVData sin createFromList metode
			 */
			@Override
			public void execute(String[] args, CommandLine env) {
				/* This is just errror-checking. Ignore if you want to look at the algorithm itself. */
				if(args.length < 1){
					System.out.println("[ERROR] No file specified. No files loaded.");
					return;
				}
				if(args.length < 2){
					System.out.println("[ERROR] No classification index specified, no files loaded.");
					return;
				}
				Integer classificationIndex;
				try{
					classificationIndex = Integer.parseInt(args[1]);
				}
				catch(NumberFormatException e){
					System.out.printf("[ERROR] %s is not a valid integer%n", args[1]);
					return;
				}
				List<String[]> datas = null;
				FileReader fr = null;
				CSVReader cr = null;
				/* This is the actual algorithm */
				try{
					File csv = new File(args[0]);
					fr = new FileReader(csv);
					cr = new CSVReader(fr);
					datas = cr.readAll();
				} 
				catch (FileNotFoundException e) {
					System.out.println("[ERROR] File not found. No files loaded.");
				} 
				catch (IOException e) {
					System.out.println("[ERROR] Could not read CSV File. Technical error message commencing:");
					e.printStackTrace();
					System.out.println("[ERROR] Technical error message complete.");
				}
				finally{
					try{
						cr.close();
						fr.close();
					}
					catch(Exception e){
						System.out.println("[ERROR] Could not close resources. Exiting.");
						e.printStackTrace(System.out);
						System.exit(1);
					}
				}				
				dataset.addAll(CSVData.createFromList(datas, classificationIndex));
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
				if(dataset.size() == 0){
					System.out.println("No data available to print");
				}
				else{
					System.out.println(dataset.get(0).toString());
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
				System.out.printf("%-"+width+"s - %s\n",":load","loads a csv-file to the dataset. If it's not compatible with the data already saved, the program will crash. (NOT DONE YET)");
				System.out.printf("%-"+width+"s - %s\n",":save","unimplemented. would save your current dataset to disk.");
				System.out.println(":script [filename] – runs the script found in filename.");
				System.out.println(":peek – Shows you the first datapoint in the dataset");
				System.out.println(":drop – deletes the currently loaded dataset.");
				System.out.println(":classify-file [filename] [classification index] [number of voters] [-add|-print|-replace] – ");
				System.out.println("               Reads a file and classifies every single row. Then either adds them to the set, prints them or replaces the current set.");
				System.out.println(":classify [number of voters] [data as doubles, separated by spaces] [-add|-print|-printadd (optional)] starts a dialogue in which you create a sample datapoint and classify it.");
				System.out.println("          If no add or print option is given, no feedback will be given to the user, and no new data will be added. If printadd is given, the result will be printed,");
				System.out.println("          and the new data will be added as well.");
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
}
