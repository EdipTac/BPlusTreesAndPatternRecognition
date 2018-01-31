package Asg4;

import java.util.LinkedList;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class InvertedIndex {
  
  private static Scanner sc;
  public static final int HTSIZE = 10000;
  
  //please change the destination of your input-file to that of the file within your system
  public static final String INPUT_FILE ="/Users/aidasharifrohani/Desktop/asg4text.txt";
  private static ArrayList<LinkedList<Tuple>> Table;
  private static byte[] ArrUTF;
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
        return list;
    }
    
  //get the array of each line of information and in each line separates 
      //the organization info which we need for inverted index
  public static String[] FieldOrganization(String[] arrayName) {

        String[] arrayOrganization = new String[arrayName.length];
        for (int i = 0; i < arrayName.length; i++) {
            arrayName[i]=arrayName[i].replaceAll(":", " : ");
            String[] split = arrayName[i].split(":");
            arrayOrganization[i] = split[2].toLowerCase();
        }
        return arrayOrganization;
    }
//gets the array of organization names and in each line makes an array of
  //the words in that specific organization
     public static void foldingHash(String[] arrayOrganization, InvertedIndex tbl) throws UnsupportedEncodingException {
        for (int i = 0; i < arrayOrganization.length; i++) {
            String[] split2 = arrayOrganization[i].split("\\s");
            for (int j = 0; j < split2.length; j++) {
                tbl.addIndex(split2[j], i);
            }
        }
    }
     
     
//creates an array list of linked lists so for each word with a specific hashing
     //index it adds a linked list of all the lines which that name repeats
  public InvertedIndex()
  {
    Table = new ArrayList<LinkedList<Tuple>>();
    for(int i=0;i<HTSIZE;++i)
      Table.add(new LinkedList<Tuple>());
  }
  
  //searches for the key word, first gets the hash index then reads the
  //linked list for the list of occurences
    public static ArrayList<Integer> lookup(String query)
  {
    int index = hash(query);
    
    ArrayList<Integer> l = new ArrayList();
    
    for(Tuple x : Table.get(index))
    {
      if (query.equals(x.name))
        l.add(x.id);
    }
    
    return l;
  }
  //adds the line number to the name of the organization
    private static class Tuple 
  {
    String name;
    Integer id;
    
    Tuple(String name, Integer id)
    {
      this.name = name;
      this.id = id;
    }
  }
  //adds the index
  public static void addIndex(String term, int id)
  {
    int index = hash(term);
    Tuple p = new Tuple(term,id);
    Table.get(index).add(p);
  }
  

  //creates index number
  public static int hash(String name)
  {
     int hashIndex = 0;
     name = name.replaceAll("\\\\s", "");
     ArrUTF = name.getBytes(Charset.forName("UTF-8"));
     for(int j = 0; j < ArrUTF.length; j++)
     {
         hashIndex = hashIndex + ArrUTF[j];
         hashIndex = hashIndex % HTSIZE;
     }
        return hashIndex;
  }
  
  //asks the user for the keyword to search
   public static String getChoice() {
        sc = new Scanner(System.in);
        String pattern = "";
        System.out.println("\nPlease enter search keyword: ");
        if (sc.hasNextLine()) {
            pattern = sc.nextLine();
            while (pattern.trim().length() == 0) {
                System.out.println("\nNo keyword was found.");
                System.out.println("\nPlease enter search keyword: ");
                pattern = sc.nextLine();
            }
        }
        return pattern.toLowerCase().replaceAll("\\\\s", "");
    }
//gets the result of occurences for the keyword
   public static void getName(ArrayList<Integer> res, String[] arrayName) {
        for (int i = 0; i < res.size(); i++) {
            String name = arrayName[res.get(i)];
            System.out.println("\nNames of people which has keyword in their organization: " + name);
        }
        System.out.println("\nTotal of: " + res.size() + "  people have this keyword in Organization");
    }
  
  public static void main(String[] args) throws IOException {
    
    InvertedIndex tbl = new InvertedIndex();
    
    String pattern = getChoice();
    System.out.println("\nKeyword to search is: " + pattern);
    ArrayList<String> text = readTextFile();
    String[] arrayName = new String[text.size()];
    arrayName = text.toArray(arrayName);
    String[] arrayOrganization = FieldOrganization(arrayName);
    foldingHash(arrayOrganization,tbl);
    
    ArrayList<Integer> res = lookup(pattern);
    System.out.println(res);
    getName(res, arrayName);
   
  }
}
