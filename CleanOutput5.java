// Uses a different approach than CleanOutput3
// Follows a clustering approach
// Complexity: O(nk), k=num of clusters
// Works on sorted data

import java.io.*;
import java.util.*;

public class CleanOutput5
{

	private static final String File_Name_Input = "./NG_Data/sorted_data_NG.txt";//"sorted_data_1397.txt";//
	private static final String File_Name_Output =	"./NG_Data/maximal_data_NG.txt"; //"temp_clean.txt";//

	// Output Files
	private static File fout;
	private static FileWriter fwrite;
	private static BufferedWriter bwrite;

	// Input Files
	private static FileReader fr;
	private static BufferedReader inFile;

	private static StringTokenizer tk;
	private static ArrayList pattern_list;
	private static ArrayList cluster_list;
	private static Cluster c;

	// Check the given string with each cluster and put
	// in the cluster with highest match
	public static void checkSubSet(String s, String count)
	{
		boolean match = false;


		// Special case when the first item is read
		if(cluster_list.size()==0)
		{
			c = new Cluster(s, count);
			cluster_list.add(c);
		}

		else{
			// compute similarity score with all existing clusters
			for(int i=0; i<cluster_list.size();i++)
			{
				c = (Cluster)cluster_list.get(i);
				match = c.getMatch(s);

				if(match==true)
				{
					break;
				}
			}

			if(match==false)
			{
				c = new Cluster(s, count);
				cluster_list.add(c);
			}
		}
	}

	public static void printClusters() throws IOException
	{
		String str;
		// Output
		fout = new File(File_Name_Output);
		fwrite = new FileWriter(fout);
		bwrite = new BufferedWriter(fwrite);

		for(int i=0; i<cluster_list.size();i++)
		{
			c = (Cluster)cluster_list.get(i);
			//c.print();
			str = c.getMembers()+" : "+c.getCount();
			bwrite.write(str, 0, str.length());
			bwrite.newLine();
		}

		bwrite.close();
	}

	public static void readData() throws IOException
	{
		String str, count;

		fr = new FileReader(File_Name_Input);
		inFile = new BufferedReader(fr);

		str = inFile.readLine();
		while(str != null)
		{
			tk = new StringTokenizer(str, ":");
			str = tk.nextToken();
			count = tk.nextToken();
			pattern_list.add(str); //put in main memory
			checkSubSet(str, count);

			str = inFile.readLine();
		}
		inFile.close();
		tk=null;

	}


	public static void main(String[] args) throws IOException
	{
		long time1, time2, time3;

		pattern_list = new ArrayList();
		cluster_list = new ArrayList();

		time1 = System.currentTimeMillis();
		readData();
		printClusters();

		time2 = System.currentTimeMillis();
		time3 = (time2-time1);
		System.out.println("Execution Time: "+time3+" msecs");

	}

	private static class Cluster
	{
		private Hashtable member_table;
		private String members;
		private String count;
		private int memberlength;

		public Cluster(String s, String c)
		{
			members = s;
			count = c;
			memberlength = 0;
			member_table = new Hashtable();
			addToMemberTable(s);

		}

		// parse the string and add to list.
		private void addToMemberTable(String s)
		{
			Integer dummy = new Integer(99);
			tk = new StringTokenizer(s);

			while(tk.hasMoreTokens())
			{
				member_table.put(tk.nextToken(), dummy);
				memberlength++;
			}

		}


		// calculate percentage match with member table
		public boolean getMatch(String s)
		{
			String temp;
			boolean match;
			int matchCount=0;
			int sLen = 0;


			tk = new StringTokenizer(s);
			// same length
			if(tk.countTokens()==memberlength)
			{
				return false;
			}

			while(tk.hasMoreTokens())
			{
				temp = tk.nextToken();
				match = member_table.containsKey(temp);
				if(match==false) return false;
				else if(match)
				{
					matchCount++;
				}
				sLen++;
			}

			if(matchCount == sLen)
				return true;

			return false;

		}

		public String getMembers()
		{
			return members;
		}
		public String getCount()
		{
			return count;
		}

		public void print()
		{
			System.out.println(members+" :" +count);
		}

	}
}