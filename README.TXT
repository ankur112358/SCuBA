Execution Sequence
==================

Execute the JAVA source files in the following order. Make sure the input and output filenames are consistent at each step.

1. preprocessdata.java
2. topdown16.java
3. sortbylength2.java
4. cleanoutput5.java
5. clusterer5nomap.java


Data Preperation
================

Input data file looks like a matrix of data points as rows and features as columns. Each entry represents the frequency of that feature in the data point. The data matrix for CLASSIC3 data set is present in "dataset_NG.txt" in the NG_Data folder.


Reading the Output
==================

Output file is "result.txt" in the root folder. It contains entries like "671 5260 7824 : 462 1458 2820 3685" here elements before the ":" represent the group for features ids and elements after ":" refer to the group of document ids which contain these features.
Calculation of accuracy is done by the python code accuracy.py present in the python folder.
It uses col.txt and row.txt which contain the mapping between features & feature id and document id and document types.