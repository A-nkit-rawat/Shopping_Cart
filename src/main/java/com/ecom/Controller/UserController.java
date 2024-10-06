package com.ecom.Controller;

import com.ecom.Utility.OrderStatus;
import com.ecom.model.Cart;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtl;
import com.ecom.service.CartService;
import com.ecom.service.ProductOrderService;
import com.ecom.service.ProductService;
import com.ecom.service.impl.UserDtlServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    UserDtlServiceImp userDtlService;
    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductOrderService productOrderService;

    @ModelAttribute
    public void getUserDetails(Model model, Principal principal) {
        System.out.println(principal.getName());
        System.out.println("+++++++++++++++++++");
        if(principal != null){
            UserDtl userDtl=userDtlService.getUserDtlByEmail(principal.getName());
            model.addAttribute("userDtl", userDtl);
            Integer noOfCartItems=cartService.countCartByUserId(userDtl.getId());
            model.addAttribute("noOfCartItems", noOfCartItems);
        }
    }

    @GetMapping("/")
    @ResponseBody
    public String userHome(){
        return "welcome to user Page";
    }

    @GetMapping("/cart")
    public String loadCartPage(Model model, Principal principal){
        UserDtl currentUser= userDtlService.getUserDtlByEmail(principal.getName());
        List<Cart> listOfCarts=cartService.getCartByUserId(currentUser.getId());
        if(!ObjectUtils.isEmpty(listOfCarts)){
            long totalPrice=0;
            for(Cart cart:listOfCarts){
                totalPrice=totalPrice+cart.getQuantity()*cart.getProduct().getDiscountedPrice().longValue();
            }
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("listOfCarts", listOfCarts);
        }
        else{
            model.addAttribute("totalPrice",0);
            model.addAttribute("listOfCarts", null);
        }
        return "User/Cart";
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateQuantity(@RequestParam("sy") String flag, @RequestParam("cid") Integer cid, Model model ){

        if(ObjectUtils.isEmpty(cid) || ObjectUtils.isEmpty(flag)){
            model.addAttribute("error","Modification is done in query parameters :( ");
            return "error";
        }
        Cart oldCart=cartService.getCartById(cid);
        if(flag.equals("dec")){
            oldCart.setQuantity(oldCart.getQuantity()-1);
            if(oldCart.getQuantity()<1){
                System.out.println("deleting the cart");
                cartService.deleteCart(oldCart);
            }
            else{
                cartService.saveCart(oldCart);
            }
        }
        else if(flag.equals("inc")){
               oldCart.setQuantity(oldCart.getQuantity()+1);
               cartService.saveCart(oldCart);
        }

        return "redirect:/user/cart";

    }

    @GetMapping("/order")
    public String viewOrder(Model model,Principal principal) {

        UserDtl user= userDtlService.getUserDtlByEmail(principal.getName());

        List<Cart> listOfCartItem=cartService.getCartByUserId(user.getId());

        String []fullName=user.getName().split(" ");
        String firstName="shopping";
        String lastName="Cart";
        if(fullName.length>0){
            firstName=fullName[0];
        }
        if(fullName.length>1){
            lastName=fullName[fullName.length-1];
        }
        model.addAttribute("defaultUserFirstName",firstName);
        model.addAttribute("defaultUserLastName",lastName);
        model.addAttribute("defaultUser",user);

        long totalPrice=0;
        for(Cart cart: listOfCartItem){

            totalPrice=totalPrice+(cart.getQuantity()*cart.getProduct().getDiscountedPrice().longValue());
        }
        model.addAttribute("orderPrice",totalPrice);
        //tax and deliver charges in 0 at this time in future we will change it;
        model.addAttribute("totalOrderPrice",totalPrice);

        return "User/Order";
    }

    @PostMapping("/save-order")
    public String save_order(@ModelAttribute OrderRequest orderRequest, Principal principal){
        UserDtl user=userDtlService.getUserDtlByEmail(principal.getName());
        productOrderService.saveProductOrder(user.getId(),orderRequest);

        return "User/Success";
    }

    @GetMapping("view_orders")
    public String view_orders(Principal principal, Model model){
        UserDtl user= userDtlService.getUserDtlByEmail(principal.getName());
        List<ProductOrder> listOfProductOrders= productOrderService.getProductOrdersByUserId(user.getId());
        model.addAttribute("orders",listOfProductOrders);
        return "User/viewOrder";
    }

    @GetMapping("/update-status")
    public String updateStatusOfProductOrder(@RequestParam("id") Integer productOrderId,@RequestParam("st") Integer statusCode,Model model){
        if(ObjectUtils.isEmpty(productOrderId)){
            model.addAttribute("error","invalid api call");
            return "error";
        }

        ProductOrder productOrder=productOrderService.getProductOrderByProductOrderId(productOrderId);
        OrderStatus[] orderStatuses= OrderStatus.values();
        for(OrderStatus orderStatus:orderStatuses){
            if(orderStatus.getId()==statusCode){
                productOrder.setStatus(orderStatus.getCurrentStatus());
            }
        }
        productOrderService.updateProductOrder(productOrder);
        return "forward:/view_order";
    }





}
