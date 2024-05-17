package com.example.bookstore.util;

import com.example.bookstore.entity.Discount;
import com.example.bookstore.entity.Invoice;
import com.example.bookstore.entity.LineItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class EmailSenderService {
    private JavaMailSender mailSender;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("onlinebookstorevn@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        System.out.println("EMAIL SENT SUCCESSFULLY !!");
    }

    public void sendHtmlMail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart
            helper.setFrom("onlinebookstorevn@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml
            mailSender.send(message);
            System.out.println("HTML EMAIL SENT SUCCESSFULLY !!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String createBillHtmlEmail(HttpSession session, String firstName) throws IOException
    {
        Invoice invoice = (Invoice) session.getAttribute("invoice");
        String content = "<html><body><h3>Hi, "+firstName+" ! </h3><br>";
        content += "<span>Here is your bill information : </span><br>";
        content += "<span>BILL ID : </span><strong>" + invoice.getInvoiceID()+"</strong>";
        content += "<table style='width:60%;'>";
        content += "<tr><th>Book Name</th><th>Quantity</th><th>Unit Price</th><th>Total Price</th></tr>";
        List<LineItem> lineItems = invoice.getLineItems();
        for (LineItem lineItem : lineItems) {
            String itemName = lineItem.getBook().getBookName();
            int quantity = lineItem.getQuantity();
            double price = lineItem.getBook().getSellPrice();
            double totalprice = lineItem.getQuantity() * lineItem.getBook().getSellPrice();
            // Create a row for each line item
            String lineItemRow = "<tr><td>" + itemName + "</td><td>" + quantity + "</td><td>$ " + price + "</td><td>$ " + totalprice + "</td></tr>";
            content += lineItemRow;
        }
        content += "</table><br>";
        content += "<span>Sub Total :</span> $" + invoice.getSubTotal() + "<br>";
        content += "<span>Discount :</span> " + invoice.getDiscount() + "%<br>";
        content += "<span>Shipping :</span> $" + invoice.getShippingFee() + "<br>";
        content += "<span>Total :</span><strong> $" + invoice.getTotal()+"</strong>";
        content += "</body></html>";
        String emailContent = content;
        return emailContent;
    }

    public String createDiscountHtmlEmail(Discount discount) throws IOException
    {
        String content = "<h3><strong><span>***</span> We've got a special" + discount.getDiscountAmount() + "% discount just for you <span>***</span></strong></h3><br>" +
                "YOUR DISCOUNT CODE: <div style=\"border: 1px solid black; padding: 5px; display: inline-block;\"><strong>" + discount.getDiscountCode() + "</strong></div>\n" +
                "<h3><strong>YOU CAN USE THIS COUPON ONE TIME</strong></h3><br>";
        String emailContent = content;
        return emailContent;
    }


}
