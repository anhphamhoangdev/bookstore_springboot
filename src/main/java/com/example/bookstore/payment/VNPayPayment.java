package com.example.bookstore.payment;

import com.example.bookstore.entity.*;
import com.example.bookstore.service.InvoiceService;
import com.example.bookstore.service.StockService;
import com.example.bookstore.util.GenerateID;
import com.example.bookstore.vnpay.Config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class VNPayPayment implements PaymentStrategy{
    @Override
    public void pay(GenerateID generateID, InvoiceService invoiceService, HttpSession session, StockService stockService, Model model, String firstName, String address, String paymentMethod, Double subTotal, Double total, HttpServletRequest request) throws UnsupportedEncodingException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceID(generateID.generateBillId());
        invoice.setFirstName(firstName);
        invoice.setAddress(address);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setStatus("Delevering");
        invoice.setOrderDate(new Date());
        // 3 days = 72 * 60 * 60 * 1000 (1000 ms)
        Long threeDays = (long) (72 * 60 * 60 * 1000);
        Date d = new Date(new Date().getTime() + threeDays);
        invoice.setDeliveryDate(d);

        invoice.setSubTotal(subTotal);
        if(invoice.getSubTotal() > 100)
        {
            invoice.setShippingFee(0.0);
        }
        else
        {
            double shippingFee = 10.0;
            invoice.setShippingFee(shippingFee);
            total = subTotal+shippingFee;
        }
        invoice.setTotal(total);
        Cart cart = (Cart) session.getAttribute("cart");
        invoice.setDiscount(cart.getDiscount());
        for (int i = cart.getLineItemList().size() - 1; i >= 0; i--) {
            LineItem l = cart.getLineItemList().get(i);
            invoice.addLineItem(l);
            Stock stock = l.getBook().getStock();
            stock.setQuantity(stock.getQuantity() - l.getQuantity());
            stockService.merge(stock);
            cart.deleteLineItem(l);
        }
        User user = (User) session.getAttribute("user");
        user.addInvoice(invoice);
        model.addAttribute("invoice", invoice);
        session.setAttribute("invoice", invoice);
        invoiceService.save(invoice);

        // 1 USD = 25445 VND
        long amount = (long) (total * 25445 * 100);
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_TmnCode = Config.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", Config.orderType);
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", Config.getIpAddress(request));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        session.setAttribute("paymentUrl", paymentUrl);
    }
}
