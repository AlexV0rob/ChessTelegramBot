package org.example;

public class Position
{
	int line;
	int column;
	public Position(int pos)
	{
		this.line = pos / 8;
		this.column = pos % 8;
	}
}
