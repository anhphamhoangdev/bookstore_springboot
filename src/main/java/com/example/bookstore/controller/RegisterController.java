package com.example.bookstore.controller;


import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Favorite;
import com.example.bookstore.entity.Role;
import com.example.bookstore.entity.User;
import com.example.bookstore.service.CartService;
import com.example.bookstore.service.FavoriteService;
import com.example.bookstore.service.RoleService;
import com.example.bookstore.service.UserService;
import com.example.bookstore.util.GenerateID;
import com.example.bookstore.web.RegisterUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {

    private UserService userService;
    private RoleService roleService;
    private FavoriteService favoriteService;
    private CartService cartService;
    private GenerateID generateID;

    @Autowired
    public RegisterController(UserService userService, GenerateID generateID, RoleService roleService, FavoriteService favoriteService, CartService cartService) {
        this.userService = userService;
        this.generateID = generateID;
        this.roleService = roleService;
        this.favoriteService = favoriteService;
        this.cartService = cartService;
    }

    @RequestMapping("/signup")
    public String signup(Model model){
        RegisterUser registerUser = new RegisterUser();
        model.addAttribute("registerUser", registerUser);
        return "signin-signup/signup";
    }

    @PostMapping("/process-signup")
    public String processSignup(@Valid @ModelAttribute RegisterUser registerUser,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession session)
    {
        // form validation
        if(bindingResult.hasErrors())
        {
            return "signin-signup/signup";
        }

        // check if username and email exist or not
        User user = userService.findByUsername(registerUser.getUsername());
        if(user != null)
        {
            registerUser.setUsername("");
            model.addAttribute("registerUser", registerUser);
            model.addAttribute("myError", "Username already exists.");
            return "signin-signup/signup";
        }
        user = userService.findByEmail(registerUser.getEmail());
        if(user != null)
        {
            registerUser.setEmail("");
            model.addAttribute("registerUser", registerUser);
            model.addAttribute("myError", "Email already exists.");
            return "signin-signup/signup";
        }

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        // user not exist
        user = new User();
        user.setUserID(generateID.generateUserId());
        user.setFirstName(registerUser.getFirstName());
        user.setLastName(registerUser.getLastName());
        user.setUsername(registerUser.getUsername());
        user.setPassword(bcrypt.encode(registerUser.getPassword()));
        user.setEmail(registerUser.getEmail());
        user.setGender(registerUser.getGender());
        user.setAddress(registerUser.getAddress());
        user.setBirthday(registerUser.getBirthday());
        user.setEnabled(true);
        user.setPhoneNum(registerUser.getPhoneNum());

        Role role = roleService.findByName("ROLE_USER");
        user.addRoles(role);

        // create cart + favorite
        Favorite favorite = new Favorite();
        favorite.setUserID(user.getUserID());
        favoriteService.save(favorite);

        Cart cart = new Cart();
        cart.setUserID(user.getUserID());
        cartService.save(cart);

        userService.save(user);
        return "signin-signup/confirm";
    }
}
