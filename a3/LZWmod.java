/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/

public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int W = 9;         // codeword width
    private static int L = (int) Math.pow(2,W);       // number of codewords = 2^W
    private static boolean isreset = false;

    public static void compress() { 
     //   String input = BinaryStdIn.readString();
	
        TrieST<Integer> st = new TrieST<Integer>();
        for (int i = 0; i < R; i++)
            st.put(new StringBuilder("" + (char) i), i);
        int code = R+1;  // R is codeword for EOF
	if(isreset == true)
	{
	 	BinaryStdOut.write("r");
	}
	StringBuilder current = new StringBuilder();
        char c = BinaryStdIn.readChar();
        current.append(c);
        Integer codeword = st.get(current);
        while (!BinaryStdIn.isEmpty()) {

	   codeword = st.get(current);
            c = BinaryStdIn.readChar();
            current.append(c);
            if(!st.contains(current)){
            	BinaryStdOut.write(codeword, W);
              if (code < L) 
              {   // Add to symbol table if not full
                  st.put(current, code++);
              }
	      else if(W < 16 && code == L)
              {
            	  W++;
            	  L=(int) Math.pow(2,W);
            	  st.put(current,code++);
              }
              
              else if(isreset == true && W==16) 
              {
            	  st= new TrieST<Integer>();
            	  for (int i = 0; i < R; i++)
                      st.put(new StringBuilder("" + (char) i), i);
            	  W=9;
            	  L=(int) Math.pow(2,9);
            	  code = R+1;
            	  st.put(current,code++);
              }
                current = new StringBuilder();
                current.append(c);
           }
	}
	codeword = st.get(current);
        BinaryStdOut.write(codeword, W);
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
	W = 9;
        L = 512;
	boolean res = false;
	if(BinaryStdIn.readChar() == 'r')
	{
	 res = true;
	}

         String[] st = new String[65536];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {

	    if(W < 16 && i>= L){
		W++;
        L = (int)Math.pow(2,W);
	}
	else if(res == true && i <= L && W == 16)
	{
	 
	  W = 9;
	  L = (int)Math.pow(2,W);
	  st = new String[65536];
	  for (i = 0; i < R; i++)
	  {      st[i] = "" + (char) i;}
       	  st[i++] = "";
       	  i = R+1;
	}
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) 
	    {
	      s = val + val.charAt(0);   // special case hack
            }
	    if (i < L){
	      st[i++] = val + s.charAt(0); 
            }
	     val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if(args[0].equals("-")) 
        {
        	if (args[1].equals("n"))
			{
				isreset = false;
			}
			else if (args[1].equals("r"))
			{
				isreset = true;
			}
        	compress();
        }
        else if(args[0].equals("+"))
        {
        	expand();
        }
        else throw new RuntimeException("Illegal command line argument");
    }

}
