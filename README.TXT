Execution Sequence
==================

Execute the JAVA source files in the following order. Make sure the input and output filenames are consistent at each step.

1. preprocessdata.java
2. topdown16.java
3. sortbylength2.java
4. cleanoutput5.java
5. clusterer5.java


Data Preperation
================

Input data file looks like a matrix of data points as rows and features as columns. Each entry represents the frequency of that feature in the data point. A sample "dataset_NG.txt" is placed in the NG_Data folder.


Reading the Output
==================

Output file contains entries that look like "1 21 27 666 932 : 4". Number on the left side of the ":" sign represent the dimension ID and the number on the right side of ":" represent the frequency of the dimensions considered together. In this case, features "1 21 27 666 932" occurred 4 times. A sample output file "maximal_data_NG.txt" is provided in NG_Data folder. 