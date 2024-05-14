package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.LineItem;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CartService;
import com.example.bookstore.service.LineItemService;
import com.example.bookstore.service.StockService;
import com.example.bookstore.util.GenerateID;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/cart")
public class CartController {


    private final GenerateID generateID;
    private BookService bookService;

    private CartService cartService;

    private LineItemService lineItemService;


    @Autowired
    public CartController(BookService bookService, CartService cartService, LineItemService lineItemService, GenerateID generateID) {
        this.bookService = bookService;
        this.cartService = cartService;
        this.lineItemService = lineItemService;
        this.generateID = generateID;
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
            double totalPrice = 0;
            for(LineItem lineItem : cart.getLineItemList())
            {
                totalPrice += lineItem.getQuantity()*lineItem.getBook().getSellPrice();
            }
            session.setAttribute("totalPrice", totalPrice);
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
                        lineItem.setQuantity(quantity);
                        lineItemService.merge(lineItem);
                    }
                }
            }
        }
        return "cart";
    }

}
