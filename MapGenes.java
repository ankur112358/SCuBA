
import java.io.*;
import java.util.*;

public class MapGenes
{

	private static final String File_Name_Input_1 = "./Data/gene_map_1397.txt"; //"map.txt"; //
	private static final String File_Name_Input_2 = "./Data/maximal_data_1397.txt"; //"map2.txt"; //
	private static final String File_Name_Input_3 = "./Data/abstract_id.txt";
	private static final String File_Name_Input_4 = "./Data/output_members_1397.txt";

	private static final String File_Name_Output =	"./Data/temp_map.txt";//"mapped_data_1397.txt";

	// Output Files
	private static File fout;
	private static FileWriter fwrite;
	private static BufferedWriter bwrite;

	// Input Files
	private static FileReader fr;
	private static BufferedReader inFile;

	private static StringTokenizer tk;
	private static Hashtable table_final;
	private static Hashtable table_members;
	private static Hashtable table_abstract_ID;


	public static void readMappings() throws IOException
	{
		String str, key;
		int id;

		fr = new FileReader(File_Name_Input_1);
		inFile = new BufferedReader(fr);

		//------- read gene mappings ---------
		id = 1;
		str = inFile.readLine();
		while(str != null)
		{
			key = id+"";
			table_final.put(key, str);
			str = inFile.readLine();
			id++;
		}
		inFile.close();
		//------------------------------------

		//-------- read abstract id ----------
		fr = new FileReader(File_Name_Input_3);
		inFile = new BufferedReader(fr);

		id = 1;
		str = inFile.readLine();
		while(str != null)
		{
			key = id+"";
			table_abstract_ID.put(key, str);
			str = inFile.readLine();
			id++;
		}
		inFile.close();
		//------------------------------------

		//-------- read members --------------
		String member;
		fr = new FileReader(File_Name_Input_4);
		inFile = new BufferedReader(fr);

		str = inFile.readLine();
		while(str != null)
		{
			tk = new StringTokenizer(str, ":");
			str = tk.nextToken();
			member = tk.nextToken();
			str = str.trim();
			member = member.trim();
			table_members.put(str, member);

			str = inFile.readLine();
		}

		inFile.close();



		//------------------------------------
	}

	public static void mapGenes() throws IOException
	{
			String str, str2, str3, str4,str5, str6, count, gene, members, abstracts;

			abstracts="";

			//Input
			fr = new FileReader(File_Name_Input_2);
			inFile = new BufferedReader(fr);

			// Output
			fout = new File(File_Name_Output);
			fwrite = new FileWriter(fout);
			bwrite = new BufferedWriter(fwrite);

			str = inFile.readLine();

			while(str != null)
			{
				str = str.trim();
				//-------- maps gene-----------------
				gene="";
				tk = new StringTokenizer(str, ":");
				str2 = tk.nextToken();
				str2 = str2.trim();
				count = tk.nextToken();

				tk = new StringTokenizer(str2);
				gene = "(genes): ";
				while(tk.hasMoreTokens())
				{
					str3 = tk.nextToken();
					gene += map(str3)+" ";
				}
				//gene += ": "+count;

				// Write to file
				str6 = "--------------- Cluster --------------";
				bwrite.write(str6, 0, str6.length());
				bwrite.newLine();
				bwrite.write(gene, 0, gene.length());
				bwrite.newLine();
				//-------- maps gene-----------------

				//-------- get, map abstracts -------
				abstracts = "";
				int counter=0;
				if(table_members.containsKey(str2))
				{
					members = (String)table_members.get(str2);

					tk = new StringTokenizer(members);
					abstracts = "PubMed ID\n";
					abstracts += "---------\n";
					while(tk.hasMoreTokens())
					{
						str4 = tk.nextToken();
						abstracts += mapAbstract(str4)+"\n";
						counter++;
					}
					abstracts +="\n";
					abstracts +="support: "+counter+"\n";

				// Write to file
				bwrite.write(abstracts, 0, abstracts.length());
				bwrite.newLine();
				}
				//-------- get, map abstracts -------

				str = inFile.readLine();
			}
			inFile.close();
			bwrite.close();
			tk=null;
	}

	private static String mapAbstract(String str)
	{
		String ab;

		ab = (String)table_abstract_ID.get(str);

		return ab;
	}

	private static String map(String str)
	{
		String gene;

		gene = (String)table_final.get(str);

		return gene;
	}


	public static void printToFile() throws IOException
	{
		String key;

		// Output
		fout = new File(File_Name_Output);
		fwrite = new FileWriter(fout);
		bwrite = new BufferedWriter(fwrite);

		for (Enumeration e = table_final.keys() ; e.hasMoreElements() ;)
		{
			key = (String)e.nextElement();
			System.out.print(key);
			System.out.print(" :");
			System.out.println(table_final.get(key));
		}

		bwrite.close();
	}

	public static void main(String[] args) throws IOException
	{
		long time1, time2, time3;

		table_final = new Hashtable();
		table_members = new Hashtable();
		table_abstract_ID = new Hashtable();


		time1 = System.currentTimeMillis();
		readMappings();
		mapGenes();
		//printToFile();

		time2 = System.currentTimeMillis();
		time3 = (time2-time1)/1000;
		System.out.println("Execution Time: "+time3+" secs");
	}
}