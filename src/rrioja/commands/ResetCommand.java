package rrioja.commands;

import cla.command.Command;

public class ResetCommand implements Command {
	@Override
	public void execute() {
		System.out.print("reset");
	}
}