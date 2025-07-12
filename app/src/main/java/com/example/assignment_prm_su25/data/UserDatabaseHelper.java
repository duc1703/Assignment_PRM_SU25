package com.example.assignment_prm_su25.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.assignment_prm_su25.model.User;
import com.example.assignment_prm_su25.model.Product;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sneaker_shop.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";

    // Product table constants
    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_IMAGE_URL = "image_url";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ROLE + " TEXT)";

    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE " + TABLE_PRODUCT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_BRAND + " TEXT, " +
                    COLUMN_SIZE + " TEXT, " +
                    COLUMN_COLOR + " TEXT, " +
                    COLUMN_STOCK + " INTEGER, " +
                    COLUMN_IMAGE_URL + " TEXT)";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);
    }

    // Thêm user mới (đăng ký)
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ROLE, user.getRole());
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    // Lấy user theo email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
            cursor.close();
        }
        db.close();
        return user;
    }

    // Kiểm tra đăng nhập (email/username + password)
    public User checkUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password}, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
            cursor.close();
        }
        db.close();
        return user;
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

    // ========== PRODUCT CRUD OPERATIONS ==========

    // Thêm sản phẩm mới
    public boolean addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_BRAND, product.getBrand());
        values.put(COLUMN_SIZE, product.getSize());
        values.put(COLUMN_COLOR, product.getColor());
        values.put(COLUMN_STOCK, product.getStock());
        values.put(COLUMN_IMAGE_URL, product.getImageUrl());
        long result = db.insert(TABLE_PRODUCT, null, values);
        db.close();
        return result != -1;
    }

    // Lấy tất cả sản phẩm
    public java.util.List<Product> getAllProducts() {
        java.util.List<Product> productList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, COLUMN_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
                product.setSize(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)));
                product.setColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)));
                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return productList;
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
            product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
            product.setSize(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)));
            product.setColor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR)));
            product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
            product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)));
            cursor.close();
        }
        db.close();
        return product;
    }

    // Cập nhật sản phẩm
    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_BRAND, product.getBrand());
        values.put(COLUMN_SIZE, product.getSize());
        values.put(COLUMN_COLOR, product.getColor());
        values.put(COLUMN_STOCK, product.getStock());
        values.put(COLUMN_IMAGE_URL, product.getImageUrl());
        int rows = db.update(TABLE_PRODUCT, values, COLUMN_ID + "=?", new String[]{String.valueOf(product.getId())});
        db.close();
        return rows > 0;
    }

    // Xóa sản phẩm
    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PRODUCT, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }
} 