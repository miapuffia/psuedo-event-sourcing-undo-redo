package rrioja.replay;

import cla.command.Command;

public class UndoCommand implements Command {
	@Override
	public void execute() {
		System.out.print("undo");
	}
}