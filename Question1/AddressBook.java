package Question1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class AddressBook {
    private Scanner sc;
    BoyerMoore boyerMoore = new BoyerMoore();
    public void perform() throws IOException {
    	//Get search details as in keyword and field to search for 
        SearchDetails searchDetails = getSearchDetails();
        System.out.println("Keyword to search is: " + searchDetails.pattern);
        //Read the text file 
        ArrayList<String> text = readTextFile();
        String[] arrayName = new String[text.size()];
        arrayName = text.toArray(arrayName);
        //Do search operation
        callBoyer(arrayName, searchDetails);
    }
    
    public SearchDetails getSearchDetails() {
        sc = new Scanner(System.in);
        String pattern = "";
        int choice=0;
        boolean patternReceived=true;
        System.out.println("Please enter search choice: ");
        System.out.println("1. Name: ");
        System.out.println("2. Email Address: ");
        System.out.println("3. Organization: ");
        System.out.println("4. Country: ");
        
        //Asking for user choice on which field to search for
        while (sc.hasNextLine()) {
        	try{
        		choice = Integer.parseInt(sc.nextLine());
        		if(choice>4 || choice<1){
        			// if choice is not between 1 to 5 then raise number format exception 
        			throw new NumberFormatException();
        		}
        		//Its a valid choice, come out of loop
        		break;
        	} catch (NumberFormatException e) {
        		System.out.println("Please eneter a valid choice between 1 to 5");
        	}
        }
        
        //Print what choice user has opted for
        switch(choice){
        case 1:
        	System.out.println("You are searching for Name");
        	break;
        case 2:
        	System.out.println("You are searching for Email Address");
        	break;
        case 3:
        	System.out.println("You are searching for Organization");
        	break;
        case 4:
        	System.out.println("You are searching for Country");
        	break;
        default:
        	System.out.println("Your choice is not correct");
        }
        
        //Get search keyword from user
        while(sc.hasNext()){
        	pattern=sc.nextLine();
        	//If its blank line or just multiple space then ignore it
            if (pattern.replace("\n", "").trim().length() == 0){
                patternReceived = false;
            } else if(pattern.replace("\n", "").trim().length() < 2){
            	//Handles search keyword less then 2 characters error
            	System.out.println("Search keyword has to be of minimum two characters");
            	System.out.println("Please enter new search keyword: ");
            	patternReceived = false;
            }
            //If above two cases passed then its a valid keyword to search for
            if(patternReceived){
            	break;
            } else {
            	System.out.println("");
            }
            //Reset patternReceived flag for each new iteration
            patternReceived = true;
        }
        //return the received pattern as well as field choice
        return new SearchDetails(choice,pattern);
    }
    public ArrayList<String> readTextFile() throws FileNotFoundException, IOException {
        //reads names from textfile. It is important to download the text file \
        //and change the path to where it is saved
        BufferedReader textFile = new BufferedReader(new FileReader("C:\\Users\\milan\\Desktop\\textfinal.txt"));
        String names;
        //adding names into and array list while it is read, it continues until \
        //there are no more names\
        ArrayList<String> list = new ArrayList<>();
        while ((names = textFile.readLine()) != null) {
            list.add(names);
        }
        return list;
    }
    public void callBoyer(String[] arrayName, SearchDetails searchDetails) {
        int flag = 0;
        String[] text;
        int pos=-1;
        for (int i = 0; i < arrayName.length; i++) {
        	//Split fields based on separator ":"
            text = arrayName[i].split(":");
            try{
            	//pass field text and search pattern to boyerMoore algo 
            	pos = boyerMoore.test(text[searchDetails.choice-1], searchDetails.pattern);
            } catch (ArrayIndexOutOfBoundsException e){
            	//If in case data file has some fields missing then ignore those bad records
            	continue;
            }
            
            if (pos != -1) {
            	//pattern found. print it
            	System.out.println(arrayName[i]);
                flag = 1;
            }
        }
        if (flag == 0) {
        	//No record with pattern matched.
            System.out.println("Keyword not found in registry!");
        }
    }

    public static void main(String[] args) throws IOException {
        AddressBook addressBook = new AddressBook();
        addressBook.perform();
        System.exit(0);
    }
}