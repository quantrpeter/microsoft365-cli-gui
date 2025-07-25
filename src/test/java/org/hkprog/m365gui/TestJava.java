package org.hkprog.m365gui;

import hk.quantr.setting.library.QuantrSettingLibrary;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.Test;

/**
 *
 * @author peter
 */
public class TestJava {
	
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TestJava.class);
	
	@Test
	public void test() {
		try {
			Setting setting = new Setting();
			QuantrSettingLibrary.load("m365gui", setting);
			String command = setting.m365Path + " tenant info get --output json";
			ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
			processBuilder.environment().put("PATH", new File(setting.m365Path).getParent());
			Process process = processBuilder.start();
			// Capture output
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

			// Capture errors
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			StringBuilder errorOutput = new StringBuilder();
			while ((line = errorReader.readLine()) != null) {
				errorOutput.append(line).append("\n");
			}
			// Wait for process to complete
			int exitCode = process.waitFor();
			System.out.println("Exit Code: " + exitCode);
			System.out.println("Output:\n" + output.toString());
			if (errorOutput.length() > 0) {
				System.err.println("Errors:\n" + errorOutput.toString());
			}
		} catch (Exception ex) {
			logger.error("Error executing command", ex);
		}
	}
}
