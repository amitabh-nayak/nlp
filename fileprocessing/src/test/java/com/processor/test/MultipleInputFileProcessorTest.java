package com.processor.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fileprocessing.processors.MultipleInputFileProcessor;


@RunWith(PowerMockRunner.class)
public class MultipleInputFileProcessorTest {
	
	private static final String SOURCE_ZIP_FILE = "src/main/resources/input/nlp_data.zip";

	private static final String OUTPUT_FOLDER = "src/main/resources/output";
	
	private static final String XML_OUTPUT_FOLDER = "src/main/resources/xmloutput";
	
	
	/** Unit under test. */
	@Test public void shouldProcessSingleFile() throws IOException {
		 
		
			MultipleInputFileProcessor.processSingleFile();
		
		 
		 File xmlDestDir = new File(XML_OUTPUT_FOLDER);
	      assert(xmlDestDir.exists());
	      assert(xmlDestDir.listFiles().length == 1);
	   }
	
	@Test public void shouldProcessCompressFile() throws InterruptedException {
		 
		 try {
			MultipleInputFileProcessor.processCompressedFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 File xmlDestDir = new File(XML_OUTPUT_FOLDER);
		 
	      assert(xmlDestDir.exists());
		  assert(xmlDestDir.listFiles().length == 10);
	   }
}
