package rrioja.commands;

import cla.command.Command;

public class CheckpointCommand implements Command {
	@Override
	public void execute() {
		System.out.print("checkpoint");
	}
}