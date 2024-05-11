import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class Product {
    private String name;
    private double price;
    private String category;

    public Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}

class ShoppingCart {
    private Map<Product, Integer> cart;

    public ShoppingCart() {
        cart = new HashMap<>();
    }

    public void addToCart(Product product, int quantity) {
        cart.put(product, cart.getOrDefault(product, 0) + quantity);
    }

    public void removeFromCart(Product product, int quantity) {
        int currentQuantity = cart.getOrDefault(product, 0);
        if (quantity >= currentQuantity) {
            cart.remove(product);
        } else {
            cart.put(product, currentQuantity - quantity);
        }
    }

    public Map<Product, Integer> getCartItems() {
        return cart;
    }

    public void printReceipt() {
        double total = 0;
        System.out.println("Receipt:");
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = quantity * product.getPrice();
            System.out.println(product.getName() + " - $" + product.getPrice() + " x " + quantity + " = $" + itemTotal);
            total += itemTotal;
        }
        System.out.println("Total: $" + total);
    }

    public void saveCartToFile(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = folderPath + File.separator + "purchase_" + System.currentTimeMillis() + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Purchase Date: " + new Date());
            writer.println("Items:");
            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                writer.println(product.getName() + " - $" + product.getPrice() + " x " + quantity + " = $" + (product.getPrice() * quantity));
            }
            writer.println("Total: $" + calculateTotal());
            System.out.println("Cart saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving cart to file: " + e.getMessage());
        }
    }

    private double calculateTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            total += product.getPrice() * quantity;
        }
        return total;
    }
}

public class Shop {
    private List<Product> products;
    private ResourceBundle messages;
    private Scanner scanner;

    public Shop() {
        products = new ArrayList<>();
        // Initialize sample products
        products.add(new Product("Banana", 0.79, "Fruit"));
        products.add(new Product("Apple", 0.99, "Fruit"));
        products.add(new Product("Orange", 0.69, "Fruit"));
        products.add(new Product("Potato", 1.29, "Vegetable"));
        products.add(new Product("Tomato", 1.49, "Vegetable"));
        products.add(new Product("Lettuce", 1.99, "Vegetable"));
        products.add(new Product("Milk", 2.49, "Dairy"));
        products.add(new Product("Eggs", 1.99, "Dairy"));
        products.add(new Product("Cheese", 3.99, "Dairy"));
        products.add(new Product("Soda", 1.29, "Beverage"));
        products.add(new Product("Water", 0.99, "Beverage"));
        products.add(new Product("Juice", 2.49, "Beverage"));
        products.add(new Product("Chips", 1.99, "Snack"));
        products.add(new Product("Chocolate", 2.99, "Snack"));
        products.add(new Product("Cookies", 1.79, "Snack"));

        // Load messages properties file based on default locale with UTF-8 encoding
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("messages.properties")) {
            messages = new PropertyResourceBundle(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error loading messages properties file: " + e.getMessage());
        }

        scanner = new Scanner(System.in);
    }

    public void displayAvailableProducts() {
        System.out.println(messages.getString("menu.option.displayProducts"));
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.getName() + " - $" + product.getPrice() + " - Category: " + product.getCategory());
        }
    }

    // Getter for products
    public List<Product> getProducts() {
        return products;
    }

    // Method to check if a given index is valid for products
    public boolean isValidIndex(int index) {
        return index >= 1 && index <= products.size();
    }

    // Method to display language selection menu
    public Locale selectLanguage() {
        System.out.println("Select language:");
        System.out.println("1. English");
        System.out.println("2. Spanish");
        System.out.println("3. Latvian");
        System.out.print("Enter your choice: ");
        int choice = getValidIntegerInput();
        switch (choice) {
            case 1:
                return Locale.ENGLISH;
            case 2:
                return new Locale("es", "ES");
            case 3:
                return new Locale("lv", "LV");
            default:
                System.out.println("Invalid choice! Defaulting to English.");
                return Locale.ENGLISH;
        }
    }

    private int getValidIntegerInput() {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    public static void main(String[] args) {
        Shop shop = new Shop();
        shop.messages = ResourceBundle.getBundle("messages", shop.selectLanguage());
        ShoppingCart cart = new ShoppingCart();

        while (true) {
            System.out.println("\n-----------------------------");
            System.out.println("1. " + shop.messages.getString("menu.option.displayProducts"));
            System.out.println("2. " + shop.messages.getString("menu.option.addProduct"));
            System.out.println("3. " + shop.messages.getString("menu.option.removeProduct"));
            System.out.println("4. " + shop.messages.getString("menu.option.viewCart"));
            System.out.println("5. " + shop.messages.getString("menu.option.getReceipt"));
            System.out.println("6. " + shop.messages.getString("menu.option.exit"));
            System.out.println("-----------------------------");
            System.out.print(shop.messages.getString("menu.choicePrompt"));

            int choice = shop.getValidIntegerInput();

            switch (choice) {
                case 1:
                    shop.displayAvailableProducts();
                    break;
                case 2:
                    shop.displayAvailableProducts();
                    System.out.print(shop.messages.getString("menu.choiceProduct"));
                    int addIndex = shop.getValidIntegerInput();
                    if (shop.isValidIndex(addIndex)) {
                        Product productToAdd = shop.getProducts().get(addIndex - 1);
                        System.out.print(shop.messages.getString("menu.choiceQuantity"));
                        int quantity = shop.getValidIntegerInput();
                        cart.addToCart(productToAdd, quantity);
                        System.out.println(productToAdd.getName() + " " + shop.messages.getString("menu.option.addedToCart"));
                    } else {
                        System.out.println(shop.messages.getString("menu.invalidIndex"));
                    }
                    break;
                case 3:
                    Map<Product, Integer> cartItems = cart.getCartItems();
                    if (!cartItems.isEmpty()) {
                        System.out.println("Cart Items:");
                        int index = 1;
                        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                            Product product = entry.getKey();
                            int quantity = entry.getValue();
                            System.out.println((index++) + ". " + product.getName() + " - Quantity: " + quantity);
                        }
                        System.out.print(shop.messages.getString("menu.choiceProduct"));
                        int removeIndex = shop.getValidIntegerInput();
                        if (removeIndex >= 1 && removeIndex <= cartItems.size()) {
                            Product productToRemove = new ArrayList<>(cartItems.keySet()).get(removeIndex - 1);
                            System.out.print("Enter the quantity to remove: ");
                            int removeQuantity = shop.getValidIntegerInput();
                            cart.removeFromCart(productToRemove, removeQuantity);
                            System.out.println(productToRemove.getName() + " " + shop.messages.getString("menu.option.removedFromCart"));
                        } else {
                            System.out.println(shop.messages.getString("menu.invalidIndex"));
                        }
                    } else {
                        System.out.println(shop.messages.getString("menu.cartEmpty"));
                    }
                    break;
                case 4:
                    Map<Product, Integer> cartItemsToDisplay = cart.getCartItems();
                    if (!cartItemsToDisplay.isEmpty()) {
                        System.out.println("Cart Items:");
                        for (Map.Entry<Product, Integer> entry : cartItemsToDisplay.entrySet()) {
                            Product product = entry.getKey();
                            int quantity = entry.getValue();
                            System.out.println(product.getName() + " - $" + product.getPrice() + " x " + quantity + " = $" + (product.getPrice() * quantity));
                        }
                    } else {
                        System.out.println(shop.messages.getString("menu.cartEmpty"));
                    }
                    break;
                case 5:
                    cart.printReceipt();
                    break;
                case 6:
                    System.out.println(shop.messages.getString("menu.exiting"));
                    // Save cart to file before exiting
                    cart.saveCartToFile("purchases");
                    System.exit(0);
                    break;
                default:
                    System.out.println(shop.messages.getString("menu.invalidChoice"));
            }
        }
    }
}
