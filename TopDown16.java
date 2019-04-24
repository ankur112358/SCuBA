// 				*** NOTE ***
// Fixed a bug. Duplicate entries.
// Ordered the key by sorting before putting in hastable.
//


/*
Date: 08/26/04
Author: Ehtesham u Haque

Documentation:

Input : processed dataset

Output: maximal itemsets

*/



import java.io.*;
import java.util.*;

public class TopDown16
{
	private static final int SUPPORT_THRESHOLD = 6;
	private static final int THRESHOLD = 4; // length of frequent pattern to consider
	private static final int NUM_ROWS = 200; //


	private static final String File_Name_Input =  "./NG_Data/data_proc_NG.txt"; //"test8.txt"; //
	private static final String File_Name_Output =	"./NG_Data/output_NG.txt";  //"temp.txt";//
	private static final String File_Name_Output_2 = "./NG_Data/output_members_NG.txt";

	private static Hashtable table_final;
	private static Hashtable table_temp;
	private static Hashtable table_main;
	private static Hashtable local_table;
	private static Hashtable result_table;
	private static ArrayList pattern_list;

	// Output Files
	private static File fout;
	private static FileWriter fwrite;
	private static BufferedWriter bwrite;

	// Input Files
	private static FileReader fr;
	private static BufferedReader inFile;

	private static StringTokenizer tk;
	private static int lineCount;
	private static Cluster c;

	public static void printTable_final() throws IOException
	{

		String key = "";
		String pattern = "";
		// Output
		fout = new File(File_Name_Output);
		fwrite = new FileWriter(fout);
		bwrite = new BufferedWriter(fwrite);

		int temp, max;
		temp = max = 0;
		for (Enumeration e = table_final.keys() ; e.hasMoreElements() ;)
		{
			key = (String)e.nextElement();
			tk = new StringTokenizer(key);
			temp = tk.countTokens();
			if(temp>max)
				max = temp;

			c = (Cluster)table_final.get(key);
			if(c.getCount() > SUPPORT_THRESHOLD)
			{
				//pattern = key + " : "+((Cluster)table_final.get(key)).getCount();
				pattern = key + " : "+c.getCount();
				bwrite.write(pattern, 0, pattern.length());
				bwrite.newLine();
			}
		}
		bwrite.close();

		System.out.println("Max pattern size: "+max);
	}


	public static void printTable_final_members() throws IOException
	{
		Cluster c;
		String key = "";
		String pattern = "";
		// Output
		fout = new File(File_Name_Output_2);
		fwrite = new FileWriter(fout);
		bwrite = new BufferedWriter(fwrite);

		for (Enumeration e = table_final.keys() ; e.hasMoreElements() ;)
		{
			key = (String)e.nextElement();
			//System.out.print(key);
			//System.out.print(" :");
			//System.out.println(table_final.get(key));

			c = (Cluster)table_final.get(key);
			if(c.getCount() > SUPPORT_THRESHOLD)
			{
				pattern = key + " : "+((Cluster)table_final.get(key)).getMembers();
				bwrite.write(pattern, 0, pattern.length());
				bwrite.newLine();
			}
		}
		bwrite.close();
	}




	// The method which does the main
	// row-enumeration to find maximal sets
	public static int findPattern(String p, int dataId) throws IOException
	{
		String pattern="";
		String str="";
		String key="";
		int support, dataId2;

		ArrayList value_list;


		table_temp.clear();
		local_table.clear();
		tk = new StringTokenizer(p);
		while(tk.hasMoreTokens())
		{
			table_temp.put(tk.nextToken(), new Integer(1));
		}

		//------- Scan rest of database in main memory--
		for(int j=lineCount; j<pattern_list.size(); j++)
		{

			pattern = (String)pattern_list.get(j);
			tk = new StringTokenizer(pattern);
			while(tk.hasMoreTokens())
			{
				local_table.put(tk.nextToken(), new Integer(1));
			}

			//------SubSet Matching between two rows ---------------------/
			for (Enumeration e = table_temp.keys() ; e.hasMoreElements() ;)
			{
				key = (String)e.nextElement();
				if(local_table.containsKey(key))
					result_table.put(key, new Integer(2));
			}
			local_table.clear();

		//-------- Sort the key before putting in hashtable -------------

			//------- Generate key and update temp table --------------------
			key = "";
			String s;
			Integer i;
			int value;
			value_list = new ArrayList(result_table.size());

			for (Enumeration e = result_table.keys() ; e.hasMoreElements() ;)
			{
				//key += (String)e.nextElement()+" ";
				s = (String)e.nextElement();
				i = new Integer(s);
				value_list.add(i);
			}
			Collections.sort(value_list);

			for(int t=0; t<value_list.size(); t++)
			{
				key += value_list.get(t)+" ";
			}
			value_list.clear();

		//----------------------------------------------------------------



			if(!table_final.containsKey(key))
			{
				dataId2 = j;
				dataId2++;
				String members = dataId+" "+dataId2;

				if(!table_main.containsKey(key))
				{
					c = new Cluster(2, members);
					table_main.put(key, c);
				}
				else{
					c = (Cluster)table_main.get(key);
					support = c.getCount();
					support++;
					c.setCount(support);
					//support = ((Integer)table_main.get(key)).intValue();
					members = c.getMembers()+" "+dataId2;
					c.setMembers(members);
					table_main.put(key, c);
				}
			}


			result_table.clear();

		}

		// Put in final table
		for (Enumeration e = table_main.keys() ; e.hasMoreElements() ;)
		{
			key = (String)e.nextElement();
			if(!table_final.containsKey(key))
			{
				table_final.put(key, (Cluster)table_main.get(key));
			}
		}

		table_main.clear();
		table_temp.clear();
		pattern=null;
		str=null;
		key=null;

		return 0;
	}


	// Read database in main memory
	public static void scanLongPatterns() throws IOException
	{
		String str;

		fr = new FileReader(File_Name_Input);
		inFile = new BufferedReader(fr);

		str = inFile.readLine();
		while(str != null)
		{
			tk = new StringTokenizer(str);
			if(tk.countTokens() >= THRESHOLD)
			{
				pattern_list.add(str);
			}
			str = inFile.readLine();
		}
		inFile.close();
		tk=null;

	}

    public static void main(String[] args) throws IOException
	{

		int status, data_id;
		long time1, time2, time3;
		String str1;


		pattern_list = new ArrayList(NUM_ROWS);
		table_final = new Hashtable();
		table_temp = new Hashtable();		// table to store temporary patterns
		table_main = new Hashtable();		// table to store all patterns
		local_table = new Hashtable();
		result_table = new Hashtable();

		time1 = System.currentTimeMillis();

		scanLongPatterns(); //read in main memory

		//fr = new FileReader(File_Name_Input);
		//inFile = new BufferedReader(fr);

		lineCount=1;
		for(int j=0; j<pattern_list.size(); j++)
		{
			str1 = (String)pattern_list.get(j);
			tk = new StringTokenizer(str1);
			data_id = j;
			data_id++;
			if(tk.countTokens() >= THRESHOLD)
			{
				status=findPattern(str1, data_id);
				lineCount++;
			}
			//str1 = inFile.readLine();

		}

		/***  File read approach ***
		str1 = inFile.readLine();	// read first line
		while(str1 != null)
		{
			tk = new StringTokenizer(str1);
			if(tk.countTokens() >= THRESHOLD)
			{
				status=findPattern(str1);
				lineCount++;
			}
			str1 = inFile.readLine();

		}*/
		time2 = System.currentTimeMillis();

		printTable_final();
		printTable_final_members();
		//inFile.close();

		time3 = (time2-time1);///1000;
		System.out.println("Execution Time: "+time3+" msecs");
	}

	// Class
	private static class Cluster
	{
		private int support;
		private String members;

		public Cluster(int s, String m)
		{
			support = s;
			members = m;
		}

		public int getCount()
		{
			return support;
		}

		public void setCount(int count)
		{
			support = count;
		}

		public String getMembers()
		{
			return members;
		}

		public void setMembers(String m)
		{
			members = m;
		}
	}
}
