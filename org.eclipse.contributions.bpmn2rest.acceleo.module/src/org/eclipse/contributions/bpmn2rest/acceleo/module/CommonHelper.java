package org.eclipse.contributions.bpmn2rest.acceleo.module;

public class CommonHelper {
	
	/**
	 * formats a string by removing the withespaces and transforming to camel case it
	 * 
	 * @param String input
	 * @return String
	 */
	public String toUpperCamelCase(String input) {
		String[] tokens = input.split("\\s");
		for (String token : tokens) token = token.substring(0, 1).toUpperCase() + token.substring(1);
		return String.join("", tokens);
	}

	/**
	 * removes the whitespaces and lowercases the string
	 * 
	 * @param String input
	 * @return String
	 */
	public String cleanAndLowerString(String input) {
		return input.replaceAll("\\s", "").toLowerCase();
	}
	
	/**
	 * formats an operation name to be a valid method
	 * 
	 * @param String input
	 * @return String
	 */
	public String toJavaMethod(String input) {
		String result = input.replaceAll("\\s", "")
			.replaceAll("-", "")
			.replaceAll("\\\\", "")
			.replaceAll("\\/", "");
		if (result.substring(0, 1).matches("\\d")) result = "_" + result;
		return result;
	}

}
