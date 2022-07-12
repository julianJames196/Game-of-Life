//package uk.ac.cam.jpj36.oop.tick5;
import java.io.*;
import java.net.*;
import java.util.*;

public class PatternStore {

  private List<Pattern> patterns = new LinkedList<>();
  private Map<String,List<Pattern>> mapAuths = new HashMap<>();
  private Map<String,Pattern> mapName = new HashMap<>();

  public PatternStore(String source) throws IOException {
    if (source.startsWith("http://") || source.startsWith("https://")) {
      loadFromURL(source);
    }
    else {
      loadFromDisk(source);
    }
  }
    
  public PatternStore(Reader source) throws IOException {
    load(source);
  }
    
  private void load(Reader r) throws IOException {
    // read each line from the reader and print it to the screen
    BufferedReader b = new BufferedReader(r);
    String line;
    while ( (line = b.readLine()) != null) {
        System.out.println(line);
        try {
            Pattern pattern = new Pattern(line);
            patterns.add(pattern);
            if (mapAuths.get(pattern.getAuthor()) == null) {
                mapAuths.put(pattern.getAuthor(), new LinkedList<>());
            }
            mapAuths.get(pattern.getAuthor()).add(pattern);
            mapName.put(pattern.getName(), pattern);
        } catch (PatternFormatException e) {
            System.out.println("Warning: Skipping pattern '" + line + "' because it is malformatted.");
        }
    }
  }
    
   private void loadFromURL(String url) throws IOException {
    // Create a Reader for the URL and then call load on it
    URL destination = new URL(url);
    URLConnection conn = destination.openConnection();
    load(new InputStreamReader(conn.getInputStream()));
   }

   private void loadFromDisk(String filename) throws IOException {
     // Create a Reader for the file and then call load on it
     load(new FileReader(filename));
   }

  public List<Pattern> getPatternsNameSorted() {
    // Get a list of all patterns sorted by name
    patterns.sort(null);
    return new LinkedList<Pattern>(patterns);
  }
  
  public List<Pattern> getPatternsAuthorSorted() {
    // Get a list of all patterns sorted by author then name
    patterns.sort(new Comparator<Pattern>() {
        public int compare(Pattern p1, Pattern p2) {
            int cmp = p1.getAuthor().compareTo(p2.getAuthor());
            return cmp != 0 ? cmp : p1.compareTo(p2);
        }
    });
    return new LinkedList<Pattern>(patterns);
  }
  
  public List<Pattern> getPatternsByAuthor(String author) throws PatternNotFound {
    // return a list of patterns from a particular author sorted by name
    if (!mapAuths.containsKey(author)) {
        throw new PatternNotFound("");
    }
    mapAuths.get(author).sort(null);
    return new LinkedList<Pattern>(mapAuths.get(author));
  }
  
  public Pattern getPatternByName(String name) throws PatternNotFound {
    // TODO: Get a particular pattern by name
    if (!mapName.containsKey(name)) {
        throw new PatternNotFound("");
    }
    return mapName.get(name);
  }
  
  public List<String> getPatternAuthors() {
    // Get a sorted list of all pattern authors in the store
    LinkedList<String> l = new LinkedList<String>(mapAuths.keySet());
    l.sort(null);
    return l;
  }
  
  public List<String> getPatternNames() {
    // Get a list of all pattern names in the store, sorted by name
    LinkedList<String> l = new LinkedList<String>(mapName.keySet());
    l.sort(null);
    return l;
  }

   public static void main(String args[]) throws IOException, PatternNotFound {
     PatternStore p = new PatternStore(args[0]);
     Pattern pp = p.getPatternByName("phi");
        System.out.println(pp.getName());
   }
}