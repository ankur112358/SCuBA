// Runs in O(nlgn) time


import java.io.*;
import java.util.*;

public class SortbyLength2
{
	private static final int LOWER_BOUND = 2;

	private static final String File_Name_Input = "./NG_Data/output_NG.txt";	//"out8.txt";//
	private static final String File_Name_Output =	"./NG_Data/sorted_data_NG.txt";

	// Output Files
	private static File fout;
	private static FileWriter fwrite;
	private static BufferedWriter bwrite;

	// Input Files
	private static FileReader fr;
	private static BufferedReader inFile;

	private static StringTokenizer tk;
	private static ArrayList pattern_list;

	public static void scanLongPatterns() throws IOException
	{
		String str;
		int count;
		Element e;

		fr = new FileReader(File_Name_Input);
		inFile = new BufferedReader(fr);

		str = inFile.readLine();
		while(str != null)
		{
			tk = new StringTokenizer(str);
			count = tk.countTokens();
			e = new Element(str, count);
			pattern_list.add(e);

			str = inFile.readLine();
		}

		Collections.sort(pattern_list);
		inFile.close();
	}

	public static void printToFile() throws IOException
	{
		String str, msg;
		Element e;

		// Output
		fout = new File(File_Name_Output);
		fwrite = new FileWriter(fout);
		bwrite = new BufferedWriter(fwrite);


		for(int j=0; j<pattern_list.size(); j++)
		{
			e = (Element)pattern_list.get(j);
			if(e.getCount() >= LOWER_BOUND+2)
			{
				str = e.toString();
				bwrite.write(str, 0, str.length());
				bwrite.newLine();
			}

		}

		bwrite.close();
	}

	public static void main(String[] args) throws IOException
	{
		pattern_list = new ArrayList();

		scanLongPatterns();
		printToFile();
	}

	private static class Element implements Comparable
	{
		private String str;
		private int count;

		public Element(String s, int c)
		{
			str = s;
			count = c;
		}

		// implementation of the compareTo method
		// descending order
		public int compareTo (Object obj) throws ClassCastException
		{
			Element e = (Element)obj;

			if(getCount() < e.getCount())
				return 1;

			else if(getCount() > e.getCount())
				return -1;

			return 0;
		}

		public int getCount()
		{
			return count;
		}

		public String toString()
		{
			return str;
		}

	}
}