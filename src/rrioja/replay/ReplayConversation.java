package rrioja.replay;

import java.util.ArrayList;
import java.util.Iterator;

import cla.command.Command;
import cla.command.undo.AbstractConversation;
import cla.command.undo.Stack;

public class ReplayConversation {
	protected final Stack<Command> eventStore;
	
	private final Command reset;
	
	public ReplayConversation(Command reset) {
		eventStore = new Stack<Command>();
		this.reset = reset;
	}

	public void exec(Command todo) {
		todo.execute();
		eventStore.push(todo);
	}

	public void undo() {
		eventStore.push(new UndoCommand());
		
		reset.execute();
		replay();
	}

	public void redo() {
		eventStore.push(new RedoCommand());
		
		reset.execute();
		replay();
	}
	
	private void replay() {
		ArrayList<String> parsedEventStore = new ArrayList<String>();
		
		Iterator<Command> loop = eventStore.getIterator();
		
		while(loop.hasNext()) {
			Command nextCommand = loop.next();
			
			if(nextCommand instanceof UndoCommand) {
				parsedEventStore.add("undo");
			} else if(nextCommand instanceof RedoCommand) {
				parsedEventStore.add("redo");
			} else {
				parsedEventStore.add("event");
			}
		}
		
		//Account for
		//redo
		//undo -> redo
		//event -> redo
		//undo -> ...skip... -> redo
		//event -> ...skip... -> redo
		for(int i = 0; i < parsedEventStore.size(); i++) {
			if(parsedEventStore.get(i).equals("redo")) {
				if(i == 0) {
					parsedEventStore.set(0, "skip");
					i = -1; //Restart loop
					continue;
				}
				
				for(int j = i - 1; j >= 0; j--) {
					if(parsedEventStore.get(j).equals("skip")) {
						if(j == 0) {
							parsedEventStore.set(i, "skip");
						}
						
						continue;
					}
					
					if(parsedEventStore.get(j).equals("undo")) {
						parsedEventStore.set(i, "skip");
						parsedEventStore.set(j, "skip");
						
						break;
					} else {
						parsedEventStore.set(i, "skip");
						
						break;
					}
				}
				
				i = -1; //Restart loop
				continue;
			}
		}
		
		//Account for
		//undo
		//event -> undo
		//event -> ...skip... -> undo
		for(int i = 0; i < parsedEventStore.size(); i++) {
			if(parsedEventStore.get(i).equals("undo")) {
				if(i == 0) {
					parsedEventStore.set(0, "skip");
					i = -1; //Restart loop
					continue;
				}
				
				for(int j = i - 1; j >= 0; j--) {
					if(parsedEventStore.get(j).equals("skip") || parsedEventStore.get(j).equals("redo")) {
						if(j == 0) {
							parsedEventStore.set(i, "skip");
						}
						
						continue;
					}
					
					if(parsedEventStore.get(j).equals("event")) {
						parsedEventStore.set(i, "skip");
						parsedEventStore.set(j, "skip");
						
						break;
					} else {
						parsedEventStore.set(i, "skip");
						
						break;
					}
				}
				
				i = -1; //Restart loop
				continue;
			}
		}
		
		loop = eventStore.getIterator();
		
		for(int i = 0; i < parsedEventStore.size(); i++) {
			Command currentCommand = loop.next();
			
			if(parsedEventStore.get(i).equals("event")) {
				currentCommand.execute();
			} else {
				System.out.print("skipped"); //Usually this event should just not be executed, but we do this instead for demo purposes
				currentCommand.execute();
			}
		}
	}
}
