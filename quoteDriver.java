package quotes;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

public class quoteDriver {
    private void printHeader(){
        System.out.println("The GMU Quote Generator");
        System.out.println("--------------------------------------------------------------------------");
    }

    private void printMenu(){
        System.out.println("Enter an input based on the following choices:");
        System.out.println("1 - Display another random quote");
        System.out.println("2 - Search by Author");
        System.out.println("3 - Search by Text");
        System.out.println("4 - Search by Both");
        System.out.println("5 - Reset Searches");
        System.out.println("6 - Exit Program");
        System.out.println("--------------------------------------------------------------------------");
    }

    private void printRandomQuote(Quote quote){
        System.out.println("Random quote of the day:");
        System.out.println(quote.getQuoteText());
        System.out.print(" -" + quote.getAuthor());
        System.out.println("\n--------------------------------------------------------------------------");
    }

    private String getSearchText(int mode){
        Scanner scanner = new Scanner(System.in);
        String searchText;

        System.out.print("\nPlease enter a string to search ");
        if(mode == 0){
            System.out.print("authors: ");
            searchText = scanner.nextLine();
        }else if(mode == 1){
            System.out.print("text: ");
            searchText = scanner.nextLine();
        }else{
            System.out.print("both: ");
            searchText = scanner.nextLine();
        }

        return searchText;
    }

    private void printSearchResults(QuoteList searchList){
        System.out.println("\nSearch Results");
        for(int i = 0; i < searchList.getSize(); i++){
            System.out.println(searchList.getQuote(i).getQuoteText());
            System.out.println(" -" + searchList.getQuote(i).getAuthor());
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    private void printRecentSearches(ArrayList<String> recentSearchList){
        if(recentSearchList.size() > 5)
            recentSearchList.remove(0);

        System.out.println("Recent Searches");
        for(int i = 0; i < recentSearchList.size(); i++){
            System.out.println((i+1) + ". " + recentSearchList.get(i));
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    public static void main(String args[]){
        QuoteSaxParser qParser = new QuoteSaxParser("quotes.xml");
        QuoteList quoteList = qParser.getQuoteList();
        quoteDriver demo = new quoteDriver();
        Quote outputQuote;
        String searchText;
        int option;

        //initialize search list
        ArrayList<String> recentSearchList = new ArrayList<String>();

        boolean flag = true;

        while(flag) {
            demo.printHeader();
            outputQuote = quoteList.getRandomQuote();
            demo.printRandomQuote(outputQuote);
            System.out.println();

            //display recent searches
            demo.printRecentSearches(recentSearchList);

            Scanner scanner = new Scanner(System.in);
            try {
                demo.printMenu();
                option = scanner.nextInt();
                switch (option) {
                    case 1:
                        break;
                    case 2:
                    case 3:
                    case 4:
                        searchText = demo.getSearchText(option - 2);
                        QuoteList searchList = quoteList.search(searchText, option - 2);
                        demo.printSearchResults(searchList);
                        recentSearchList.add(searchText);
                        break;
                    case 5:
                        recentSearchList.clear();
                        break;
                    default:
                        flag = false;
                        break;
                }//end switch
            }catch(InputMismatchException e){
                scanner.next();
            }//try-catch block
        }//end while
    }
}
