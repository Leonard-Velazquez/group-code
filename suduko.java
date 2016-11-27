package sudoku;

	import java.util.ArrayList;
	import org.jacop.constraints.Alldistinct;
	import org.jacop.constraints.XneqY;
	import org.jacop.core.IntVar;
	import org.jacop.core.Store;
	import org.jacop.examples.fd.ExampleFD;



	public class suduko extends ExampleFD {

		IntVar[][] elements;
		
		@Override
		public void model() {

			
			// >0 - known element
			// 0 - unknown element
			
			int[][] description = { { 5, 3, 0, 0, 7, 0, 0, 0, 0 },
								    { 6, 0, 0, 1, 9, 5, 0, 0, 0 }, 
								    { 0, 9, 8, 0, 0, 0, 0, 6, 0 },
								    
								    { 8, 0, 0, 0, 6, 0, 0, 0, 3 }, 
								    { 4, 0, 0, 8, 0, 3, 0, 0, 1 },
								    { 7, 0, 0, 0, 2, 0, 0, 0, 6 }, 
								    
								    { 0, 6, 0, 0, 0, 0, 2, 8, 0 },
								    { 0, 0, 0, 4, 1, 9, 0, 0, 5 }, 
								    { 0, 0, 0, 0, 8, 0, 0, 7, 9 } };
			
	
			// No of rows and columns in a box.
			int noRows = 3;
			int noColumns = 3;

			// Store a jacop
			
			
			store = new Store();
			
			// vars 
			
			vars = new ArrayList<IntVar>();
			
			elements = new IntVar[noRows * noColumns][noRows * noColumns];

			// Creating variables.
			for (int i = 0; i < noRows * noColumns; i++)
				for (int j = 0; j < noRows * noColumns; j++)
					if (description[i][j] == 0) {
						elements[i][j] = new IntVar(store, "f" + i + j, 1, noRows * noColumns);
						vars.add(elements[i][j]);
					}
					else
						elements[i][j] = new IntVar(store, "f" + i + j,
								description[i][j], description[i][j]);

			// Creating constraints for rows.
			for (int i = 0; i < noRows * noColumns; i++)
				store.impose(new Alldistinct(elements[i]));

			// Creating constraints for columns.
			for (int j = 0; j < noRows * noColumns; j++) {
				IntVar[] column = new IntVar[noRows * noColumns];
				for (int i = 0; i < noRows * noColumns; i++)
					column[i] = elements[i][j];

				store.impose(new Alldistinct(column));
			}

			// Creating constraints for blocks.
			for (int i = 0; i < noRows; i++)
				for (int j = 0; j < noColumns; j++) {

					ArrayList<IntVar> block = new ArrayList<IntVar>();
					for (int k = 0; k < noColumns; k++)
						for (int m = 0; m < noRows; m++)
							block.add(elements[i * noColumns + k][j * noRows + m]);

					store.impose(new Alldistinct(block));

				}

		}

		public void modelBasic() {

			// >0 - known element
			// 0 - unknown element
			int[][] description = { { 5, 3, 0, 0, 7, 0, 0, 0, 0 },
				                  { 6, 0, 0, 1, 9, 5, 0, 0, 0 }, 
				                  { 0, 9, 8, 0, 0, 0, 0, 6, 0 },
				    
				                  { 8, 0, 0, 0, 6, 0, 0, 0, 3 }, 
				                  { 4, 0, 0, 8, 0, 3, 0, 0, 1 },
				                  { 7, 0, 0, 0, 2, 0, 0, 0, 6 }, 
				    
				                  { 0, 6, 0, 0, 0, 0, 2, 8, 0 },
				                  { 0, 0, 0, 4, 1, 9, 0, 0, 5 }, 
				                  { 0, 0, 0, 0, 8, 0, 0, 7, 9 } };

			// No of rows and columns in a box.
			int noRows = 3;
			int noColumns = 3;

			store = new Store();
			vars = new ArrayList<IntVar>();
			
			elements = new IntVar[noRows * noColumns][noRows * noColumns];

			// Creating variables.
			for (int i = 0; i < noRows * noColumns; i++)
				for (int j = 0; j < noRows * noColumns; j++)
					if (description[i][j] == 0) {
						elements[i][j] = new IntVar(store, "f" + i + j, 1, noRows * noColumns);
						vars.add(elements[i][j]);
					}
					else
						elements[i][j] = new IntVar(store, "f" + i + j,
								description[i][j], description[i][j]);

			// Creating constraints for rows.
			for (int i = 0; i < noRows * noColumns; i++)
				for (int k = 0; k < noRows * noColumns; k++)
					for (int j = k + 1; j < noRows * noColumns; j++)
						store.impose(new XneqY(elements[i][k], elements[i][j]));

			// Creating constraints for columns.
			for (int i = 0; i < noRows * noColumns; i++)
				for (int k = 0; k < noRows * noColumns; k++)
					for (int j = k + 1; j < noRows * noColumns; j++)
						store.impose(new XneqY(elements[k][i], elements[j][i]));

			// Creating constraints for blocks.
			for (int i = 0; i < noRows; i++)
				for (int j = 0; j < noColumns; j++) {

					ArrayList<IntVar> block = new ArrayList<IntVar>();
					for (int k = 0; k < noColumns; k++)
						for (int m = 0; m < noRows; m++)
							block.add(elements[i * noColumns + k][j * noRows + m]);

					for (int k = 0; k < noColumns*noRows; k++)
						for (int m = k + 1; m < noColumns*noRows; m++)
							store.impose(new XneqY(block.get(k), block.get(m)));

				}
			
		}
		
		
	    
	
		public static void main(String args[]) {

			suduko example = new suduko();
			
			example.model();

			if (example.searchSmallestDomain(false))
				System.out.println("Solution(s) found");
			
			ExampleFD.printMatrix(example.elements, example.elements.length, example.elements[0].length);
			
			example = new suduko();
			
		}		

		
		
		public static void test(String args[]) {

			suduko example = new suduko();
			
			example.model();

			if (example.searchSmallestDomain(false))
				System.out.println("Solution(s) found");
			
			ExampleFD.printMatrix(example.elements, example.elements.length, example.elements[0].length);
			
			example = new suduko();
			
			example.modelBasic();

			if (example.searchSmallestDomain(false))
				System.out.println("Solution(s) found");

			ExampleFD.printMatrix(example.elements, example.elements.length, example.elements[0].length);

		}		


	
}
