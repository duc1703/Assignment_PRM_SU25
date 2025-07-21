package com.example.assignment_prm_su25;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.assignment_prm_su25.model.Product;
import com.example.assignment_prm_su25.ui.CartActivity;
import com.example.assignment_prm_su25.ui.ImageSliderAdapter;
import com.example.assignment_prm_su25.ui.NotificationActivity;
import com.example.assignment_prm_su25.ui.ProductAdapter;
import com.example.assignment_prm_su25.ui.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

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
                // Handle item click
            }

            @Override
            public void onAddToCartClick(Product product) {
                dbHelper.addToCart(1, product.getId(), 1); // Replace with actual user ID
                showToast("Added to cart");
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
        // Add sample shoe products
        productList.add(new Product(1, "Nike Air Max 270", "Giày thể thao nam thoải mái, phong cách trẻ trung", 2899000, "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/awjogtdnqxniqqk0wpgf/air-max-270-mens-shoes-KkLcGR.png", 4.8f, 1));
        productList.add(new Product(2, "Adidas Ultraboost 22", "Giày chạy bộ nữ với công nghệ Boost", 3299000, "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/fbaf991a78bc4896a3e9ad7800abcec6_9366/Ultraboost_22_Shoes_Black_GZ0127_01_standard.jpg", 4.9f, 2));
        productList.add(new Product(3, "Converse Chuck Taylor All Star", "Giày sneaker cổ điển, phù hợp mọi lứa tuổi", 1599000, "https://www.converse.com/dw/image/v2/BCZC_PRD/on/demandware.static/-/Sites-cnv-master-catalog/default/dw2f8b4f0d/images/a_107/M7650_A_107X1.jpg", 4.6f, 3));
        productList.add(new Product(4, "Vans Old Skool", "Giày skateboard trẻ trung, năng động", 1899000, "https://images.vans.com/is/image/Vans/D3HY28-HERO?$583x583$", 4.7f, 1));
        productList.add(new Product(5, "Puma RS-X", "Giày thể thao retro với thiết kế độc đáo", 2199000, "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/374393/01/sv01/fnd/PNA/fmt/png/RS-X-Reinvention-Sneakers", 4.5f, 4));
        productList.add(new Product(6, "New Balance 574", "Giày lifestyle thoải mái cho mọi hoạt động", 2499000, "https://nb.scene7.com/is/image/NB/ml574evg_nb_02_i?$dw_detail_main_lg$&bgc=f1f1f1&layer=1&bgcolor=f1f1f1&blendMode=mult&scale=10&wid=1600&hei=1600", 4.4f, 1));
        
        // Initially show all products
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
        filteredProductList.clear();
        
        if (query.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(lowerCaseQuery) ||
                    product.getDescription().toLowerCase().contains(lowerCaseQuery)) {
                    filteredProductList.add(product);
                }
            }
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
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}