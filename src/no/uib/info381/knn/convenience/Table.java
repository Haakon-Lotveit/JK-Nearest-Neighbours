package no.uib.info381.knn.convenience;
import java.util.ArrayList;
import java.util.Arrays;


public class Table {
	/***
	 * member vars
	 */
		// # of columns/attributes
		private int nColumns;
		
		// # of rows
		private int nRows;
	
	/***
	 * Just a list of attribute names (like a header)
	 */
	private ArrayList<String> AttributeNames;
	
	/***
	 * List of data entries (table rows), an entry is a list representing data for each attribute
	 * such that AttributeNames[i]'s data for a given entry is Entry[i]
	 */
	private ArrayList<Entry> Entries;
	
	
	/***
	 * Table constructor
	 * @param attributes - arraylist of attribute names
	 */
	public Table(String[] attributes)
	{
		nColumns = attributes.length;
		nRows = 0;
		AttributeNames = new ArrayList<String>(Arrays.asList(attributes));
		Entries = new ArrayList<Entry>();
	}
	
	
	/***
	 * insert data entry into table
	 * @param data
	 * @return
	 */
	public boolean Insert(String[] data)
	{
		if (data.length!=nColumns) return false;
		
		Entry entry = new Entry(data);
		Entries.add(entry);
		
		nRows++;
		return true;
	}
	
	/***
	 * insert data entry into table, here we insert entry object, this is private because
	 * we want to abstract away the data objects involved
	 * @param data
	 * @return
	 */
	private boolean Insert(Entry entry)
	{
		if (entry.Size()!=nColumns) return false;

		Entries.add(entry);

		nRows++;
		return true;
	}
	
	/***
	 * remove an attribute and return a Table with a single attribute of the removed attribute
	 * @param attr
	 * @return
	 */
	public Table RemoveAttribute(String attribute)
	{
		int attrIndex = GetAttributeIndex(attribute);
		return RemoveAttribute(attrIndex);
	}
	
	/***
	 * remove by index, the main work force
	 * @param index
	 * @return
	 */
	public Table RemoveAttribute(int attrIndex)
	{
		if (attrIndex<0) return null;
		if (attrIndex>=this.ColSize()) return null;
		
		String[] attributes = new String[] {AttributeNames.get(attrIndex)};
		Table table = new Table(attributes);
		
		AttributeNames.remove(attrIndex);
		
		for (Entry entry: Entries)
		{
			String[] data = new String[]{entry.Data.get(attrIndex)};
			table.Insert(data);
			entry.Data.remove(attrIndex);
		}
		nColumns--;
		return table;
	}
	
	
	/***
	 * remove all entries specified by the indexes, all the removed entries are returned as a table
	 * with the same attributes
	 * @param indexes
	 * @return
	 */
	public Table RemoveEntries(int[] indexes)
	{
		if (indexes.length<1) return null;

		// this is to compensate index offset after each removal
		int shrink = 0;
		Table table = new Table(AttributeNames.toArray(new String[AttributeNames.size()]));
		for (int index: indexes)
		{
			Entry entry = Entries.remove(index-shrink);
			table.Insert((String[])entry.Data.toArray(new String[AttributeNames.size()]));
			shrink++;
			nRows--;
		}
		return table;
	}
	
	
	/***
	 * like remove entries, but instead of removing simply returning
	 *  all entries specified by the indexes as a table
	 * with the same attributes
	 * @param indexes
	 * @return
	 */
	public Table GetEntries(int[] indexes)
	{
		if (indexes.length<1) return null;

		// this is to compensate index offset after each removal
		Table table = new Table(AttributeNames.toArray(new String[AttributeNames.size()]));
		for (int index: indexes)
		{
			Entry entry = Entries.get(index);
			table.Insert((String[])entry.Data.toArray(new String[AttributeNames.size()]));
		}
		return table;
	}
	
	// arraylist version
	public Table GetEntries(ArrayList<Integer> Indexes)
	{
		int[] indexes = TableHelper.convertIntegers(Indexes);
		return GetEntries(indexes);
	}
	
	/***
	 * Get Row returns row entry at given index, private because we want
	 * to abstract away internal data objects
	 * @return
	 */
	private Entry GetRow(int index)
	{
		return Entries.get(index);
	}
	
	
	/***
	 * Add a table to current table, given input table must have matching attributes
	 * @param table
	 * @return this table
	 */
	public Table AddTable(Table table)
	{
		if (ColSize()!=table.ColSize()) return null;
		// order sensitive
		for (int i=0; i<AttributeNames.size(); i++)
		{
			if (!table.GetAttributeName(i).equalsIgnoreCase(AttributeNames.get(i))) return null;
		}
		
		for (int i=0; i<table.RowSize();i++)
		{
			Insert(table.GetRow(i));
		}
		
		return this;
	}
	
	
	/***
	 * merge current table with another table, if tables have different size, then empty
	 * fields will be filled for entries of the smaller table
	 * @param table
	 * @return
	 */
	public void MergeTable(Table table)
	{
		int newAttributes = table.ColSize();
		AttributeNames.addAll(table.GetAllAttributeNames());
		this.nColumns += newAttributes;
		
		int i=0;
		for (i=0;i<table.RowSize();i++)
		{
			if (i<=this.nRows-1)
			{
				this.Entries.get(i).Data.addAll(table.GetRow(i).Data);
			} else
			{
				String[] data = new String[nColumns];
				Arrays.fill(data, "");
				for (int j=nColumns-newAttributes;j<nColumns;j++)
					data[j] = table.GetCellContent(newAttributes-(nColumns-j), i);
				this.Insert(data);
			}
		}
		// continue fill empty on the other (right) side
		for (;i<this.RowSize();i++)
		{
			String[] data = new String[newAttributes];
			Arrays.fill(data, "");
			Entries.get(i).Data.addAll(Arrays.asList(data));
		}
	}
	
	
	/***
	 * Select and return the content of a cell in an attribute specified by an index
	 * @param attribute
	 * @param row
	 * @return
	 */
	public String GetCellContent(String attribute, int row)
	{
		return GetCellContent(this.GetAttributeIndex(attribute),row);
	}
	
	// by attribute index
	public String GetCellContent(int attrIndex, int row)
	{
		if (attrIndex<0) return null;
		if (row>=nRows) return null;
		if (attrIndex>=nColumns) return null;

		return Entries.get(row).Data.get(attrIndex);
	}
	
	/***
	 * return a cell as a singular table
	 * @param attribute
	 * @param row
	 * @return
	 */
	public Table GetCell(String attribute, int row)
	{
		if (row>=nRows) return null;
		int attrIndex = GetAttributeIndex(attribute);
		if (attrIndex==-1) return null;
		
		Table table = new Table(new String[] {attribute});
		table.Insert(new String[] {Entries.get(row).Data.get(attrIndex)});
		return table;
	}
	
	/***
	 * attributeNames
	 * @return returns a copy of the attribute names
	 */
	public ArrayList<String> GetAllAttributeNames()
	{
		return new ArrayList<String>(AttributeNames);
	}
	
	/***
	 * return the attribute name at a given index
	 * @param index
	 * @return
	 */
	public String GetAttributeName(int index)
	{
		if (index>=nColumns) return null;
		return AttributeNames.get(index);
	}
	
	/***
	 * return the index of an attribute
	 * @param attribute
	 * @return
	 */
	public int GetAttributeIndex(String attribute)
	{
		int attrIndex = -1;
		// find attribute index
		for (int i=0; i<AttributeNames.size();i++) 
		{
			// the search is case insensitive
			if (AttributeNames.get(i).toLowerCase().equals(attribute.toLowerCase()))
				attrIndex = i;
		}
		return attrIndex;
	}
	
	
	/***
	 * get size of the table
	 */
	public int RowSize() {return nRows;}
	public int ColSize() {return nColumns;}
	
	
	/***
	 * shortcut method for printing
	 * @param colWidth
	 */
	public void print(int colWidth)
	{
		this.print(colWidth, -1);
	}
	
	
	/***
	 * Print the table
	 * @param colWidth
	 */
	public void print(int colWidth, int maxRows)
	{
		int indexWidth = 0;
		// calculating index width
		for (int a=Entries.size(); a>0; a=a/10, indexWidth++);
		
		String output = String.format("\n%"+indexWidth+"s| ","");
		for (String attName: AttributeNames)
		{
			output += String.format("%-"+colWidth+"s",attName.toUpperCase());
		}
		System.out.println(output);
		output = "";
		int i=0;
		for (; i<Entries.size() && (i<maxRows || maxRows==-1); i++)
		{
			Entry entry = Entries.get(i);
			output = String.format("%"+indexWidth+"d|  ",i);
			for (String cell: entry.Data)
			{
				output += String.format("%-"+colWidth+"s",cell);
			}
			System.out.println(output);
		}
		if (i<Entries.size())
		{
			
			System.out.printf("%"+indexWidth+"s\n","...");
			
			i = Entries.size()-1;
			Entry entry = Entries.get(i);
			output = String.format("%"+indexWidth+"d|  ",i);
			for (String cell: entry.Data)
			{
				output += String.format("%-"+colWidth+"s",cell);
			}
			System.out.println(output);
		}
			
		System.out.println();
	}
	
	
	/***
	 * class to represent table row entry, private to the table structure
	 * @author Madlion
	 *
	 */
	private class Entry
	{
		private ArrayList<String> Data;
		protected Entry(String[] data)
		{
			Data = null;
			// extra safety
			if (data.length!=nColumns)
				return;
			Data = new ArrayList<String>(Arrays.asList(data));
		}
		
		public int Size()
		{
			return Data.size();
		}
	}
}