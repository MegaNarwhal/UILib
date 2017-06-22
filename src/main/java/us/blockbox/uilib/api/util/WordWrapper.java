package us.blockbox.uilib.api.util;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WordWrapper{
	private static final Pattern space = Pattern.compile(" ");
	private final String s;

	public WordWrapper(String s){
		Validate.notNull(s);
		this.s = s;
	}

	public List<String> wrap(int lineMaxLength){//todo wrap strings without spaces
		return wrapString(lineMaxLength,space.split(s));
	}

	public List<String> wrap(int lineMaxLength,String splitRegex){
		return wrapString(lineMaxLength,s.split(splitRegex));
	}

	private List<String> wrapString(int lineMaxLength,String[] split){
		int i = 0;
		int capacity = s.length() / lineMaxLength;
		List<String> lines = new ArrayList<>(capacity);
		StringBuilder b = new StringBuilder(lineMaxLength);
		int initialLength = b.length();
		int c = 0;
		int splitLength = split.length;
		while(i < splitLength){
			String word = split[i];
			int length = word.length();
			if(c + length >= lineMaxLength){
				String line = b.toString();
				lines.add(line);
				b = new StringBuilder(lineMaxLength);
				b.append(ChatColor.getLastColors(line));
				initialLength = b.length();
				c = 0;
			}else{
				i++;
				b.append(word);
				c += length;
				if(i < splitLength && c + 1 < lineMaxLength){
					b.append(' ');
					c += 1;
				}
			}
		}
		if(b.length() > initialLength){
			lines.add(b.toString());
		}
		return lines;
	}
}
