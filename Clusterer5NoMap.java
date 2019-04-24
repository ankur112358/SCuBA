
import java.io.*;
import java.util.*;

public class Clusterer5NoMap
{
	//------------------------------ Parameters -------------------------------------//

	private static final String File_Name_Input = "./NG_Data/maximal_data_NG.txt"; //"test_cluster.txt";//
	private static final String File_Name_Input3 = "./NG_Data/output_members_NG.txt"; //"test_cluster.txt";//


	private static final String File_Name_Output =	"temp";//"result_1397.txt";

	//------------------------------------------------------------------------------//

	private static double THRESHOLD;  //Overlap percentage

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
	private static Hashtable cluster_table;
	private static Hashtable gene_table;
	private static Hashtable member_table;
	private static Hashtable abstract_ID_table;
	private static Cluster c;

	// Check the given string with each cluster and put
	// in the cluster with highest match
	public static void checkClusterMembership(String s)
	{
		String str;
		double current_score = 0.0;
		double max_score = 0.0;
		String cluster_id="";

		// Special case when the first item is read
		if(pattern_list.size()==1)
		{
			c = new Cluster(s);
			cluster_table.put(s, c);
		}

		else{
			// compute similarity score with data points above
			for(int i=0; i<pattern_list.size()-1;i++)
			{
				str = (String)pattern_list.get(i);
				current_score = getMatch(s, str);
				//System.out.println("Score: "+current_score);

				if(current_score > THRESHOLD)
				{
					if(current_score > max_score)
						max_score = current_score;

					cluster_id = str;
					// if score is higher than threshold
					// put it in the cluster with highest score
					if(cluster_table.containsKey(cluster_id))
					{
						c = (Cluster)cluster_table.get(cluster_id);
						c.addMember(s);
						cluster_table.put(cluster_id, c);
					}
					else{
						c = new Cluster(cluster_id);
						c.addMember(s);
						cluster_table.put(cluster_id, c);
					}



				}
			}


			// Create new cluster
			if(max_score <= THRESHOLD)
			{
				c = new Cluster(s);
				cluster_table.put(s, c);
			}

		}
	}

	public static double getMatch(String s, String str)
	{
		String temp;
		int matchCount=0;
		int sLen = 0;
		double score = 0.0;
		int totalCount=0;

		Integer dummy = new Integer(99);
		tk = new StringTokenizer(str);
		Hashtable temp_table = new Hashtable();

		while(tk.hasMoreTokens())
		{
			temp_table.put(tk.nextToken(), dummy);
			totalCount++;
		}


		tk = new StringTokenizer(s);
		while(tk.hasMoreTokens())
		{
			temp = tk.nextToken();
			if(temp_table.containsKey(temp))
			{
				matchCount++;
			}
			sLen++;
		}

		score = (matchCount*1.0) / (totalCount+sLen-matchCount);

		return score;
	}



	/*public static void printClusters() throws IOException
	{
		Cluster c;
		String str1, str2;
		ArrayList list;
		int j;


		for(int i=0; i<cluster_list.size(); i++)
		{
			c = (Cluster)cluster_list.get(i);
			j=i+1;
			//System.out.println();
			//System.out.println("Cluster: "+j+"   size: "+c.getSize());
			//System.out.println("-----------------------------------");
			//System.out.println("-------- GeneId------- : ------- PubMedId --------");
			//c.print();
			//str1 = c.getPrint();
			list = c.getMemberList();
			str2 = getMap(list);  // maps to the geneId
			System.out.print(str2);
			//System.out.println();
		}
	}*/
	public static void printClusters() throws IOException
	{
		Cluster c;
		String str2;
		ArrayList list;
		int j;


//    for(int i=0; i<cluster_list.size(); i++)
//    {
//       c = (Cluster)cluster_list.get(i);
//       j=i+1;
//       //System.out.println();
//       //System.out.println("Cluster: "+j+"   size: "+c.getSize());
//       //System.out.println("-----------------------------------");
//       //System.out.println("-------- GeneId------- : ------- PubMedId --------");
//       //c.print();
//       //str1 = c.getPrint();
//       list = c.getMemberList();
//       str2 = getMap(list);  // maps to the geneId
//       System.out.print(str2);
//       //System.out.println();
//    }
		String File_Name_Output1="result123.txt";
		fout = new File(File_Name_Output1);
		fwrite = new FileWriter(fout);
		bwrite = new BufferedWriter(fwrite);

		for(int i=0; i<cluster_list.size();i++)
		{
			//c = (CleanOutput5.Cluster)cluster_list.get(i);
			//c.print();
			c = (Cluster)cluster_list.get(i);
			j=i+1;
			list = c.getMemberList();
			str2 = getMap(list);
			// str = c.getMembers()+" : "+c.getCount();
			bwrite.write(str2);
			bwrite.newLine();
		}

		bwrite.close();
	}

	public static String getMap(ArrayList list)
	{
		String str, temp1, temp2, temp3, ab;

		temp3="";
		for(int i=0; i<list.size(); i++)
		{
			str = (String)list.get(i);
			str = str.trim();
			ab = getAbstract(str);

			/*
			tk = new StringTokenizer(str);
			while(tk.hasMoreTokens())
			{
				temp1 = tk.nextToken();
				temp2 = (String)gene_table.get(temp1);
				temp3 += temp2+" ";
			}
			*/
			temp3 += str +" : "+ab;
			temp3 += "\n";
		}

		return temp3;
	}

	private static String getAbstract(String str)
	{
		String abs, abs2, temp;

		abs = (String)member_table.get(str);

		/*
		tk = new StringTokenizer(abs);
		abs2="";
		while(tk.hasMoreTokens())
		{
			temp = tk.nextToken();
			abs2 += (String)abstract_ID_table.get(temp)+" ";
		}
		*/

		return abs;
	}


	public static void sortBySize()
	{
		String key;
		int i=0;

		cluster_list = new ArrayList(cluster_table.size());

		for (Enumeration e = cluster_table.keys() ; e.hasMoreElements() ;)
		{
			key = (String)e.nextElement();
			c = (Cluster)cluster_table.get(key);
			cluster_list.add(c);
		}

		Collections.sort(cluster_list);

	}

	public static void readData() throws IOException
	{
		String str;

		fr = new FileReader(File_Name_Input);
		inFile = new BufferedReader(fr);

		str = inFile.readLine();
		while(str != null)
		{
			tk = new StringTokenizer(str, ":");
			str = tk.nextToken();
			pattern_list.add(str); //put in main memory
			checkClusterMembership(str);

			str = inFile.readLine();
		}
		inFile.close();
		tk=null;

	}


	public static void readAbstractMappings() throws IOException
	{
		String str, key, member;

		fr = new FileReader(File_Name_Input3);
		inFile = new BufferedReader(fr);

		//------- read abs mappings ---------
		str = inFile.readLine();
		while(str != null)
		{
			tk = new StringTokenizer(str, ":");
			key = tk.nextToken();
			key = key.trim();
			member = tk.nextToken();
			member = member.trim();
			member_table.put(key, member);

			str = inFile.readLine();

		}
		inFile.close();
		//------------------------------------



	}


	public static void main(String[] args) throws IOException
	{
		long time1, time2, time3;

		pattern_list = new ArrayList();
		cluster_table = new Hashtable();
		gene_table = new Hashtable();
		member_table = new Hashtable();
		abstract_ID_table = new Hashtable();

		time1 = System.currentTimeMillis();

		//Get threshold
		/*Double d = new Double(args[0]);*/
		Double d= 5.0;
		THRESHOLD = d.doubleValue();

		//readGeneMappings();
		readAbstractMappings();
		readData();
		sortBySize();
		printClusters();


		time2 = System.currentTimeMillis();
		time3 = (time2-time1)/1000;
		//System.out.println("Execution Time: "+time3+" secs");

	}

	private static class Cluster implements Comparable
	{
		private int totalCount;
		private int memberCount;
		private Hashtable member_table;
		private ArrayList member_list;
		private String members; // not updated, use with caution

		public Cluster(String s)
		{
			totalCount = 0;
			members = s;
			member_table = new Hashtable();
			member_list = new ArrayList();
			member_list.add(s);
			addToMemberTable(s);

		}

		// implementation of the compareTo method
		// descending order
		public int compareTo (Object obj) throws ClassCastException
		{
			Cluster c = (Cluster)obj;

			if(getSize() < c.getSize())
				return 1;

			else if(getSize() > c.getSize())
				return -1;

			return 0;

		}

		// parse the string and add to list.
		private void addToMemberTable(String s)
		{
			Integer dummy = new Integer(99);
			tk = new StringTokenizer(s);

			while(tk.hasMoreTokens())
			{
				member_table.put(tk.nextToken(), dummy);
				totalCount++;
			}
			memberCount++;

		}

		public void addMember(String s)
		{
			//String temp;
			//Integer dummy = new Integer(99);
			//tk = new StringTokenizer(s);

			members +="\n";
			members +=s;
			memberCount++;
			member_list.add(s);

			/********
			while(tk.hasMoreTokens())
			{
				temp = tk.nextToken();
				if(!member_table.containsKey(temp))
				{
					member_table.put(temp, dummy);
					totalCount++;
				}
			}
			********/
		}


		// calculate percentage match with member table
		public double getMatch(String s)
		{
			String temp;
			int matchCount=0;
			int sLen = 0;
			double score = 0.0;

			tk = new StringTokenizer(s);
			while(tk.hasMoreTokens())
			{
				temp = tk.nextToken();
				if(member_table.containsKey(temp))
				{
					matchCount++;
				}
				sLen++;
			}

			score = (matchCount*1.0) / (totalCount+sLen-matchCount);

			return score;

		}

		public int getSize()
		{
			return memberCount;
		}

		public void print()
		{
			System.out.println(members);
		}

		public ArrayList getMemberList()
		{
			return member_list;
		}

		public String getPrint()
		{
			return members;
		}

	}
}