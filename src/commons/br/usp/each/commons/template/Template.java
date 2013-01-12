package br.usp.each.commons.template;

import java.util.List;

public class Template {

	private List<String> tokens, regexs, vars;

	protected Template(List<String> tokens, List<String> regexs, List<String> vars) {
		this.tokens = tokens;
		this.regexs = regexs;
		this.vars = vars;
	}

	public String get(String key, String str) {
		if (!str.matches(getRegex())) {
			throw new RuntimeException();
		}
		StringBuilder before = new StringBuilder();
		StringBuilder after = new StringBuilder();
		int i;
		for (i = 0; i < tokens.size(); i++) {
			before.append(tokens.get(i));
			if (vars.get(i).equals(key)) {
				i++;
				break;
			}
			before.append(regexs.get(i));
		}
		for (; i < tokens.size(); i++) {
			after.append(tokens.get(i) + regexs.get(i));
		}
		for (i = str.length() - 1; i >= 0; i--) {
			if (str.substring(i).matches(after.toString())) {
				str = str.substring(0, i);
				break;
			}
		}
		str = str.replaceFirst(before.toString(), "");
		return str;
	}

	public void set(String key, String value) {
		int i;
		for (i = 0; i < vars.size(); i++) {
			if(vars.get(i).equals(key)) {
				regexs.set(i, value);
				break;
			}
		}
	}

	public String getRegex() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < tokens.size(); i++) {
			b.append(tokens.get(i) + regexs.get(i));
		}
		return b.toString();
	}

}
