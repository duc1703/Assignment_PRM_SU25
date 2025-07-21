package com.example.assignment_prm_su25.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.assignment_prm_su25.model.User;
import com.example.assignment_prm_su25.model.Category;
import com.example.assignment_prm_su25.model.Product;
import com.example.assignment_prm_su25.model.ProductVariant;
import com.example.assignment_prm_su25.model.Cart;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    // Brand table
    public static final String TABLE_BRAND = "brand";
    public static final String COLUMN_BRAND_ID = "id";
    public static final String COLUMN_BRAND_NAME = "name";
    public static final String COLUMN_BRAND_DESCRIPTION = "description";

    private static final String CREATE_TABLE_BRAND =
            "CREATE TABLE " + TABLE_BRAND + " (" +
                    COLUMN_BRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BRAND_NAME + " TEXT, " +
                    COLUMN_BRAND_DESCRIPTION + " TEXT)";
    private static final String DATABASE_NAME = "sneaker_shop.db";
    private static final int DATABASE_VERSION = 31; // Incremented version to ensure cart table is created

    private static UserDatabaseHelper instance;

    public static synchronized UserDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new UserDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";

    public static final String TABLE_CATEGORY = "category";
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_DESCRIPTION = "description";

    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_IMAGE = "image";
    public static final String COLUMN_PRODUCT_CATEGORY_ID = "categoryId";
    public static final String COLUMN_PRODUCT_RATE = "rate";

    public static final String TABLE_PRODUCT_VARIANT = "product_variant";
    public static final String COLUMN_VARIANT_ID = "id";
    public static final String COLUMN_VARIANT_PRODUCT_ID = "productId";
    public static final String COLUMN_VARIANT_COLOR = "color";
    public static final String COLUMN_VARIANT_SIZE = "size";
    public static final String COLUMN_VARIANT_STOCK = "stock";

    public static final String TABLE_CART = "cart";
    public static final String COLUMN_CART_ID = "id";
    public static final String COLUMN_CART_USER_ID = "user_id";
    public static final String COLUMN_CART_PRODUCT_ID = "product_id";
    public static final String COLUMN_CART_QUANTITY = "quantity";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ROLE + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT)";

    private static final String CREATE_TABLE_CATEGORY =
            "CREATE TABLE " + TABLE_CATEGORY + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT, " +
                    COLUMN_CATEGORY_DESCRIPTION + " TEXT)";

    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE " + TABLE_PRODUCT + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT, " +
                    COLUMN_PRODUCT_DESCRIPTION + " TEXT, " +
                    COLUMN_PRODUCT_PRICE + " REAL, " +
                    COLUMN_PRODUCT_IMAGE + " TEXT, " +
                    COLUMN_PRODUCT_CATEGORY_ID + " INTEGER, " +
                    COLUMN_PRODUCT_RATE + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_PRODUCT_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + ")" +
                    ")";

    private static final String CREATE_TABLE_PRODUCT_VARIANT =
            "CREATE TABLE " + TABLE_PRODUCT_VARIANT + " (" +
                    COLUMN_VARIANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VARIANT_PRODUCT_ID + " INTEGER, " +
                    COLUMN_VARIANT_COLOR + " TEXT, " +
                    COLUMN_VARIANT_SIZE + " TEXT, " +
                    COLUMN_VARIANT_STOCK + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_VARIANT_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ")" +
                    ")";

    private static final String CREATE_TABLE_CART =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CART_USER_ID + " INTEGER, " +
                    COLUMN_CART_PRODUCT_ID + " INTEGER, " +
                    COLUMN_CART_QUANTITY + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_CART_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ")" +
                    ")";

    private UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_PRODUCT_VARIANT);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_BRAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_VARIANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Thêm user mới (đăng ký)
    public boolean addUser(User user) {
        // First, check if the user already exists to provide a clear error message.
        if (getUserByEmail(user.getEmail()) != null) {
            return false; // User already exists
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ROLE, user.getRole());
        values.put(COLUMN_PHONE, user.getPhone() != null ? user.getPhone() : "");
        values.put(COLUMN_ADDRESS, user.getAddress() != null ? user.getAddress() : "");
        // The UNIQUE constraint on the email column will enforce uniqueness at the DB level.
        long result = db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1;
    }

    // Lấy user theo email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        try (Cursor cursor = db.query(TABLE_USER, null, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
            }
        }
        return user;
    }

    // Kiểm tra đăng nhập (email/username + password)
    public User checkUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        try (Cursor cursor = db.query(TABLE_USER, null, COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
            }
        }
        return user;
    }

    // Cập nhật thông tin user
    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ROLE, user.getRole());
        values.put(COLUMN_PHONE, user.getPhone() != null ? user.getPhone() : "");
        values.put(COLUMN_ADDRESS, user.getAddress() != null ? user.getAddress() : "");
        
        int result = db.update(TABLE_USER, values, COLUMN_ID + "=?", new String[]{String.valueOf(user.getId())});
        return result > 0;
    }

    // Đổi mật khẩu
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        int rows = db.update(TABLE_USER, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();
        return rows > 0;
    }

    // CRUD cho Category
    public boolean addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_DESCRIPTION, category.getDescription());
        long result = db.insert(TABLE_CATEGORY, null, values);
        db.close();
        return result != -1;
    }

    public java.util.List<Category> getAllCategories() {
        java.util.List<Category> categoryList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)));
                category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_DESCRIPTION)));
                categoryList.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return categoryList;
    }

    public boolean updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_DESCRIPTION, category.getDescription());
        int rows = db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(category.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rows > 0;
    }

    // CRUD cho Product
    public boolean addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_IMAGE, product.getImageUrl());
        values.put(COLUMN_PRODUCT_CATEGORY_ID, product.getCategoryId());
        values.put(COLUMN_PRODUCT_RATE, product.getRating());
        long result = db.insert(TABLE_PRODUCT, null, values);
        db.close();
        return result != -1;
    }

    public java.util.List<Product> getAllProducts() {
        java.util.List<Product> productList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE)));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY_ID)));
                product.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_RATE)));
                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return productList;
    }

    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Product product = null;
        try (Cursor cursor = db.query(TABLE_PRODUCT, null, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE)));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CATEGORY_ID)));
                product.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_RATE)));
            }
        }
        return product;
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_IMAGE, product.getImageUrl());
        values.put(COLUMN_PRODUCT_CATEGORY_ID, product.getCategoryId());
        values.put(COLUMN_PRODUCT_RATE, product.getRating());
        int rows = db.update(TABLE_PRODUCT, values, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(product.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PRODUCT, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
        db.close();
        return rows > 0;
    }

    // CRUD cho ProductVariant
    public boolean addProductVariant(ProductVariant variant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VARIANT_PRODUCT_ID, variant.getProductId());
        values.put(COLUMN_VARIANT_COLOR, variant.getColor());
        values.put(COLUMN_VARIANT_SIZE, variant.getSize());
        values.put(COLUMN_VARIANT_STOCK, variant.getStock());
        long result = db.insert(TABLE_PRODUCT_VARIANT, null, values);
        db.close();
        return result != -1;
    }

    public java.util.List<ProductVariant> getProductVariantsByProductId(int productId) {
        java.util.List<ProductVariant> variantList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT_VARIANT, null, COLUMN_VARIANT_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ProductVariant variant = new ProductVariant();
                variant.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VARIANT_ID)));
                variant.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VARIANT_PRODUCT_ID)));
                variant.setColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VARIANT_COLOR)));
                variant.setSize(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VARIANT_SIZE)));
                variant.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VARIANT_STOCK)));
                variantList.add(variant);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return variantList;
    }

    public boolean updateProductVariant(ProductVariant variant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VARIANT_COLOR, variant.getColor());
        values.put(COLUMN_VARIANT_SIZE, variant.getSize());
        values.put(COLUMN_VARIANT_STOCK, variant.getStock());
        int rows = db.update(TABLE_PRODUCT_VARIANT, values, COLUMN_VARIANT_ID + "=?", new String[]{String.valueOf(variant.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteProductVariant(int variantId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PRODUCT_VARIANT, COLUMN_VARIANT_ID + "=?", new String[]{String.valueOf(variantId)});
        db.close();
        return rows > 0;
    }

    // CRUD cho Cart
    public boolean addToCart(int userId, int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_USER_ID, userId);
        values.put(COLUMN_CART_PRODUCT_ID, productId);
        values.put(COLUMN_CART_QUANTITY, quantity);
        long result = db.insert(TABLE_CART, null, values);
        db.close();
        return result != -1;
    }

    public java.util.List<Cart> getCartItems(int userId) {
        java.util.List<Cart> cartList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, null, COLUMN_CART_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID)));
                cart.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_USER_ID)));
                cart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_PRODUCT_ID)));
                cart.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY)));
                cartList.add(cart);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return cartList;
    }

    public boolean updateCartItem(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, cart.getQuantity());
        int rows = db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cart.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        db.close();
        return rows > 0;
    }

    // Get total cart item count for a user
    public int getCartItemCount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        try (Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_CART_QUANTITY + ") FROM " + TABLE_CART + " WHERE " + COLUMN_CART_USER_ID + "=?", new String[]{String.valueOf(userId)})) {
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        }
        return count;
    }
}