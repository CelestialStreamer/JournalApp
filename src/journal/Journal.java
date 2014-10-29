package journal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A journal that has entries.
 * 
 * @author Brian Woodruff
 *
 */
public class Journal implements Runnable {
	private List<Entry> entries = new ArrayList<Entry>();
	String fileName = "";

	/**
	 * Sets up valid scripture and topic list.
	 */
	public Journal() {
		try {
			setupFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads valid scriptures and valid topics list
	 * 
	 * @throws IOException
	 */
	private void setupFiles() throws IOException {
		Map<String, String> topics = new HashMap<String, String>();
		List<String> scriptures = new ArrayList<String>();
		Properties prop = new Properties();
		String curDir = System.getProperty("user.dir");

		prop.load(getClass().getResourceAsStream("config.properties"));

		BufferedReader reader = new BufferedReader(new FileReader(curDir
				+ prop.getProperty("validScriptures")));
		while (reader.ready()) {
			scriptures.add(reader.readLine());
		}

		reader.close();
		reader = new BufferedReader(new FileReader(curDir
				+ prop.getProperty("validTopics")));

		while (reader.ready()) {
			String topicSplit[] = reader.readLine().split(":");
			String topicMatches[] = topicSplit[1].split(",");

			for (String item : topicMatches) {
				topics.put(item, topicSplit[0]);
			}
		}
		reader.close();

		Scripture.setValidScriptures(scriptures);
		Entry.setTopics(topics);
	}

	/**
	 * A given xml file is checked against a schema file to verify that it is
	 * formatted correctly.
	 * 
	 * @param xmlFile
	 * @throws SAXException
	 * @throws IOException
	 */
	private void validateFile(File xmlFile) throws SAXException, IOException {
		Source file = new StreamSource(xmlFile);
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File("schema.xsd"));
		Validator validator = schema.newValidator();
		validator.validate(file);
	}

	/**
	 * Reads from a txt file.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void importTxtFile(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		entries.clear();

		for (String line = br.readLine(); line != null; line = br.readLine()) {
			String date = br.readLine();
			String content = "";
			while ((line = br.readLine()) != null && !line.equals("-----")) {
				content.concat(line);
			}
			entries.add(new Entry(content, date));
		}
		br.close();
	}

	/**
	 * Writes a file in text format.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void exportTxtFile(File file) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (Entry entry : entries) {
			bw.write("-----\n");
			bw.write(entry.getDate() + "\n");
			bw.write(entry.getContent() + "\n");
		}
		bw.close();
	}

	/**
	 * Open an xml file and read contents into a list of entries.
	 * 
	 * @param file
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void open(File file) throws SAXException, IOException,
			ParserConfigurationException {
		if (file.getPath().endsWith(".txt")) {
			importTxtFile(file);
			return;
		}
		fileName = file.getPath();
		ArrayList<Entry> tempEntry = new ArrayList<Entry>();

		validateFile(file);

		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(file);
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("entry");
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			NodeList nList2 = ((Element) nNode).getElementsByTagName("content"); // content
																					// subElements

			for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
				Node nNode2 = nList2.item(temp2);

				if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
					String content = nNode2.getTextContent().toString(); // element
																			// text
																			// content
					String date = ((Element) nNode).getAttribute("date")
							.toString(); // superElement date attribute

					tempEntry.add(new Entry(content, date));
				}
			}
		}
		entries = tempEntry;
	}

	/**
	 * Open an xml file with a string file name.
	 * 
	 * @param fileName
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void open(String fileName) throws SAXException, IOException,
			ParserConfigurationException {
		open(new File(fileName));
	}

	/**
	 * Write an xml format file.
	 * 
	 * @param file
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 * @throws IOException
	 */
	public void save(File file) throws ParserConfigurationException,
			TransformerConfigurationException, TransformerException,
			TransformerFactoryConfigurationError, IOException {
		if (file.getPath().endsWith(".txt")) {
			exportTxtFile(file);
			return;
		}
		fileName = file.getPath();

		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("journal");
		doc.appendChild(rootElement);

		for (Entry anEntry : entries) {
			// entry elements
			Element entryElement = doc.createElement("entry");
			rootElement.appendChild(entryElement);

			// set attribute
			entryElement.setAttribute("date", anEntry.getDate());

			// set content
			Element content = doc.createElement("content");
			content.appendChild(doc.createTextNode(anEntry.getContent()));
			entryElement.appendChild(content);
		}

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileName));

		TransformerFactory.newInstance().newTransformer()
				.transform(source, result);
	}

	/**
	 * Write an xml format file with a string file name.
	 * 
	 * @param fileName
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 * @throws IOException
	 */
	public void save(String fileName) throws ParserConfigurationException,
			TransformerConfigurationException, TransformerException,
			TransformerFactoryConfigurationError, IOException {
		save(new File(fileName));
	}

	/**
	 * Write an xml format file if a previous save has occurred.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 * @throws IOException
	 */
	public void save() throws TransformerConfigurationException,
			ParserConfigurationException, TransformerException,
			TransformerFactoryConfigurationError, IOException {
		save(fileName);
	}

	/**
	 * Get an entry with a date.
	 * 
	 * @param date
	 * @return
	 */
	public Entry getEntry(String date) {
		for (Entry anEntry : entries) {
			if (anEntry.getDate().equals(date)) {
				return anEntry;
			}
		}
		return null;
	}

	/**
	 * Get all entries currently in the journal.
	 * 
	 * @return
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * Add an entry to the journal.
	 * 
	 * @param newEntry
	 */
	public void addEntry(Entry newEntry) {
		entries.add(newEntry);
	}

	/**
	 * Remove all entries from the journal.
	 */
	public void clear() {
		entries.clear();
	}

	/**
	 * Remove an entry with a date.
	 * 
	 * @param entry
	 */
	public void removeEntry(Entry entry) {
		this.entries.remove(entry);
	}

	/**
	 * Return entries that match the scripture.
	 * 
	 * @param scripture
	 * @return
	 */
	public List<Entry> getScriptureMatch(String scripture) {
		List<Entry> matches = new ArrayList<Entry>();
		for (Entry entry : entries) {
			for (Scripture scripture1 : entry.getScriptures()) {
				if (scripture1.getBook().equals(scripture)) {
					matches.add(entry);
				}
			}
		}
		return matches;
	}

	/**
	 * Returns a map of scriptures to entries that match the scripture.
	 * 
	 * @return
	 */
	public Map<String, ArrayList<Entry>> getScriptureReferences() {
		Map<String, ArrayList<Entry>> map = new HashMap<String, ArrayList<Entry>>();
		for (Entry item : entries) {
			for (Scripture scripture : item.getScriptures()) {
				if (map.get(scripture.getBook()) == null) {
					map.put(scripture.getBook(), new ArrayList<Entry>());
				}
				if (!map.get(scripture.getBook()).contains(item)) {
					map.get(scripture.getBook()).add(item);
				}
			}
		}
		return map;
	}

	/**
	 * Returns a map of topics to entries that match the topic.
	 * 
	 * @return
	 */
	public Map<String, ArrayList<Entry>> getTopicReferences() {
		Map<String, ArrayList<Entry>> map = new HashMap<String, ArrayList<Entry>>();
		for (Entry item : entries) {
			for (String topic : item.getTopics()) {
				if (map.get(topic) == null) {
					map.put(topic, new ArrayList<Entry>());
				}
				if (!map.get(topic).contains(item)) {
					map.get(topic).add(item);
				}
			}
		}
		return map;
	}

	/**
	 * Prints a list of scriptures, on the command line, with entries that match
	 * the scripture.
	 */
	public void printScriptureReferences() {
		for (java.util.Map.Entry<String, ArrayList<Entry>> pair : getScriptureReferences()
				.entrySet()) {
			System.out.println(pair.getKey());
			for (Entry anEntry : pair.getValue()) {
				System.out.println("\t" + anEntry.getDate());
			}
		}
	}

	/**
	 * Prints a list of topics, on the command line, with entries that match the
	 * topic.
	 */
	public void printTopicReferences() {
		for (java.util.Map.Entry<String, ArrayList<Entry>> pair : getTopicReferences()
				.entrySet()) {
			System.out.println(pair.getKey());
			for (Entry anEntry : pair.getValue()) {
				System.out.println("\t" + anEntry.getDate());
			}
		}
	}

	/**
	 * Returns a list of entries that contain the string.
	 * 
	 * @param content
	 * @return
	 */
	public List<Entry> find(String content) {
		ArrayList<Entry> matches = new ArrayList<Entry>();
		for (Entry item : entries) {
			if (item.getContent().contains(content)) {
				matches.add(item);
			}
		}
		return matches;
	}

	/**
	 * Returns all entries with date and contents.
	 * 
	 * @return
	 */
	public String toString() {
		String journal = "";
		for (Entry entry : entries) {
			journal += "Entry:\n" + entry.toString() + "\n\n";
		}
		return journal;
	}

	@Override
	public void run() {
		
	}
}
