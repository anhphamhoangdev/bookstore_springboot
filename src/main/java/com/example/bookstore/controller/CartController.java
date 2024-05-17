package com.example.bookstore.controller;

import com.example.bookstore.entity.*;
import com.example.bookstore.service.*;
import com.example.bookstore.util.GenerateID;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/cart")
public class CartController {


    private final GenerateID generateID;
    private BookService bookService;

    private CartService cartService;

    private LineItemService lineItemService;
    private DiscountService discountService;


    @Autowired
    public CartController(BookService bookService, CartService cartService, LineItemService lineItemService, GenerateID generateID, DiscountService discountService) {
        this.bookService = bookService;
        this.cartService = cartService;
        this.lineItemService = lineItemService;
        this.generateID = generateID;
        this.discountService = discountService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }


    @RequestMapping("")
    public String cart(HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart != null){
            double subtotal = cart.subTotal();
            double total = cart.total();
            session.setAttribute("subtotal", subtotal);
            session.setAttribute("total", total);
        }
        return "cart";
    }

    @RequestMapping("/add-to-cart")
    public String addToCart(HttpSession session, @RequestParam("bookID") String bookID, @RequestParam("quantity") int quantity){
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart != null){

            // check book exist
            Book book = bookService.getBookById(bookID);
            if(book == null)
            {
                session.setAttribute("error", "BOOK NOT FOUND...");
                throw new EntityNotFoundException("Book not found");
            }
            if(book.getStock().getQuantity() <= 0)
            {
                return "cart";
            }
            // check lineItem is contained in cart or not
            for(LineItem lineItem : cart.getLineItemList())
            {
                if(lineItem.getBook().getBookID().equals(bookID))
                {
                    if((lineItem.getQuantity() + quantity) >= book.getStock().getQuantity())
                    {
                        lineItem.setQuantity(book.getStock().getQuantity());
                    }
                    else lineItem.setQuantity(lineItem.getQuantity() + quantity);
                    lineItemService.merge(lineItem);
                    return "cart";
                }
            }
            LineItem lineItem = new LineItem();
            lineItem.setLineItemID(generateID.generateLineItemId());
            lineItem.setBook(book);
            lineItem.setQuantity(quantity);
            lineItemService.save(lineItem);
            cart.addLineItem(lineItem);
            cartService.merge(cart);
        }
        return "cart";
    }

    @RequestMapping("/update")
    public String update(HttpSession session, @RequestParam("bookID") String bookID, @RequestParam("quantity") int quantity){
        Cart cart = (Cart) session.getAttribute("cart");
        List<LineItem> lineItemList = cart.getLineItemList();
        if(quantity < 0)
        {
            quantity = 1;
        }
        if(cart != null){

            // check book exist
            Book book = bookService.getBookById(bookID);
            if(book == null)
            {
                session.setAttribute("error", "BOOK NOT FOUND...");
                throw new EntityNotFoundException("Book not found");
            }
            // check lineItem is contained in cart or not
            for(LineItem lineItem : cart.getLineItemList())
            {
                if(lineItem.getBook().getBookID().equals(bookID))
                {
                    if(quantity == 0)
                    {
                        cart.deleteLineItem(lineItem);
                        lineItemService.delete(lineItem.getLineItemID());
                        break;
                    }else
                    {
                        if(book.getStock().getQuantity() > 0)
                        {
                            lineItem.setQuantity(quantity);
                            lineItemService.merge(lineItem);
                        }
                    }
                }
            }
        }
        return "cart";
    }

    @GetMapping("/coupon-process")
    public String processCoupon(@RequestParam("coupon") String couponId, Model model, HttpSession session) {
        Discount discount = discountService.findById(couponId);
        if(discount == null) {
            model.addAttribute("errorDiscount", "DISCOUNT CODE IS WRONG");
            return "cart";
        }
        User user = (User) session.getAttribute("user");
        if(discount.getUsers().contains(user))
        {
            model.addAttribute("errorDiscount", "DISCOUNT CODE IS USED");
            return "cart";
        }
        if(!user.getDiscounts().contains(discount))
        {
            model.addAttribute("errorDiscount", "YOU DON'T HAVE PERMIT TO USE THIS DISCOUNT");
            return "cart";
        }
        Cart cart = (Cart) session.getAttribute("cart");
        cart.setDiscount(discount.getDiscountAmount());
        cartService.merge(cart);
        discount.addUser(user);
        discountService.merge(discount);
        model.addAttribute("successDiscount", "DISCOUNT APPLIED !");
        return "cart";
    }



}
