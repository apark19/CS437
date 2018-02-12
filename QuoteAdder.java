import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QuoteAdder {
    public void addQuote(QuoteList quotelist){
        Quote quote = getNewQuote(quotelist);
        if(quote != null)
            modifyFile(quote);
        }

    //prompts user for quote-text and author name. If author name or if the exact same quote is found, the method returns null.
    //Otherwise, the new quote is returned.
    public Quote getNewQuote(QuoteList quotelist) {
        //ask for user input
        QuoteList searchList, searchList1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a quote to add to the file");
        String newQuoteText = scanner.nextLine(); //any input allowed for quote-text

        System.out.println("Enter the author's name");
        String newAuthor;
        if(scanner.hasNext("[A-Za-z.,]+")) {  //only allows alphanumeric input for author name
            newAuthor = scanner.nextLine();

            Quote newQuote = new Quote(newAuthor, newQuoteText);
            searchList = quotelist.search(newQuote.getAuthor(), 2);
            searchList1 = quotelist.search(newQuote.getQuoteText(), 2);

            if (searchList.getSize() == 0 && searchList1.getSize() == 0)
                return newQuote;
            else {
                if (compareQuote(newQuote, searchList) == 0 || compareQuote(newQuote, searchList1) == 0){
                    System.out.println("That quote already exists");
                    return null;
                }
                else
                    return newQuote;
            }
        }
        else{
            System.out.println("The author's name is invalid. Unable to add this quote to file.");
            return null;    //invalid author name entered
        }
    }

    //This method reads in the file and edits it. A new quote is appended to the end of the file with the correct
    //formatting for xml files.
    public void modifyFile(Quote newQuote){
        String file = "quotes.xml";

        List<String> outLines = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8)){
                if(line.contains("</quote-list>")){
                    outLines.add(line.replace("</quote-list>", "   <quote>"));
                    outLines.add("      <quote-text>" + newQuote.getQuoteText() + "</quote-text>");
                    outLines.add("      <author>" + newQuote.getAuthor() + "</author>");
                    outLines.add("   </quote>");
                    outLines.add("</quote-list>");
                }
                else
                    outLines.add(line);
            }
            Files.write(Paths.get(file), outLines, StandardCharsets.UTF_8);
        }catch(Exception E){
            System.out.println("Unable to add quotations...");
            return;
        }
    }

    //This method compares all the quotes in the searchlist with the new quote to make sure the exact same quote is not present.
    //Returns 1 if quote is not present.
    //Returns 0 if quote is present.
    public int compareQuote(Quote quote, QuoteList searchList){
        for(int i = 0; i < searchList.getSize(); i++){
            if(quote.getQuoteText().compareToIgnoreCase(searchList.getQuote(i).getQuoteText()) == 0)
                return 0;    //exact same quote present
            else
                return 1;
        }
        return 1;
    }
}
