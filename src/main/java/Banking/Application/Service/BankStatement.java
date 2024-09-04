package Banking.Application.Service;
import Banking.Application.Dto.EmailDetails;
import Banking.Application.Entity.Transaction;
import Banking.Application.Entity.User;
import Banking.Application.Repository.TransactionRepository;
import Banking.Application.Repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private  EmailService emailService;
    private static final String File= "D:\\Spring Boot folder\\Projects\\Banking Statements\\statements.pdf";

    public List<Transaction> generateStatement (String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getTransactionAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start)).filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();


        User user = userRepository.findByAccountNumber(accountNumber);
        if (user == null) {
            log.error("User not found for account number: {}", accountNumber);
            throw new IllegalArgumentException("User not found for account number: " + accountNumber);
        }

        String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
        Rectangle statementSize= new Rectangle(PageSize.A4);
        Document document= new Document(statementSize);
        log.info("setting Size of document");
        OutputStream outputStream= new FileOutputStream(File);
        PdfWriter.getInstance(document,outputStream);
        document.open();

        PdfPTable bankInfoTable= new PdfPTable(1);
        PdfPCell bankName =new PdfPCell(new Phrase("KIMZ Bank") );
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.GREEN);
        bankName.setPadding(10f);
        PdfPCell bankAddress =new PdfPCell(new Phrase("29, FUNNY, ADDRESS KENYA"));
        bankAddress.setBorder(0);
        bankAddress.setBackgroundColor(BaseColor.GREEN);
        bankAddress.setPadding(10f);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);
        PdfPTable  statementInfo= new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date  " + startDate));
        customerInfo.setBorder(0);
        customerInfo.setBackgroundColor(BaseColor.GREEN);
        PdfPCell statement=new PdfPCell(new Phrase("STATEMENT ACCOUNT"));
        statement.setBorder(0);
        statement.setBackgroundColor(BaseColor.GREEN);

        PdfPCell stopDate=new PdfPCell(new Phrase("End Date  " + endDate));
        stopDate.setBorder(0);
        stopDate.setBackgroundColor(BaseColor.GREEN);
        PdfPCell name=new PdfPCell(new Phrase("Customer Name " + customerName));
         name.setBorder(0);
         name.setBackgroundColor(BaseColor.GREEN);
         PdfPCell space = new PdfPCell();
         space.setBorder(0);
         space.setBackgroundColor(BaseColor.GREEN);
         PdfPCell address= new PdfPCell(new Phrase("Address  " + user.getAddress()));
         address.setBorder(0);
         address.setBackgroundColor(BaseColor.GREEN);
         PdfPTable transactionsTable= new PdfPTable(4);
         PdfPCell date= new PdfPCell(new Phrase("DATE"));
         date.setBorder(0);
         date.setBackgroundColor(BaseColor.ORANGE);
         PdfPCell transactionType= new PdfPCell(new Phrase("TRANSACTION TYPE"));
         transactionType.setBorder(0);
         transactionType.setBackgroundColor(BaseColor.ORANGE);
         PdfPCell transactionAmount= new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
         transactionAmount.setBackgroundColor(BaseColor.ORANGE);
         transactionAmount.setBorder(0);
         PdfPCell status= new PdfPCell(new Phrase("STATUS"));
         status.setBorder(0);
         status.setBackgroundColor(BaseColor.ORANGE);

         transactionsTable.addCell(date);
         transactionsTable.addCell(transactionType);
         transactionsTable.addCell(transactionAmount);
         transactionsTable.addCell(status);

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

         transactionList.forEach(transaction -> {
             transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
             transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
             transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
             transactionsTable.addCell(new Phrase(transaction.getTransactionStatus()));

         });



         document.add(bankInfoTable);
         document.add(statementInfo);
         document.add(transactionsTable);



document.close();

        EmailDetails emailDetails= EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested account statement")
                .attachment(File)
                .build();

         emailService.sendEmailAttachment(emailDetails);





        return transactionList;
    }

    }


