package com.fileprocessing.processors;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipAZipFile {
	
	private static final Logger log = Logger.getLogger( UnzipAZipFile.class.getName() );

	/*private static final String SOURCE_ZIP_FILE = "src/main/resources/input/nlp_data.zip";

	private static final String OUTPUT_FOLDER = "src/main/resources/output";

	
	public static void main(String[] args) throws IOException {
		
		UnzipAZipFile.unzip(SOURCE_ZIP_FILE, OUTPUT_FOLDER);
		UnzipAZipFile.getOutputPath(SOURCE_ZIP_FILE, OUTPUT_FOLDER);
	}*/

	/**
	 * Size of the buffer to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified
	 * by destDirectory (will be created if does not exists)
	 * 
	 * @param zipFilePath
	 * @param destDirectory
	 * @throws IOException
	 */
	private static void unzip(String zipFilePath, String destDirectory)
			throws IOException {
		File destDir = new File(destDirectory);
		
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		
		
		
		ZipEntry entry = zipIn.getNextEntry();
		
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		
	}

	/**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private static void extractFile(ZipInputStream zipIn, String filePath)
			throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}
	/**
	 * Gets the output path of the extracted files
	 * @param zipFilePath
	 * @param destDirectory
	 * @return
	 */
	private static String getOutputPath(String zipFilePath, String destDirectory){
		File zipFile = new File(zipFilePath);
		log.info(zipFile.getName());
		String zipfileName = zipFile.getName();
		String outPutFilePath = destDirectory + "/" + zipfileName.substring(0, (zipfileName.length()-4));
		log.info(zipFile.getName());
		log.info(outPutFilePath);
		
		return outPutFilePath;
	}
	/**
	 * 
	 * @param zipFilePath
	 * @param destDirectory
	 * @return
	 * @throws IOException
	 */
	public static String processZipFile(String zipFilePath, String destDirectory) throws IOException{
		unzip(zipFilePath, destDirectory);
		return getOutputPath(zipFilePath, destDirectory);
	}
}