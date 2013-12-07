package no.uib.info381.knn.userinterface.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import no.uib.info381.knn.dataloaders.CSVData;

public class CommandLine {
	
	ArrayList<CSVData> dataset;
	private HashMap<String, CLICommand> lookup;
	protected Boolean done = Boolean.FALSE;
	
	public CommandLine(){
		dataset = new ArrayList<>();
		lookup = new HashMap<>();
		
		lookup.put(":classify", new CLICommand() {
			
			@Override
			public void execute(String[] args, CommandLine env) {
				System.out.println(":classify is not done yet. Sorry.");
				/* MEMO: Hvis jeg ikke har noen argumenter kommer args til å være et array med et element som er en tom streng. Siden det som var igjen av strengen var "\n" etter trim(). */ 
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
				System.out.printf("Supposed to be loading file %s right now, but isn't, because the :load command is unimplemented.%n", args[0]);
			}
		});
		
		lookup.put(":exit", new CLICommand() {		
			@Override
			public void execute(String[] args, CommandLine env) {
				env.done = Boolean.TRUE;
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
				System.out.printf("%-"+width+"s - %s\n",":classify [-add]","starts a dialogue in which you create a sample datapoint and classify it. (NOT DONE YET)");
			}
		});
	}
	
	public void start(){
		Scanner kb = new Scanner(System.in);
		while(!done){
			System.out.printf("%d Command: ", dataset.size());
			String command = kb.next().trim();
			String[] args = kb.nextLine().trim().split(" ");
	
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
	
}
