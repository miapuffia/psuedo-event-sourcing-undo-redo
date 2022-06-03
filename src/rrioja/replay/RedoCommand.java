package rrioja.replay;

import cla.command.Command;

public class RedoCommand implements Command {
	@Override
	public void execute() {
		System.out.print("redo");
	}
}