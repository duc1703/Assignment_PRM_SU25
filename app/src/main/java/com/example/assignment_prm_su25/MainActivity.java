package com.example.assignment_prm_su25;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.assignment_prm_su25.model.Product;
import com.example.assignment_prm_su25.ui.CartActivity;
import com.example.assignment_prm_su25.Adapter.ImageSliderAdapter;
import com.example.assignment_prm_su25.ui.NotificationActivity;
import com.example.assignment_prm_su25.Adapter.ProductAdapter;
import com.example.assignment_prm_su25.ui.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.button.MaterialButton;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> filteredProductList;
    private UserDatabaseHelper dbHelper;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ChipGroup chipGroup;
    private SearchView searchView;
    private TextView cartBadge;
    private int userId;
    
    // Filter components
    private AutoCompleteTextView brandFilter;
    private AutoCompleteTextView priceFilter;
    private MaterialButton btnSortBy;
    private MaterialButton btnClearFilters;
    
    // Filter state
    private String selectedBrand = "";
    private String selectedPriceRange = "";
    private String currentSortOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvProducts = findViewById(R.id.rvProducts);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        dbHelper = UserDatabaseHelper.getInstance(this);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        chipGroup = findViewById(R.id.chipGroup);
        searchView = findViewById(R.id.searchView);
        
        // Initialize filter components
        brandFilter = findViewById(R.id.brandFilter);
        priceFilter = findViewById(R.id.priceFilter);
        btnSortBy = findViewById(R.id.btnSortBy);
        btnClearFilters = findViewById(R.id.btnClearFilters);

        // Get user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        // Initialize product list and adapter
        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, filteredProductList);

        // Set up RecyclerView
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);

        // Populate product list with sample data
        populateProducts();
        setupViewPager();
        setupCategoryChips();
        setupSearchView();
        setupFilters();

        // Set up BottomNavigationView listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    showToast("Home selected");
                    return true;
                } else if (itemId == R.id.nav_search) {
                    showToast("Search selected");
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(MainActivity.this, com.example.assignment_prm_su25.ui.ProductDetailActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Product product) {
                if (userId != -1) {
                    dbHelper.addToCart(userId, product.getId(), 1);
                    showToast("Added to cart");
                    updateCartBadge();
                } else {
                    showToast("Please login first");
                }
            }

            @Override
            public void onDeleteClick(Product product) {
                // Handle delete click
            }
        });
    }

    private void setupViewPager() {
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://static.nike.com/a/images/f_auto/dpr_1.0,cs_srgb/w_1423,c_limit/a8b47c4e-98d4-4c8d-9b3c-957b0c6c0a7e/nike-just-do-it.jpg");
        imageUrls.add("https://brand.assets.adidas.com/image/upload/f_auto,q_auto,fl_lossy/if_w_gt_1920,w_1920/if_w_gt_1920,w_1920/enUS/Images/adidas-3-stripes-brand-banner_tcm221-539349.jpg");
        imageUrls.add("https://images.vans.com/is/image/Vans/VN0A4U3B18L-HERO?$583x583$");
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(this, imageUrls);
        viewPager.setAdapter(sliderAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();
    }

    private void setupCategoryChips() {
        String[] categories = {"Tất cả", "Giày Nam", "Giày Nữ", "Giày Trẻ em", "Thương hiệu"};
        for (String category : categories) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            chipGroup.addView(chip);
        }
    }

    private void populateProducts() {
        if (dbHelper.getAllCategories().isEmpty()) {
            dbHelper.addCategory(new Category("Giày thể thao", "Giày thể thao các loại"));
            dbHelper.addCategory(new Category("Giày chạy bộ", "Giày chuyên dụng cho chạy bộ"));
            dbHelper.addCategory(new Category("Sneaker", "Giày sneaker thời trang"));
            dbHelper.addCategory(new Category("Lifestyle", "Giày phong cách sống"));
            dbHelper.addCategory(new Category("Bóng rổ", "Giày bóng rổ"));
            dbHelper.addCategory(new Category("Tennis", "Giày tennis"));
            Log.d("MainActivity", "Added sample categories to DB.");
        }
        if (dbHelper.getAllProducts().isEmpty()) { // Chỉ thêm nếu DB đang trống
            Log.d("MainActivity", "Adding sample products to DB...");
            dbHelper.addProduct(new Product(1, "Nike Air Max 270", "Giày thể thao nam thoải mái, phong cách trẻ trung", 2899000, "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/awjogtdnqxniqqk0wpgf/air-max-270-mens-shoes-KkLcGR.png", 4.8f, 1));
            dbHelper.addProduct(new Product(2, "Adidas Ultraboost 22", "Giày chạy bộ nữ với công nghệ Boost", 3299000, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbaf991a78bc4896a3e9ad7800abcec6_9366/Ultraboost_22_Shoes_Black_GZ0127_01_standard.jpg", 4.9f, 1));
            dbHelper.addProduct(new Product(3, "Converse Chuck Taylor All Star", "Giày sneaker cổ điển, phù hợp mọi lứa tuổi", 1599000, "https://www.converse.com/dw/image/v2/BCZC_PRD/on/demandware.static/-/Sites-cnv-master-catalog/default/dw2f8b4f0d/images/a_107/M7650_A_107X1.jpg", 4.6f, 1));
            dbHelper.addProduct(new Product(4, "Vans Old Skool", "Giày skateboard trẻ trung, năng động", 1899000, "https://images.vans.com/is/image/Vans/D3HY28-HERO?$583x583$", 4.7f, 1));
            dbHelper.addProduct(new Product(5, "Puma RS-X", "Giày thể thao retro với thiết kế độc đáo", 2199000, "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/374393/01/sv01/fnd/PNA/fmt/png/RS-X-Reinvention-Sneakers", 4.5f, 1));
            dbHelper.addProduct(new Product(6, "New Balance 574", "Giày lifestyle thoải mái cho mọi hoạt động", 2499000, "https://nb.scene7.com/is/image/NB/ml574evg_nb_02_i?$dw_detail_main_lg$&bgc=f1f1f1&layer=1&bgcolor=f1f1f1&blendMode=mult&scale=10&wid=1600&hei=1600", 4.4f, 1));
            dbHelper.addProduct(new Product(7, "Jordan Air Jordan 1", "Giày bóng rổ kinh điển với thiết kế iconic", 3599000, "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/b7d9211c-26e7-431a-ac24-b0540fb3c00f/air-jordan-1-retro-high-og-shoes-Prsm5V.png", 4.9f, 1));
            dbHelper.addProduct(new Product(8, "Adidas Stan Smith", "Giày tennis cổ điển, phong cách tối giản", 1899000, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/ee8b7b1d15b54d4b9b4aab4500f8b5e0_9366/Stan_Smith_Shoes_White_M20324_01_standard.jpg", 4.7f, 1));
            Log.d("MainActivity", "Sample products added to DB.");
        }
        productList.clear();
        productList.addAll(dbHelper.getAllProducts()); // Lấy TẤT CẢ sản phẩm từ DB
        Log.d("MainActivity", "Loaded " + productList.size() + " products from DB.");

        filteredProductList.clear();
        filteredProductList.addAll(productList);
        productAdapter.notifyDataSetChanged();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });
    }

    private void filterProducts(String query) {
        List<Product> searchResults = new ArrayList<>();
        
        if (query.isEmpty()) {
            searchResults.addAll(productList);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(lowerCaseQuery) ||
                    product.getDescription().toLowerCase().contains(lowerCaseQuery)
                    ) {
                    searchResults.add(product);
                }
            }
        }
        
        // Apply additional filters to search results
        filteredProductList.clear();
        for (Product product : searchResults) {
            boolean matchesBrand = selectedBrand.isEmpty() ;
            boolean matchesPrice = selectedPriceRange.isEmpty() || matchesPriceRange(product, selectedPriceRange);
            
            if (matchesBrand && matchesPrice) {
                filteredProductList.add(product);
            }
        }
        
        // Apply sorting
        if (!currentSortOption.isEmpty()) {
            sortProducts(filteredProductList, currentSortOption);
        }
        
        productAdapter.notifyDataSetChanged();
        
        if (filteredProductList.isEmpty() && !query.isEmpty()) {
            showToast("Không tìm thấy sản phẩm nào");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        // Setup cart badge
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        View actionView = cartItem.getActionView();
        if (actionView != null) {
            cartBadge = actionView.findViewById(R.id.cart_badge);
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                }
            });
            updateCartBadge();
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else if (itemId == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCartBadge() {
        if (cartBadge != null && userId != -1) {
            int count = dbHelper.getCartItemCount(userId);
            if (count > 0) {
                cartBadge.setText(String.valueOf(count));
                cartBadge.setVisibility(View.VISIBLE);
            } else {
                cartBadge.setVisibility(View.GONE);
            }
        }
    }

    private void logout() {
        // Clear user session
        SharedPreferences prefs = getSharedPreferences("LoginSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        
        // Redirect to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    private void setupFilters() {
        setupPriceFilter();
        setupSortButton();
        setupClearFiltersButton();
    }
    

    
    private void setupPriceFilter() {
        String[] priceRanges = {
            getString(R.string.all_prices),
            getString(R.string.price_under_500k),
            getString(R.string.price_500k_1m),
            getString(R.string.price_1m_2m),
            getString(R.string.price_above_2m)
        };
        
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_dropdown_item_1line, priceRanges);
        priceFilter.setAdapter(priceAdapter);
        
        priceFilter.setOnItemClickListener((parent, view, position, id) -> {
            selectedPriceRange = priceRanges[position];
            if (selectedPriceRange.equals(getString(R.string.all_prices))) {
                selectedPriceRange = "";
            }
            applyFilters();
        });
    }
    
    private void setupSortButton() {
        btnSortBy.setOnClickListener(v -> showSortDialog());
    }
    
    private void setupClearFiltersButton() {
        btnClearFilters.setOnClickListener(v -> clearAllFilters());
    }
    
    private void showSortDialog() {
        String[] sortOptions = {
            getString(R.string.sort_name_asc),
            getString(R.string.sort_name_desc),
            getString(R.string.sort_price_asc),
            getString(R.string.sort_price_desc),
            getString(R.string.sort_newest),
            getString(R.string.sort_popular)
        };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.sort_by))
               .setItems(sortOptions, (dialog, which) -> {
                   currentSortOption = sortOptions[which];
                   applyFilters();
               })
               .show();
    }
    
    private void clearAllFilters() {
        selectedBrand = "";
        selectedPriceRange = "";
        currentSortOption = "";
        
        brandFilter.setText("", false);
        priceFilter.setText("", false);
        
        applyFilters();
        showToast("Đã xóa tất cả bộ lọc");
    }
    
    private void applyFilters() {
        filteredProductList.clear();
        
        // Apply brand and price filters
        for (Product product : productList) {
            boolean matchesBrand = selectedBrand.isEmpty() ;
            boolean matchesPrice = selectedPriceRange.isEmpty() || matchesPriceRange(product, selectedPriceRange);
            
            if (matchesBrand && matchesPrice) {
                filteredProductList.add(product);
            }
        }
        
        // Apply sorting
        if (!currentSortOption.isEmpty()) {
            sortProducts(filteredProductList, currentSortOption);
        }
        
        productAdapter.notifyDataSetChanged();
        
        if (filteredProductList.isEmpty()) {
            showToast("Không tìm thấy sản phẩm phù hợp");
        }
    }
    
    private boolean matchesPriceRange(Product product, String priceRange) {
        double price = product.getPrice();
        
        if (priceRange.equals(getString(R.string.price_under_500k))) {
            return price < 500000;
        } else if (priceRange.equals(getString(R.string.price_500k_1m))) {
            return price >= 500000 && price < 1000000;
        } else if (priceRange.equals(getString(R.string.price_1m_2m))) {
            return price >= 1000000 && price < 2000000;
        } else if (priceRange.equals(getString(R.string.price_above_2m))) {
            return price >= 2000000;
        }
        
        return true;
    }
    
    private void sortProducts(List<Product> products, String sortOption) {
        if (sortOption.equals(getString(R.string.sort_name_asc))) {
            Collections.sort(products, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
        } else if (sortOption.equals(getString(R.string.sort_name_desc))) {
            Collections.sort(products, (p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
        } else if (sortOption.equals(getString(R.string.sort_price_asc))) {
            Collections.sort(products, (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
        } else if (sortOption.equals(getString(R.string.sort_price_desc))) {
            Collections.sort(products, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        } else if (sortOption.equals(getString(R.string.sort_newest))) {
            Collections.sort(products, (p1, p2) -> Integer.compare(p2.getId(), p1.getId()));
        } else if (sortOption.equals(getString(R.string.sort_popular))) {
            Collections.sort(products, (p1, p2) -> Float.compare(p2.getRating(), p1.getRating()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }
}