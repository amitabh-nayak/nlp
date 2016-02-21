package com.fileprocessing.processors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fileprocessing.domain.FileComponent;
import com.fileprocessing.domain.NounEntity;
import com.fileprocessing.domain.Punctuation;
import com.fileprocessing.domain.SentenceComponent;

public class InputFileProcessor {
	
	private static final Logger log = Logger.getLogger( InputFileProcessor.class.getName() );
	
	private static String patternString; 
	
	private static Pattern nounPattern;
	
	static{
	 
		patternString = buildPoperNounPattern();
		nounPattern = Pattern.compile(patternString);
	
	}
	
	 
	public static String processFile(Path fileName) throws FileNotFoundException, IOException{
				
		String []linesInFile = new String (Files.readAllBytes(fileName)).split (Punctuation.SENTENCE_DELIMITERS);
		FileComponent fileComponent = new FileComponent();
				
		for(String line : linesInFile){
			
			if(!(line == null || "".equals(line))) {
				String[] words = line.split("\\s+");
				SentenceComponent sentenceComponent = new SentenceComponent();
				sentenceComponent.setSentence(line);
				sentenceComponent.setWords( Arrays.asList(words));
				sentenceComponent.setNounList(getMatchingSentencePortion(line));
				fileComponent.addSentence(sentenceComponent);
			}
		}
		String xmlOutput = jaxbObjectToXML(fileComponent);
		//log.info(xmlOutput);
		return xmlOutput;
	}
	/**
	 * Construct object model to XML file representation
	 * @param fileComponent
	 * @return
	 */
	private static String jaxbObjectToXML(FileComponent fileComponent) {
	    String xmlString = "";
	    try {
	        JAXBContext context = JAXBContext.newInstance(FileComponent.class);
	        Marshaller m = context.createMarshaller();

	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

	        StringWriter sw = new StringWriter();
	        m.marshal(fileComponent, sw);
	        xmlString = sw.toString();

	    } catch (JAXBException e) {
	        e.printStackTrace();
	    }

	    return xmlString;
	}
	/**
	 * 
	 * @param toMatch
	 * @param matchIn
	 * @return
	 */
	public static boolean matchesWord(String toMatch, String matchIn) {
	     return Pattern.matches(".*([^A-Za-z]|^)"+toMatch+"([^A-Za-z]|$).*", matchIn);
	}
	
	private static String buildPoperNounPattern() {
		StringBuilder patternStr = new StringBuilder();
		FileSystem fs = FileSystems.getDefault();
		Path path1 = fs.getPath("src/main/resources/input/NER.txt");
		try {
			String []linesInFile = new String (Files.readAllBytes(path1)).split ("\\r?\\n");
			for(String noun : linesInFile){
				patternStr.append(noun).append("|");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(patternStr.toString());
		return patternStr.toString();
	}
	/**
	 * 
	 * @param inputString
	 * @return
	 */
	private static List<NounEntity> getMatchingSentencePortion(String inputString){
		
		List<NounEntity> nounList = new ArrayList<>();
		 for (MatchResult match : allMatches(nounPattern, inputString)) {
			 if(!match.group().isEmpty()){
				  log.info(match.group() + " at " + match.start());
				  log.info(inputString.substring(0, match.start()));
				  NounEntity entity = new NounEntity(match.group(), 
						  inputString.substring(0, match.start()), match.start() );
				  nounList.add(entity);
			 }
		}
		return nounList;
	}
	/**
	 * gets all possible mathces of a pattern
	 * @param p
	 * @param input
	 * @return
	 */
	private static Iterable<MatchResult> allMatches(
		      final Pattern p, final CharSequence input) {
		  	return new Iterable<MatchResult>() {
		    public Iterator<MatchResult> iterator() {
		      return new Iterator<MatchResult>() {
		        // Use a matcher internally.
		        final Matcher matcher = p.matcher(input);
		        // Keep a match around that supports any interleaving of hasNext/next calls.
		        MatchResult pending;

		        public boolean hasNext() {
		          // Lazily fill pending, and avoid calling find() multiple times if the
		          // clients call hasNext() repeatedly before sampling via next().
		          if (pending == null && matcher.find()) {
		            pending = matcher.toMatchResult();
		          }
		          return pending != null;
		        }

		        public MatchResult next() {
		          // Fill pending if necessary (as when clients call next() without
		          // checking hasNext()), throw if not possible.
		          if (!hasNext()) { throw new NoSuchElementException(); }
		          // Consume pending so next call to hasNext() does a find().
		          MatchResult next = pending;
		          pending = null;
		          return next;
		        }

		        /** Required to satisfy the interface, but unsupported. */
		        public void remove() { throw new UnsupportedOperationException(); }
		      };
		    }
		  };
		}
	/*public static void main(String [] args) throws FileNotFoundException, IOException{
		FileSystem fs = FileSystems.getDefault();
		Path path1 = fs.getPath("src/main/resources/input/nlp_data.txt");
		processFile(path1);
	}*/
}
