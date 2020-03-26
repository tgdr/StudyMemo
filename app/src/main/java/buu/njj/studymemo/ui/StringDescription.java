package buu.njj.studymemo.ui;

public class StringDescription {

	public String content;
	public int begin;
	public int end;
	
	public StringDescription(String content)
	{
		this.content = content;
		begin = 0;
		end = this.content.length() - 1;
	}
	
}
