package rrioja.commands;

import cla.command.Command;

public class Plus1Command implements Command {
	@Override
	public void execute() {
		System.out.print("+1");
	}
}