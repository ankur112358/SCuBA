import java.io.*;
import java.util.*;

public class PreProcessData
{
	//------------------  PARAMETERS -----------------------//

	private static final int NUM_COLS = 8448;
	private static final String File_Name_Input = "./NG_Data/dataset_NG.txt";	//"dataset_1397.txt";
	private static final String File_Name_Output = "./NG_Data/data_proc_NG.txt";	//"data_proc_1397.txt";

	private static Integer freq1; // ** Depends on data type
	//private static Double freq1;  // ** Depends on data type

	//-------------------------------------------------------//

	private static int freq2;
	private static StringTokenizer tk;
	private static boolean print_flag;


	public static void main(String[] args) throws IOException
	{

		long time1, time2;

		// Output
		File fout = new File(File_Name_Output);
		FileWriter fwrite = new FileWriter(fout);
		BufferedWriter bwrite = new BufferedWriter(fwrite);

		// Input
		FileReader fr = new FileReader(File_Name_Input);
		BufferedReader inFile = new BufferedReader(fr);

		String str1, pattern;
		int i, max, lineCount, len, item_num;
		int[] array = new int[NUM_COLS]; 	// array to store a row


		time1 = System.currentTimeMillis();

		str1 = inFile.readLine();
		lineCount=0;
		max=0;
		while(str1 != null)
		{
			tk = new StringTokenizer(str1);
			i = 0;
			while(tk.hasMoreTokens())
			{
				freq1 = new Integer(tk.nextToken()); // **
				//freq1 = new Double(tk.nextToken()); // **
				freq2 = freq1.intValue();           // **
				array[i] = freq2;
				i++;
			}

			pattern="";
			len=0;
			print_flag = false;
			for(i=0; i < NUM_COLS; i++)
			{
				if(array[i] != 0)
				{
					item_num = i;
					item_num++;
					pattern += item_num+" ";
					len++;
					print_flag = true;
				}
			}
			if(print_flag == true)
			{
				bwrite.write(pattern, 0, pattern.length());
				bwrite.newLine();
			}
			if(len > max)
				max = len;

			str1 = inFile.readLine();
			lineCount++;
		}

		time2 = System.currentTimeMillis();
		inFile.close();
		bwrite.close();

		System.out.println("Data Processing Complete!");
		System.out.println("");
		System.out.println("Number of Lines: "+lineCount);
		System.out.println("Size of Max Pattern: "+max);
		System.out.println("Execution Time: "+(time2-time1));
	}

}
