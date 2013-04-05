package com.lomoussw.androidstarter.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;

/**
 * Useful utils for extracting resources from a jar
 * 
 * @author Alexandre THOMAS
 */
public class ResourcesUtils {

	public static void copyResourcesTo(File resourcesDir, String filepathMarker) throws IOException {

		if (resourcesDir == null) {
			throw new IllegalArgumentException("The resources dir must not be null");
		}

		ClassLoader classLoader = ResourcesUtils.class.getClassLoader();
		URL url = classLoader.getResource(filepathMarker);
		String protocol = url.getProtocol();

		if (protocol.equals("file")) {
			File src = new File("src/main/resources");
			FileUtils.copyDirectory(src, resourcesDir);
		} else if (protocol.equals("jar")) {
			copyResourcesToFromJar(resourcesDir, url);
		}
	}

	private static void copyResourcesToFromJar(File target, URL url) throws IOException {
		JarURLConnection connection = (JarURLConnection) url.openConnection();
		JarFile jarFile = connection.getJarFile();

		Enumeration<JarEntry> entries = jarFile.entries();

		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			InputStream is = jarFile.getInputStream(jarEntry);
			String entryPath = jarEntry.getName();

			File file = null;
			String dirs = "";
			if (entryPath.contains("/")) {
				int lastIndexOf = entryPath.lastIndexOf("/");
				dirs = (String) entryPath.subSequence(0, lastIndexOf + 1);
			}

			File parent = new File(target, dirs);
			parent.mkdirs();

			if (!jarEntry.isDirectory()) {
				String[] splitedPath = entryPath.split("/");
				String fileName = splitedPath[splitedPath.length - 1];
				file = new File(parent, fileName);
				FileUtils.copyInputStreamToFile(is, file);
			}
		}
	}
}