package Asg4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.Arrays;
import java.lang.Math;

//------------------------------------------------------------
	//Assignment: 4
	//File name: BTreePlus.java
	//Written by: Aida Rohani ID 21341669, Edip Tac ID 26783287, Faezeh Mobasheri ID 26821022, Milan Jetha ID 40013982
	//For Comp 5511 Section DD/Fall 2017
	//------------------------------------------------------------

	/*---------------------------------PROGRAM DESCRIPTIONS---------------------------------------------------
	 * The program takes an input of text file and searches through the file to check whether the name requested
	 * by the user is available in the text file, and displays the number of nodes accessed to find the name. 
	 * The specific method used for this program is the B+ tree and the program is optimized to work specifically
	 * with the data file provided by the instructor (ds17s-asg2-data.txt).


	/**
	 * @author Aida Rohani <ID 21341669>
	 * @author Edip Tac <ID 26783287>
	 * @author Faezeh Mobasheri <ID 26821022>
	 * @author Milan Jetha <ID 40013982>
	 * 
	 */	
public class BTreePlus
{
	//Please change the location of this txt file to the correct destination within your own device
   public static final String INPUT_FILE ="C:\\Users\\Edip\\workspace\\Comp5511Asg4\\src\\Asg4\\ds17s-asg2-data.txt";
   //this is the counter that tracks the number of nodes that have been accessed
   public static int counter = 0;
   //the load factor M
   public static final int M=5; // between 5 (6) to 10 (11) keys (children)
   
   //as per the requirements of the question the maxload that the nodes will have is set to 60%
   public static final double MAXLOAD = 0.6;
   
   
   /*
    * Internal class Node for the nodes of the B+ tree
    */
   private class Node
   {
      int keyCount;  
      String[] keys;
      Node[] children;
      boolean isLeaf;
      
      Node(boolean leaf_flag)
      {
        this.keyCount = 0;
        //as per the question we can have 2M keys and 2M+1 pointers
        this.keys = new String[2*M];
        this.children = new Node[2*M+1];
        this.isLeaf = leaf_flag;
      }
      
   }
   
  	static Node root;	
  	public static int height;
  	
  	
  	//default constructor
  	public BTreePlus()
  	{	
  	  Node r = new Node(true);
  	  this.root = r;
  	  this.height = 1;
  	}
  	
  	// this constructor implements the bottom up approach as per the question's requirements
  	public BTreePlus(String[] list)
  	{
  		// the total number of entries within the .txt file provided
  	  final int N = list.length;
  	  //using the formula provided during the lecture was not sufficient as data was missing.
  	  //To compensate for this, we opted to create an integer called extraKeysPerNode similar to the
  	  //problem done in class where every 10th addition had extra keys to makeup for the missing ones/
  	  final int extraKeysPerNode = (int) Math.floor((MAXLOAD * 2 * M) - M);
  	  Arrays.sort(list);
  	  
  	  Node childLevel[] = new Node[N];
  	  Node parentLevel[] = new Node[N];
  	  
  	  // creating leaves for the tree
  	  int listIndex = 0;
  	  int L = (int) Math.floor(N / M);
  	  int numExtraKeys = N % M;
  	  
  	  System.out.println("input size="+N);
  	  
  	  for(int i=0;i<L;++i)
  	  { 
  	        Node node = new Node(true);
  	        
  	        int KeyPerNode;
  	        if (numExtraKeys > 0)
  	        {
  	            int delta = Math.min(numExtraKeys, extraKeysPerNode);
  	            KeyPerNode = M + delta;
  	            numExtraKeys -= delta;
  	        }
  	        else
  	            KeyPerNode = M;
  	    
  	        for(int j=0;j<KeyPerNode && listIndex < N;++j)
  	        {
  	            node.keys[j] = list[listIndex];
  	            ++listIndex;
  	            ++node.keyCount;
  	            
  	            System.out.println(node.keys[j]);
  	        }
  	        
  	        System.out.println("leaf "+i+" created");
  	        
  	        
  	        
  	        childLevel[i] = node;
     }
     
     this.height = 1;
     System.out.println();
  	  
  	  // creating parent levels iteratively
  	  do
  	  {
  	     System.out.println("L is "+L);
  	     int P = (int) Math.floor(L / M);
  	     System.out.println("P is "+P);
  	     numExtraKeys = L % M;
  	     System.out.println("extra keys: "+numExtraKeys);
  	     listIndex = 0;
  	     
  	     for(int i=0;i<P;++i)
  	     { 
  	        Node node = new Node(false);
  	        
  	        int KeyPerNode;
  	        if (numExtraKeys > 0)
  	        {
  	            int delta = Math.min(numExtraKeys, extraKeysPerNode);
  	            KeyPerNode = M + delta;
  	            numExtraKeys -= delta;
  	        }
  	        else
  	            KeyPerNode = M;
  	    
  	        int j;
  	        for(j=0;j<KeyPerNode && listIndex < L-1;++j)
  	        {
  	            node.children[j] = childLevel[listIndex];
  	            node.keys[j] = childLevel[listIndex+1].keys[0];
  	            ++listIndex;
  	            ++node.keyCount;
  	            System.out.println(node.keys[j]);
  	        }
  	        
  	        System.out.println("listIndex="+listIndex);
  	        
  	        node.children[j] = childLevel[listIndex];
  	        //++listIndex;
  	        System.out.println("node "+i);
  	        parentLevel[i] = node;
        }
  	     
  	     L = P;
  	     
  	     for(int i=0;i<L;++i)
  	        childLevel[i] = parentLevel[i];
  	         
  	     ++this.height;
  	     
  	  } while(L>=M);
  	  
  	  if (L==1)
  	      this.root = childLevel[0];
  	  else
  	  {   System.out.println("making a root");
  	      listIndex = 0;
      	  Node r = new Node(false);
  	  
  	      for(int i=0;i<L-1;++i)
  	      {
  	          r.children[i] = childLevel[i];
  	          r.keys[i] = childLevel[i+1].keys[0];
  	          ++listIndex;
  	          ++r.keyCount;
       	  }
  	  
  	      r.children[L-1] = childLevel[listIndex];
  	     
  	      this.root = r;
  	   }
  	    
  	  ++this.height;
  	  
  	  System.out.println("done. height is "+height);
  }
  
   
   public String search(String target)
   {
      Node l = searchIt(this.root,target);
    
      int i;
      for(i = 0; i < l.keyCount; ++i)
           if (target.equals(l.keys[i]))
              return "Found";
              
        return "Not Found";
   }
  
   Node searchIt(Node p, String target)
   { 
      if (p.isLeaf)
      {
        System.out.println("leaf");
        counter++;
        return p;
      }
      
      int i = 0;
      System.out.println("comparing " + target + " to " + p.keys[i]);
      while (i < p.keyCount && target.compareTo(p.keys[i]) > 0)
      {
        System.out.println("compared " + target + " to " + p.keys[i]);
        System.out.println("compareTo says "+target.compareTo(p.keys[i]));
        counter++;
        ++i;
      }
      System.out.println("jumping...");
      return searchIt(p.children[i],target);
   }
   
   private void walkIt(Node p)
   {
     if (p.isLeaf)
     {
       System.out.println("LEAF contents  " + p.keyCount);
       
       
       for(int i=0;i<p.keyCount;++i)
         System.out.println(p.keys[i]);
       return;
     }
     
     System.out.println("key count is " + p.keyCount);
     for(int i=0;i<p.keyCount;++i)
         System.out.println(p.keys[i]);
   
   walkIt(p.children[p.keyCount]);
   }
   
   public void walk()
   {
     walkIt(root);
   }
   
   public static ArrayList<String> readTextFile() throws FileNotFoundException, IOException
   {
        //reads names from textfile. It is important to download the text file 
        //and change the path to where it is saved
        BufferedReader textFile = new BufferedReader(new FileReader(INPUT_FILE));
        String names;
        //adding names into and array list while it is read, it continues until 
        //there are no more names
        ArrayList<String> list = new ArrayList<>();
        while ((names = textFile.readLine()) != null) {
            list.add(names);
        }
      //this is done to remove all the blank spaces so that the text is compared accurately
        for (int i = 0; i < list.size(); i++) {
        	list.set(i, list.get(i).replaceAll("\\s", ""));
		}
        return list;
    }
    
    public static void main(String[] args) throws IOException
    {
        ArrayList<String> text = readTextFile();
        String[] arrayName = new String[text.size()];
        arrayName = text.toArray(arrayName);
        
        BTreePlus tree = new BTreePlus(arrayName);
        
        //String searchKey = "Revesz, Peter";
        String searchKey = "Revesz, Peter";
        searchKey=searchKey.replaceAll("\\s", "");
        String res1 = tree.search(searchKey);
        //String res2 = tree.search("akbar");
        
        System.out.println("\nFirst result: " + searchKey+ " has been "+ res1);
        System.out.println("The total number of nodes entered are: " + counter);
        //System.out.println("\nSecond result: " + res2);
        
        System.out.println("Press return to end the program");
        System.in.read();
        System.exit(0);
    }
}