package cla.command.undo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

public class Stack<T> {

	private final Deque<T> stack = new ArrayDeque<>();
	
	/**
	 * @return null if stack is empty
	 */
	public T pop() {
		return stack.pollLast(); //Not using pop since it throws NoSuchElementException if the deque is empty
	}
	
	public void push(T cmd) {
		stack.addLast(cmd);
	}

	public void clear() {
		stack.clear();
	}
	
	public void forEachFifo(Consumer<? super T> action) {
		stack.stream().forEachOrdered(action);
	}
	
	public Iterator<T> getIterator() {
		return stack.iterator();
	}
	
	public Iterator<T> getDescendingIterator() {
		return stack.descendingIterator();
	}

	@Override public String toString() {
		return stack.toString();
	}
}
