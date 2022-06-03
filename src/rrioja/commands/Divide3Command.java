package rrioja.commands;

import cla.command.Command;

public class Divide3Command implements Command {
	@Override
	public void execute() {
		System.out.print("/3");
	}
}