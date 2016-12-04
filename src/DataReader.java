import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataReader {
	
	List <String> results;
	Iterator<String> iterator;
	BufferedReader br;
	
	public DataReader (String path) {
		results = new ArrayList <String>();
		try {
			br = new BufferedReader(new FileReader(path));
			int c;
			char ch;
		    StringBuilder sb = new StringBuilder();
		    while ((c = br.read()) != -1) {

		    	ch = (char) c;
		    	if (ch == '#') {
		    		if (!sb.toString().isEmpty()) {
		    			results.add(sb.toString());
		    			sb = new StringBuilder();
			    		continue;
			    		}
			    		continue;
			    		}
		    	
		    	sb.append(ch);
		    }
		    if (c == -1) {results.add(sb.toString());}
		    br.close();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iterator = results.iterator();
	}
	
	String getElement() {
		if (iterator.hasNext()) {
			String s = iterator.next();
			iterator.remove();
			return s;
		}
		else {
			return null;
		}
	}
	
	int getLen() {
		return results.size();
	}
	
}


