package org.scm4j.commons;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class RegexConfig {
	private final LinkedHashMap<Object, Object> content = new LinkedHashMap<>();
	
	@SuppressWarnings("unchecked")
	public void loadFromYamlUrls(String... separatedUrls) throws IOException {
		Yaml yaml = new Yaml();
		URLContentLoader loader = new URLContentLoader();
		List<String> contents = loader.getContentsFromUrlStrings(Arrays.asList(separatedUrls));
		for (String content : contents) {
			this.content.putAll((Map<? extends Object, ? extends Object>) yaml.load(content));
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getPropByName(String nameToMatch, String propName, T defaultValue) {
		if (content != null) {
			for (Object key : content.keySet()) {
				if (key == null || nameToMatch.matches((String) key)) {
					Map<?, ?> props = (Map<?, ?>) content.get(key);
					if (props.containsKey(propName)) {
						return (T) props.get(propName);
					}
				}
			}
		}
		return defaultValue;
	}

	public String getPlaceholderedStringByName(String nameToMatch, Object propName, String defaultValue) {
		String result = defaultValue;
		if (content != null) {
			for (Object key : content.keySet()) {
				if (key == null || nameToMatch.matches((String) key)) {
					Map<?, ?> props = (Map<?, ?>) content.get(key);
					if (props.containsKey(propName)) {
						result = (String) props.get(propName);
						if (result != null)
							result = nameToMatch.replaceFirst(key == null ? ".*" : (String) key, result);
						break;
					}
				}
			}
		}
		return result;
	}
}
