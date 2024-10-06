package com.ecom.Controller;

import com.ecom.Utility.ResetPasswordUrlGenerator;
import com.ecom.model.*;
import com.ecom.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	UserDtlService userDtlService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	EmailService emailService;

	@Autowired
	private CartService cartService;


	@ModelAttribute
	public void getUserDetails(Model model, Authentication principal){
		if(principal != null){

			UserDtl userDtl=userDtlService.getUserDtlByEmail(principal.getName());
			model.addAttribute("userDtl", userDtl);
			Integer noOfCartItems=cartService.countCartByUserId(userDtl.getId());
			model.addAttribute("noOfCartItems", noOfCartItems);
		}
	}

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/home")
	public String testingController() {
		return "index";
	}

	@GetMapping("/signIn")
	public String signIn() {
		return "login";
	}

//	@GetMapping("/login")
//	public String Login() {
//		return "login";
//	}
	@GetMapping("/register")
	public String Register() {
		return "register";
	}
	
	@GetMapping("/product")
	public String productDetails(@RequestParam("category") String category, Model model) {

		List<Category> categoryList= categoryService.getAllActiveCategory();
		List<Product> productList;
		if(ObjectUtils.isEmpty(category)){
			productList=productService.getIsActiveProducts();
		}
		else{
			productList=productService.getProductsByCategory(category);
		}

		model.addAttribute("category", categoryList);
		model.addAttribute("product", productList);
		return "product";
	}
	
	@ResponseBody
	@PostMapping("/getDetails")
	public String GetDetails(HttpServletRequest request) {
			System.out.println(request.getParameter("password"));
			return " its working";
	}

	@GetMapping("/view_product/{id}")
	public String viewProducts(@PathVariable("id") int id, Model model) {
		if(id==0){

		}
		Product product=productService.getProductById(id);
		if(ObjectUtils.isEmpty(product)){
			model.addAttribute("error", "Product not found");
			return "error";
		}
		model.addAttribute("product", product);
		return "view_products";
	}

	@PostMapping("/save_user")
	public String saveUser(@Valid @ModelAttribute UserDtl userDtl, BindingResult error, Model model, @RequestParam("file") MultipartFile profileImage) {
		if(error.hasErrors()) {
			model.addAttribute("errors", error.getAllErrors());
			for(FieldError fieldError:error.getFieldErrors()){
				System.out.println(fieldError.getField()+"=="+fieldError.getDefaultMessage());
			}
			return "register";

		}
		if(userDtlService.getUserDtlByEmail(userDtl.getEmail())!=null){
			model.addAttribute("error", "This email already exists");
			return "register";
		}
		model.addAttribute("success","Successfully Register :) ");
		userDtl.setRole("ROLE_USER");
		userDtl.setPassword(passwordEncoder.encode(userDtl.getPassword()));
		userDtlService.saveUserDtl(userDtl);
		return "loginTemp";
	}

	@ResponseBody
	@PostMapping("/test")
	public String test(@Valid @ModelAttribute test t,BindingResult error) {
		if(error.hasErrors()){
			System.out.println(error.getAllErrors());
		}

		return t.getName();
	}
	@GetMapping("/gototest")
	public String gotToTest() {
		return "testHtml";
	}

	@GetMapping("/forget_password")
	public String forgetPassword() {
		return "forgetPassword";
	}

	@PostMapping("/forget_password")
	public String forgetPassword(@RequestParam("email") String email, Model model) throws Exception{
		if(ObjectUtils.isEmpty(email)){
			model.addAttribute("error", "Please enter email");
			return "forgetPassword";
		}

		else if(!userDtlService.emailExist(email)){
			model.addAttribute("error", "Enter valid email");
			return "forgetPassword";
		}
		String generatedToken=UUID.randomUUID().toString();
		UserDtl userDtl=userDtlService.getUserDtlByEmail(email);
		userDtl.setToken(generatedToken);
		userDtlService.saveUserDtl(userDtl);
		String body = "<p>Hello</p> "
				+ "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>"
				+ "<a href=\"" + ResetPasswordUrlGenerator.generateResetPasswordUrl() + generatedToken + "\">Reset Password</a>";

		emailService.sendEmail(email,"reset your password" ,body);
		model.addAttribute("success","Reset password page link sent to your mail");
		return "forgetPassword";

	}
	@GetMapping("/reset_password")
	public String resetPassword(@RequestParam("token") String token, Model model) {
		if(ObjectUtils.isEmpty(token)){
			model.addAttribute("error", "Internal server error");
			return "forgetPassword";
		}
		UserDtl useDtl= userDtlService.getUserDtlByToken(token);
		if(useDtl==null){
			return "forgetPassword";
		}
		return "resetPassword";


	}

	@GetMapping("/view_product/addToCart")
	public String addToCart(@RequestParam("pid")int pid, @RequestParam("uid") int uid ,RedirectAttributes model){
		if(pid==0 || uid==0){

		}
		Cart cart=cartService.saveCart(uid,pid);
		if(cart==null){
			model.addFlashAttribute("error", "Cart Not Added Successfully");
		}
		else{
			model.addFlashAttribute("success", "Cart added successfully");
		}

		return "redirect:/view_product/"+pid;

	}





	
}
