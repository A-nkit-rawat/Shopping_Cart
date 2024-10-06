package com.ecom.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import com.ecom.Utility.OrderStatus;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtl;
import com.ecom.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ecom.model.Category;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/admin")
@Controller
public class AdminController {

	final String pathToImageFolder=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\";
	final String pathToProductFolder=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\product_img\\";

	@Autowired
	CategoryService categoryService;

    @Autowired
    private ProductService productService;

	@Autowired
	private UserDtlService userDtlService;

	@Autowired
	private CartService cartService;

	@Autowired
	private ProductOrderService productOrderService;

	@ModelAttribute
	public void getUserDetails(Model model, Principal principal){
//		System.out.println(principal.getName());
		System.out.println("+++++++++++++++++++");
		if(principal != null){
			UserDtl userDtl=userDtlService.getUserDtlByEmail(principal.getName());
			model.addAttribute("userDtl", userDtl);
			Integer noOfCartItems=cartService.countCartByUserId(userDtl.getId());
			model.addAttribute("noOfCartItems", noOfCartItems);
		}
	}

    @GetMapping("/")
	public String index() {
		return "admin/index";
	}
	
	@GetMapping("/add_product")
	public String product(Model model) {
		List<Category> listOfCategory=categoryService.getAllCategory();
		model.addAttribute("category", listOfCategory);
		return "admin/add_product";
	}

	@GetMapping("/category")
	public String category(Model model, HttpServletRequest request) {
		List<Category> categoryList=categoryService.getAllCategory();
		model.addAttribute("categories",categoryList);
		return "admin/add_category";
	}

	//helper method to extract List of Categories;
	public List<Category> getCategory() {
			return categoryService.getAllCategory();
	}
	
	//saving Category 
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam("image") MultipartFile multifile, RedirectAttributes redirectAttributes) throws IOException {

		System.out.println(category);
		if(multifile!=null) {
			String fileName= multifile!=null ? 	multifile.getOriginalFilename():"default.jpg";
			category.setImageName(fileName);
		}
//		System.out.println(category.toString()+multifile.toString());

		
		if(category ==null ) {
			redirectAttributes.addFlashAttribute("Msg", "category is null");
			redirectAttributes.addFlashAttribute("error",true);
//			System.out.println("null is  error hai");
		}
		else if(ObjectUtils.isEmpty(category.getName()) || ObjectUtils.isEmpty(category.getImageName())) {
			if(ObjectUtils.isEmpty(category.getName())) {
			redirectAttributes.addFlashAttribute("Msg", "Category Name is not filled !");
				redirectAttributes.addFlashAttribute("error",true);

			}
			else {
				redirectAttributes.addFlashAttribute("Msg","Image is not uploaded ");
				redirectAttributes.addFlashAttribute("error",true);
//				System.out.println("image ki is  error hai");
				
			}
		}
		else if(categoryService.existCategory(category)) {
			redirectAttributes.addFlashAttribute("Msg","Already in existing category .Insert new name and image");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else if(categoryService.saveCategory(category)==null) {
			redirectAttributes.addFlashAttribute("Msg","Category Not Saved! internal server error ");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else {
			category.setImageName(multifile.getOriginalFilename());
			categoryService.saveCategory(category);

			String prePathForNewImage=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\category_img\\";
			File finalPathForNewImage =new File(prePathForNewImage+multifile.getOriginalFilename());
			Path path= Paths.get(finalPathForNewImage.getAbsolutePath());
			InputStream inputStream=multifile.getInputStream();
			Files.copy(multifile.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			inputStream.close();
			redirectAttributes.addFlashAttribute("Msg", "Category Added");
			redirectAttributes.addFlashAttribute("success",true);

		}
		
		return "redirect:./category";
	}
	//deletion of Category
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable("id") int id) {
		categoryService.deleteCategory(id);
		return "redirect:/admin/category";
	}
	//edit the existing Category
	@GetMapping("/editCategory/{id}")
	public String editCategory(@PathVariable("id") int id,Model model) {
		Category category = categoryService.getCategoryById(id);
//		model.addAttribute("name",category.getName());
//		model.addAttribute("image",category.getImageName());
//		model.addAttribute("isActive",category.getIsActive());
		model.addAttribute("category",category);
		return "admin/edit_Category";
	}

	@PostMapping("/saveEdit")
	public String saveEdit(@ModelAttribute Category category ,@RequestParam("image") MultipartFile multifile ) throws IOException {

		Category oldCategory=categoryService.getCategoryById(category.getId());

		category.setImageName(multifile!=null ?  multifile.getOriginalFilename():oldCategory.getImageName());

		categoryService.updateCategory(category);
		String directory=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\category_img\\"+category.getImageName();
		saveImage(multifile,directory);
		return "redirect:./category";
	}
	//Helper Function to save image to particular folder

	public void saveImage(MultipartFile file,String directory) throws IOException {
		Path path= Paths.get(directory);
		Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
	}

	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile multifile , RedirectAttributes redirectAttributes) throws IOException {
//		System.out.println(product.getName());
//		System.out.println(multifile.getOriginalFilename())		;
		if(ObjectUtils.isEmpty(product.getName())) {
			redirectAttributes.addFlashAttribute("Msg", "Product Name is not filled !");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else if(product.getIsActive()==null){
			redirectAttributes.addFlashAttribute("Msg", "Product isActive is not filled !");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else if(multifile==null){
			redirectAttributes.addFlashAttribute("Msg", "Product Image is not Uploaded !");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else if(product.getPrice()==null){
			redirectAttributes.addFlashAttribute("Msg", "Product Price is not filled !");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else if(product.getStock()==null){
			redirectAttributes.addFlashAttribute("Msg", "Stock can't be empty !");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else if(product.getCategory()==null){
			redirectAttributes.addFlashAttribute("Msg", "Choose the category");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else if(productService.exitsProductName(multifile.getOriginalFilename())){
			redirectAttributes.addFlashAttribute("Msg", "Product Name already exists !");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else if(productService.exitsProductImageName(multifile.getOriginalFilename())){
			redirectAttributes.addFlashAttribute("Msg", "Product Image already exists !");
			redirectAttributes.addFlashAttribute("error",true);
		}
		else{
			String path=pathToImageFolder+"\\product_img\\"+multifile.getOriginalFilename();
			saveImage(multifile,path);
			product.setImage(multifile.getOriginalFilename());
			product.setDiscountedPrice(product.getPrice());
			productService.saveProduct(product);
			redirectAttributes.addFlashAttribute("Msg", "Product Added Successfully :)");
			redirectAttributes.addFlashAttribute("success",true);
		}
		return "redirect:./add_product";
	}

	@GetMapping("/view_products")
	public String viewProducts(Model model) {
		List<Product> products=productService.getAllProducts();
		model.addAttribute("products",products);
		return "admin/view_products";
	}

	@GetMapping("/edit_product/{id}")
	public String editProduct(Model model,@PathVariable("id") int id) {
        Product oldProduct=productService.getProductById(id);
		model.addAttribute("product",oldProduct);

		model.addAttribute("category",getCategory());
		System.out.println(oldProduct);
		return "admin/edit_product";
	}

	@PostMapping("/edit_product/update_product/")
	public String updateProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes,@RequestParam("file") MultipartFile multipartFile) throws IOException {
		if(product.getDiscount()!=null){
			if(product.getDiscount()<0 || product.getDiscount()>100){
				redirectAttributes.addFlashAttribute("Msg", "Discount value ( 0 - 100 )");
				return "redirect:admin/edit_product/"+product.getId();
			}
		}


		if(product.getName()==null){
			product.setName("_____");
		}
		if(product.getPrice()==null){
			product.setPrice(0.00);
		}
		if(product.getStock()==null){
			product.setStock(0);
		}
		if(product.getCategory()==null){
			product.setCategory("no category");
		}
		if(product.getDiscount()==null){
			product.setDiscount(0.00);
		}

		if (product.getDescription()==null){ product.setDescription(""); }

		if(!ObjectUtils.isEmpty(multipartFile.getOriginalFilename()))
		{
//			System.out.println(multipartFile.getOriginalFilename());
//			System.out.println(product.getImage());

			//deleting the old image
			Files.deleteIfExists(Paths.get(pathToProductFolder+product.getImage()));
			//setting the new image name
			product.setImage(multipartFile.getOriginalFilename());
			//copy image to my folder structure
			Files.copy(multipartFile.getInputStream(),Paths.get(pathToProductFolder+product.getImage()),StandardCopyOption.REPLACE_EXISTING);
		}

		product.setDiscountedPrice(product.getPrice()*(100-product.getDiscount())/100);
		productService.saveProduct(product);
		redirectAttributes.addFlashAttribute("Msg","Product Updated Successfully :)");

		return "redirect:../../view_products";
	}

	@GetMapping("/delete_product/{id}")
	public String deleteProduct(@PathVariable("id") int id) throws IOException{
//		if(!productService.deleteProduct(id)){
//
//		}
		Product product=productService.deleteProduct(id);
		if(!ObjectUtils.isEmpty (product.getImage())){
			Path path=Paths.get(pathToProductFolder+product.getImage());
			Files.delete(path);
		}

		return "redirect:../view_products";
	}

	@GetMapping("/users")
	public String getUsers(Model model) {
		List<UserDtl> listOfUsers=userDtlService.getUserDtls();
		model.addAttribute("userList",listOfUsers);
		return "admin/users";
	}
	@GetMapping("users/updateUserAccountStatus/{id}")
	public String activeUser(@PathVariable("id") int id,@RequestParam("setStatus") Boolean setStatus){
		Boolean flag=userDtlService.updateUserAccountStatus(id,setStatus);
		if(flag==false){

		}

		return "redirect:../../../admin/users";
	}

	@GetMapping("/view_orders")
	public String viewOrders(Model model){
		List<ProductOrder> orders=productOrderService.getAllProductOrders();
		model.addAttribute("orders",orders);
		return "admin/view_Order";

	}

	@PostMapping("/update_order_status")
	public String updateOrderStatus(@RequestParam("st") Integer statusCode,@RequestParam("id") Integer productOrderId,Model model){
		if(ObjectUtils.isEmpty(statusCode) || ObjectUtils.isEmpty(productOrderId)){
			model.addAttribute("msg","Something went wrong :(");
		}
		ProductOrder productOrder= productOrderService.getProductOrderByProductOrderId(productOrderId);
		OrderStatus[] orderStatuses=OrderStatus.values();

		//we can optimize iteration part
		for(OrderStatus status:orderStatuses){
			if(status.getId()==statusCode){
				productOrder.setStatus(status.getCurrentStatus());
				break;
			}
		}
		productOrderService.updateProductOrder(productOrder);
		model.addAttribute("msg","Status Changed :)");
		List<ProductOrder> orders=productOrderService.getAllProductOrders();
		model.addAttribute("orders",orders);
		return "admin/view_Order";
	}



//	@GetMapping("users/disable/{id}")
//	public String disableUser(@PathVariable("id") int id){
//		System.out.println(id);
//		UserDtl modifyUser=userDtlService.getUserDtl(id);
//		modifyUser.setIsEnabled(false);
//		userDtlService.saveUserDtl(modifyUser);
//		return "redirect:../../../admin/users";
//	}

}

