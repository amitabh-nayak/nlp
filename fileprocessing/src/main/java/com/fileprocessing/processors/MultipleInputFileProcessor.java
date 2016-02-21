package com.fileprocessing.processors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class MultipleInputFileProcessor {
	
	private static final Logger log = Logger.getLogger( MultipleInputFileProcessor.class.getName() );
	
	private static final String SOURCE_ZIP_FILE = "src/main/resources/input/nlp_data.zip";

	private static final String OUTPUT_FOLDER = "src/main/resources/output";
	
	private static final String XML_OUTPUT_FOLDER = "src/main/resources/xmloutput";
	
	public static void main(String[] args) throws IOException, InterruptedException{
		
		//processCompressedFile();
		processSingleFile();
	}
	
	/**
	 * Method to process a single file 
	 * In this case it processes nlp_data.txt
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void processSingleFile() throws FileNotFoundException, IOException{
		
		//clean and create xmloutput directory to store xml files
	    File xmlDestDir = new File(XML_OUTPUT_FOLDER);
	    if (xmlDestDir.exists()) {
			purgeDirectory(xmlDestDir);
		}
	    
		if (!xmlDestDir.exists()) {
			xmlDestDir.mkdir();
		}
		
		
		FileSystem fs = FileSystems.getDefault();
		Path inputFilePath = fs.getPath("src/main/resources/input/nlp_data.txt");
		String xmlString = InputFileProcessor.processFile(inputFilePath);
		StringBuilder filePathStr = new StringBuilder();
		filePathStr.append(XML_OUTPUT_FOLDER).append("/").append("outfile").append((11)).append(".xml");
		String xmlFilePathStr = filePathStr.toString();
		log.info(xmlFilePathStr);
		createXMLFile(xmlString, xmlFilePathStr);
	}
	/**
	 * Method to process compressed file contents
	 * In this case it processes nlp_data.zip
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void processCompressedFile() throws FileNotFoundException, IOException, InterruptedException{
			//Process 0 : Clean up and create xml output directory
			File zipOutputDir = new File(OUTPUT_FOLDER);
			if (zipOutputDir.exists()) {
				purgeDirectory(zipOutputDir);
			}
			
			//clean and create xmloutput directory to store xml files
		    File xmlDestDir = new File(XML_OUTPUT_FOLDER);
		    if (xmlDestDir.exists()) {
				purgeDirectory(xmlDestDir);
			}
		    
			if (!xmlDestDir.exists()) {
				xmlDestDir.mkdir();
			}
			
			//Process 1 : get the uzip file and extract it
			//After extracting get the extracted folder path
			String extractedFolderPath = UnzipAZipFile.processZipFile(SOURCE_ZIP_FILE, OUTPUT_FOLDER);
			log.info("Extracted folder path ::  " + extractedFolderPath);
			
			//Process 2 : get the File system defaults
			FileSystem fs = FileSystems.getDefault();
			
			// Process 3 Create the executor
			ExecutorService executor = Executors.newCachedThreadPool();
			
			List<Future<String>> resultList = new ArrayList<>();
			
			//Process 4 : Iterate over files and submit for processing
			File folder = new File(extractedFolderPath);
			File[] listOfFiles = folder.listFiles();
			
		    for (int i = 0; i < listOfFiles.length; i++) {
		    
		      if (listOfFiles[i].isFile()) {
		    	  
		        log.info("File " + listOfFiles[i].getName());
		        String inputFileName = listOfFiles[i].getName();
		        String inputFilePathStr = extractedFolderPath + "/" +  inputFileName;
		        
		        log.info("Submitting Inputfile for processing at path ::   " + inputFilePathStr);
		        
		        final Path inputFilePath = fs.getPath(inputFilePathStr);
		        
		        log.info("InputFilePath ::  " + inputFilePath.getFileName());
		        
		        Future<String> result =  executor.submit(new Callable<String>(){
		        	public String call(){
		        		String xmlOutput = "";
		        		try {
		        			xmlOutput = InputFileProcessor.processFile(inputFilePath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return xmlOutput;
		        	}
		        });
		        
		        resultList.add(result);
		        
		      } else if (listOfFiles[i].isDirectory()) {
		    	 log.info("Directory " + listOfFiles[i].getName());
		      }
		    }
		    
		  //Process 5  : Iterate over future objects and create xml files inthe xml output folder
			int fileCount = 0;
		    for(Future<String> result : resultList){
		    	log.info("Processed XML File  ::  ");
				try {
					String xmlString = result.get();
					log.info(xmlString);
					StringBuilder filePathStr = new StringBuilder();
					filePathStr.append(XML_OUTPUT_FOLDER).append("/").append("outfile").append((fileCount++)).append(".xml");
					createXMLFile(xmlString, filePathStr.toString());
					
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		    executor.shutdown();
	}
	/**
	 * Clean up files and directories 
	 * @param dir
	 */
	private static void purgeDirectory(File dir) {
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory()) {
	        	purgeDirectory(file);
	        }
	        file.delete();
	    }
	}
	/**
	 * I/O xml file to disk
	 * @param xmlString
	 * @param filePath
	 * @throws IOException
	 */
	private static void createXMLFile(String xmlString, String filePath)throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"));
		writer.write(xmlString);
		writer.close();
	}
}

