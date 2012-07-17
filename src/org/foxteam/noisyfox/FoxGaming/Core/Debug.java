/**
 * FileName:     Debug.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-17 下午12:41:58
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import android.util.Log;

/**
 * @ClassName: Debug
 * @Description: Functions and helpers to aid debugging and engine reports.
 *               DebugMode can be toggled to avoid using the print command. This
 *               is a copy from Rokon by Richard.
 * @author: Noisyfox
 * @date: 2012-7-17 下午12:41:58
 * 
 */
public class Debug {
	private static String tag = "FoxGmaing";
	protected static boolean debugMode = true;

	public static String getDebugTag() {
		return tag;
	}

	/**
	 * Sets the tag to be used in LogCat debug messages If unset, it will
	 * default to "FoxGmaing"
	 * 
	 * @param tag
	 *            any valid String for LogCat tags
	 */
	public static void setDebugTag(String tag) {
		Debug.tag = tag;
	}

	/**
	 * Prints a warning to LogCat with information about the engine warning
	 * 
	 * @param source
	 *            The source of the warning, such as function name
	 * @param message
	 *            The message to be passed on
	 */
	public static void warning(String source, String message) {
		if (!debugMode)
			return;
		Log.w(tag, source + " - " + message);
		Exception e = new Exception(source + " - " + message);
		e.printStackTrace();
	}

	/**
	 * Prints a warning to LogCat with information about the engine warning
	 * 
	 * @param message
	 *            The message to be passed on
	 */
	public static void warning(String message) {
		if (!debugMode)
			return;
		Log.w(tag, message);
	}

	/**
	 * Prints to the verbose stream of LogCat with information from the engine
	 * 
	 * @param message
	 *            The message to be passed on
	 */
	public static void print(String message) {
		if (!debugMode)
			return;
		Log.v(tag, message);
	}

	/**
	 * Prints to the error stream of LogCat with information from the engine
	 * 
	 * @param message
	 *            The message to be passed on
	 */
	public static void error(String message) {
		Log.e(tag, message);
		Exception e = new Exception(message);
		e.printStackTrace();
	}

	/**
	 * Prints to the verbose stream of LogCat, with information that comes in
	 * when in Rokon.verboseMode
	 * 
	 * @param method
	 * @param message
	 */
	public static void verbose(String method, String message) {
		if (!debugMode)
			return;
		Log.v(tag, method + " - " + message);
		if (!debugMode)
			return;
	}

	/**
	 * Prints to the verbose stream of LogCat, with information that comes in
	 * when in Rokon.verboseMode
	 * 
	 * @param message
	 */
	public static void verbose(String message) {
		if (!debugMode)
			return;
		Log.v(tag, message);
	}

	/**
	 * Forces the application to exit, this is messy, unsure if it should be
	 * used. For debugging purposes while testing, it will be.
	 */
	public static void forceExit() {
		System.exit(0);
	}

}
