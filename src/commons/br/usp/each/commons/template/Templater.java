package br.usp.each.commons.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Templater {

	private Properties p;

	public Templater() throws IOException {
		p = new Properties();
		Reader r = new InputStreamReader(ClassLoader.getSystemResourceAsStream("template.properties"));
		try {
			p.load(r);
		} finally {
			r.close();
		}
	}

	public Templater(File f) throws FileNotFoundException, IOException {
		p = new Properties();
		Reader r = new FileReader(f);
		try {
			p.load(r);
		} finally {
			r.close();
		}
	}

	public Template template(String template) {
		List<String> tokens = new LinkedList<String>();
		List<String> regxp = new LinkedList<String>();
		List<String> vars = new LinkedList<String>();
		final String t = p.getProperty(template);
		if (t == null)
			return null;
		int i = 0, j = 0;
		String token;
		while (i < t.length()) {
			j = t.indexOf('#', i);
			if (j == -1) {
				token = t.substring(i);
				tokens.add(token);
				regxp.add("");
				vars.add("");
				break;
			}
			token = t.substring(i, j);
			tokens.add(token);
			i = t.indexOf('#', ++j);
			if (i == -1)
				throw new RuntimeException("Invalid!");
			token = t.substring(j, i);
			vars.add(token);
			token = p.getProperty(token);
			if (token == null)
				token = ".*";
			regxp.add(token);
			i++;
		}
		return new Template(tokens, regxp, vars);
	}

}
