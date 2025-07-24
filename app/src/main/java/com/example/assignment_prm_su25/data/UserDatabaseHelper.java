
package com.example.assignment_prm_su25.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.assignment_prm_su25.model.Order;
import com.example.assignment_prm_su25.model.Rating;
import com.example.assignment_prm_su25.model.SupportMessage;
import com.example.assignment_prm_su25.model.SupportResponse;
import com.example.assignment_prm_su25.ui.OrderItem;
import com.example.assignment_prm_su25.model.User;
import com.example.assignment_prm_su25.model.Category;
import com.example.assignment_prm_su25.model.Product;
import com.example.assignment_prm_su25.model.ProductVariant;
import com.example.assignment_prm_su25.model.Cart;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    // Đảm bảo tài khoản abc@gmail.com là admin
    public void addOrUpdateAdminAbcGmail() {
        String email = "abc@gmail.com";
        User user = getUserByEmail(email);
        if (user == null) {
            // Thêm mới admin nếu chưa có
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail(email);
            admin.setPassword("admin123"); // Đặt mật khẩu mặc định, có thể thay đổi
            admin.setRole("admin");
            admin.setPhone("");
            admin.setAddress("");
            addUser(admin);
        } else if (!"admin".equals(user.getRole())) {
            // Nếu đã có nhưng chưa phải admin thì cập nhật role
            user.setRole("admin");
            updateUser(user);
        }
    }

    public void addOrUpdateStaffXyzGmail() {
        String email = "xyz@gmail.com";
        User user = getUserByEmail(email);
        if (user == null) {
            // Thêm mới staff nếu chưa có
            User staff = new User();
            staff.setName("Staff");
            staff.setEmail(email);
            staff.setPassword("staff123"); // Đặt mật khẩu mặc định
            staff.setRole("staff");
            staff.setPhone("");
            staff.setAddress("");
            addUser(staff);
        } else if (!"staff".equals(user.getRole())) {
            // Nếu đã có nhưng chưa phải staff thì cập nhật role
            user.setRole("staff");
            updateUser(user);
        }
    }
    // ...existing code...
    // Lấy toàn bộ user
    public java.util.List<User> getAllUsers() {
        java.util.List<User> userList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return userList;
    }

    // Lấy user theo id
    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.query(TABLE_USER, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)));
            cursor.close();
        }
        db.close();
        return user;
    }

    // Xóa user
    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USER, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Thêm user (nếu chưa có)
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ROLE, user.getRole());
        values.put(COLUMN_PHONE, user.getPhone() != null ? user.getPhone() : "");
        values.put(COLUMN_ADDRESS, user.getAddress() != null ? user.getAddress() : "");
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }
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
    private static final int DATABASE_VERSION = 33; // Incremented version to ensure cart table is created

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
    public static final String TABLE_ORDER = "orders"; // Renamed from 'order' to 'orders' to avoid SQL keyword conflict
    public static final String COLUMN_ORDER_ID = "id";
    public static final String COLUMN_ORDER_USER_ID = "userId";
    public static final String COLUMN_ORDER_DATE = "orderDate";
    public static final String COLUMN_ORDER_TOTAL_AMOUNT = "totalAmount";
    public static final String COLUMN_ORDER_STATUS = "status";
    public static final String COLUMN_ORDER_SHIPPING_ADDRESS = "shippingAddress";
    public static final String COLUMN_ORDER_PHONE_NUMBER = "phoneNumber";

    public static final String TABLE_ORDER_ITEM = "order_items";
    public static final String COLUMN_ORDER_ITEM_ID = "id";
    public static final String COLUMN_ORDER_ITEM_ORDER_ID = "orderId";
    public static final String COLUMN_ORDER_ITEM_PRODUCT_ID = "productId";
    public static final String COLUMN_ORDER_ITEM_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_ITEM_PRICE = "price";
    public static final String COLUMN_ORDER_ITEM_PRODUCT_NAME = "productName"; // Added for historical accuracy
    public static final String COLUMN_ORDER_ITEM_PRODUCT_IMAGE = "productImage";
    public static final String TABLE_RATING = "ratings";
    public static final String COLUMN_RATING_ID = "id";
    public static final String COLUMN_RATING_PRODUCT_ID = "productId";
    public static final String COLUMN_RATING_USER_ID = "userId";
    public static final String COLUMN_RATING_VALUE = "ratingValue";
    public static final String COLUMN_RATING_COMMENT = "reviewComment";
    public static final String COLUMN_RATING_TIMESTAMP = "timestamp";
    public static final String TABLE_SUPPORT_MESSAGE = "support_messages";
    public static final String COLUMN_SUPPORT_MESSAGE_ID = "id";
    public static final String COLUMN_SUPPORT_MESSAGE_USER_ID = "userId";
    public static final String COLUMN_SUPPORT_MESSAGE_ORDER_ID = "orderId"; // Can be -1 if not related to specific order
    public static final String COLUMN_SUPPORT_MESSAGE_SUBJECT = "subject";
    public static final String COLUMN_SUPPORT_MESSAGE_MESSAGE = "message";
    public static final String COLUMN_SUPPORT_MESSAGE_TIMESTAMP = "timestamp";
    public static final String COLUMN_SUPPORT_MESSAGE_STATUS = "status";
    public static final String TABLE_SUPPORT_RESPONSE = "support_responses";
    public static final String COLUMN_SUPPORT_RESPONSE_ID = "id";
    public static final String COLUMN_SUPPORT_RESPONSE_MESSAGE_ID = "messageId";
    public static final String COLUMN_SUPPORT_RESPONSE_ADMIN_ID = "adminId";
    public static final String COLUMN_SUPPORT_RESPONSE_CONTENT = "responseContent";
    public static final String COLUMN_SUPPORT_RESPONSE_TIMESTAMP = "timestamp";

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
    private static final String CREATE_TABLE_ORDER =
            "CREATE TABLE " + TABLE_ORDER + " (" +
                    COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_USER_ID + " INTEGER, " +
                    COLUMN_ORDER_DATE + " INTEGER, " +
                    COLUMN_ORDER_TOTAL_AMOUNT + " REAL, " +
                    COLUMN_ORDER_STATUS + " TEXT, " +
                    COLUMN_ORDER_SHIPPING_ADDRESS + " TEXT, " +
                    COLUMN_ORDER_PHONE_NUMBER + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")" +
                    ")";
    private static final String CREATE_TABLE_ORDER_ITEM =
            "CREATE TABLE " + TABLE_ORDER_ITEM + " (" +
                    COLUMN_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_ITEM_ORDER_ID + " INTEGER, " +
                    COLUMN_ORDER_ITEM_PRODUCT_ID + " INTEGER, " +
                    COLUMN_ORDER_ITEM_QUANTITY + " INTEGER, " +
                    COLUMN_ORDER_ITEM_PRICE + " REAL, " +
                    COLUMN_ORDER_ITEM_PRODUCT_NAME + " TEXT, " +
                    COLUMN_ORDER_ITEM_PRODUCT_IMAGE + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ORDER_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_ORDER_ITEM_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ")" +
                    ")";
    private static final String CREATE_TABLE_RATING =
            "CREATE TABLE " + TABLE_RATING + " (" +
                    COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RATING_PRODUCT_ID + " INTEGER, " +
                    COLUMN_RATING_USER_ID + " INTEGER, " +
                    COLUMN_RATING_VALUE + " REAL, " +
                    COLUMN_RATING_COMMENT + " TEXT, " +
                    COLUMN_RATING_TIMESTAMP + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_RATING_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_RATING_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")" +
                    ")";
    private static final String CREATE_TABLE_SUPPORT_MESSAGE =
            "CREATE TABLE " + TABLE_SUPPORT_MESSAGE + " (" +
                    COLUMN_SUPPORT_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SUPPORT_MESSAGE_USER_ID + " INTEGER, " +
                    COLUMN_SUPPORT_MESSAGE_ORDER_ID + " INTEGER, " +
                    COLUMN_SUPPORT_MESSAGE_SUBJECT + " TEXT, " +
                    COLUMN_SUPPORT_MESSAGE_MESSAGE + " TEXT, " +
                    COLUMN_SUPPORT_MESSAGE_TIMESTAMP + " INTEGER, " +
                    COLUMN_SUPPORT_MESSAGE_STATUS + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_SUPPORT_MESSAGE_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_SUPPORT_MESSAGE_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + COLUMN_ORDER_ID + ")" +
                    ")";
    private UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Đảm bảo luôn có tài khoản admin abc@gmail.com khi khởi tạo DB helper
        addOrUpdateAdminAbcGmail();
        addOrUpdateStaffXyzGmail();
    }
    private static final String CREATE_TABLE_SUPPORT_RESPONSE =
            "CREATE TABLE " + TABLE_SUPPORT_RESPONSE + " (" +
                    COLUMN_SUPPORT_RESPONSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SUPPORT_RESPONSE_MESSAGE_ID + " INTEGER, " +
                    COLUMN_SUPPORT_RESPONSE_ADMIN_ID + " INTEGER, " +
                    COLUMN_SUPPORT_RESPONSE_CONTENT + " TEXT, " +
                    COLUMN_SUPPORT_RESPONSE_TIMESTAMP + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_SUPPORT_RESPONSE_MESSAGE_ID + ") REFERENCES " + TABLE_SUPPORT_MESSAGE + "(" + COLUMN_SUPPORT_MESSAGE_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_SUPPORT_RESPONSE_ADMIN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")" +
                    ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_PRODUCT_VARIANT);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_BRAND);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_ORDER_ITEM);
        db.execSQL(CREATE_TABLE_RATING);
        db.execSQL(CREATE_TABLE_SUPPORT_MESSAGE);
        db.execSQL(CREATE_TABLE_SUPPORT_RESPONSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_VARIANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPORT_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPORT_RESPONSE);
        onCreate(db);
    }
    public void createDefaultAdminUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Kiểm tra xem đã có người dùng admin nào chưa
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USER, new String[]{COLUMN_ID}, COLUMN_ROLE + "=?", new String[]{"admin"}, null, null, null);
            if (cursor != null && cursor.getCount() == 0) {
                // Chưa có admin, tạo một tài khoản admin mặc định
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, "Admin User");
                values.put(COLUMN_EMAIL, "admin@example.com"); // Email admin mặc định
                values.put(COLUMN_PASSWORD, "admin123"); // Mật khẩu admin mặc định
                values.put(COLUMN_ROLE, "admin");
                values.put(COLUMN_PHONE, "0987654321");
                values.put(COLUMN_ADDRESS, "Admin Address");
                db.insert(TABLE_USER, null, values);
                Log.d("UserDatabaseHelper", "Default admin user created.");
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    // Thêm user mới (đăng ký)

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
    // Lấy user theo Id


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
    // Validate email format
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    // Validate password strength
    private boolean isValidPassword(String password) {
        // Ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    public boolean updateUser(User user) {
        // Validate dữ liệu trước khi cập nhật
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return false;
        }
        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            return false;
        }
        if (user.getPassword() == null || !isValidPassword(user.getPassword())) {
            return false;
        }
        if (user.getRole() == null || (!user.getRole().equals("admin") && !user.getRole().equals("user"))) {
            return false;
        }
        
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName().trim());
        values.put(COLUMN_EMAIL, user.getEmail().trim());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ROLE, user.getRole());
        values.put(COLUMN_PHONE, user.getPhone() != null ? user.getPhone().trim() : "");
        values.put(COLUMN_ADDRESS, user.getAddress() != null ? user.getAddress().trim() : "");
        
        int result = db.update(TABLE_USER, values, COLUMN_ID + "=?", new String[]{String.valueOf(user.getId())});
        db.close();
        return result > 0;
    }

    // Đổi mật khẩu
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        int rows = db.update(TABLE_USER, values, COLUMN_EMAIL + "=?", new String[]{email});
        return rows > 0;
    }

    // CRUD cho Category
    public boolean addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_DESCRIPTION, category.getDescription());
        long result = db.insert(TABLE_CATEGORY, null, values);
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
        return categoryList;
    }

    public boolean updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_DESCRIPTION, category.getDescription());
        int rows = db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(category.getId())});
        return rows > 0;
    }

    public boolean deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});
        return rows > 0;
    }
    public Category getCategoryById(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Category category = null;
        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_CATEGORY,
                    null,
                    COLUMN_CATEGORY_ID + "=?",
                    new String[]{String.valueOf(categoryId)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)));
                category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_DESCRIPTION)));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return category;
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
        return result != -1;
    }

    // Tìm kiếm sản phẩm theo tên
    public java.util.List<Product> searchProducts(String query) {
        java.util.List<Product> productList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "%" + query + "%";
        Cursor cursor = db.query(TABLE_PRODUCT, null, 
            COLUMN_PRODUCT_NAME + " LIKE ? OR " + COLUMN_PRODUCT_DESCRIPTION + " LIKE ?",
            new String[]{searchQuery, searchQuery}, null, null, null);
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

    // Lọc sản phẩm theo khoảng giá
    public java.util.List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        java.util.List<Product> productList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, null, 
            COLUMN_PRODUCT_PRICE + " BETWEEN ? AND ?",
            new String[]{String.valueOf(minPrice), String.valueOf(maxPrice)}, 
            null, null, COLUMN_PRODUCT_PRICE + " ASC");
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

    // Lấy sản phẩm đã sắp xếp
    public java.util.List<Product> getAllProductsSorted(String sortBy, boolean ascending) {
        java.util.List<Product> productList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String orderBy = "";
        switch (sortBy.toLowerCase()) {
            case "price":
                orderBy = COLUMN_PRODUCT_PRICE;
                break;
            case "name":
                orderBy = COLUMN_PRODUCT_NAME;
                break;
            case "rating":
                orderBy = COLUMN_PRODUCT_RATE;
                break;
            default:
                orderBy = COLUMN_PRODUCT_ID;
        }
        orderBy += (ascending ? " ASC" : " DESC");
        
        Cursor cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, orderBy);
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
        return rows > 0;
    }

    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PRODUCT, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
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
        return variantList;
    }

    public boolean updateProductVariant(ProductVariant variant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VARIANT_COLOR, variant.getColor());
        values.put(COLUMN_VARIANT_SIZE, variant.getSize());
        values.put(COLUMN_VARIANT_STOCK, variant.getStock());
        int rows = db.update(TABLE_PRODUCT_VARIANT, values, COLUMN_VARIANT_ID + "=?", new String[]{String.valueOf(variant.getId())});
        return rows > 0;
    }

    public boolean deleteProductVariant(int variantId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PRODUCT_VARIANT, COLUMN_VARIANT_ID + "=?", new String[]{String.valueOf(variantId)});
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
        return cartList;
    }

    public boolean updateCartItem(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, cart.getQuantity());
        int rows = db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cart.getId())});
        return rows > 0;
    }

    public boolean deleteCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
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
    // Place an order from cart items
    public boolean placeOrder(int userId, String shippingAddress, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        long orderId = -1;
        try {
            List<Cart> cartItems = getCartItems(userId);
            if (cartItems.isEmpty()) {
                return false; // No items in cart to place an order
            }

            double totalAmount = 0;
            for (Cart cartItem : cartItems) {
                Product product = getProductById(cartItem.getProductId());
                if (product != null) {
                    totalAmount += product.getPrice() * cartItem.getQuantity();
                }
            }

            ContentValues orderValues = new ContentValues();
            orderValues.put(COLUMN_ORDER_USER_ID, userId);
            orderValues.put(COLUMN_ORDER_DATE, System.currentTimeMillis()); // Current timestamp
            orderValues.put(COLUMN_ORDER_TOTAL_AMOUNT, totalAmount);
            orderValues.put(COLUMN_ORDER_STATUS, "Pending"); // Initial status
            orderValues.put(COLUMN_ORDER_SHIPPING_ADDRESS, shippingAddress);
            orderValues.put(COLUMN_ORDER_PHONE_NUMBER, phoneNumber);

            orderId = db.insert(TABLE_ORDER, null, orderValues);

            if (orderId == -1) {
                return false; // Failed to insert order
            }

            for (Cart cartItem : cartItems) {
                Product product = getProductById(cartItem.getProductId());
                if (product != null) {
                    ContentValues itemValues = new ContentValues();
                    itemValues.put(COLUMN_ORDER_ITEM_ORDER_ID, orderId);
                    itemValues.put(COLUMN_ORDER_ITEM_PRODUCT_ID, cartItem.getProductId());
                    itemValues.put(COLUMN_ORDER_ITEM_QUANTITY, cartItem.getQuantity());
                    itemValues.put(COLUMN_ORDER_ITEM_PRICE, product.getPrice()); // Save current price
                    itemValues.put(COLUMN_ORDER_ITEM_PRODUCT_NAME, product.getName()); // Save product name
                    itemValues.put(COLUMN_ORDER_ITEM_PRODUCT_IMAGE, product.getImageUrl()); // Save product image

                    long result = db.insert(TABLE_ORDER_ITEM, null, itemValues);
                    if (result == -1) {
                        db.yieldIfContendedSafely(); // Allow rollback in case of error
                        throw new Exception("Failed to insert order item");
                    }
                }
            }

            // Clear the cart after successfully placing the order
            db.delete(TABLE_CART, COLUMN_CART_USER_ID + "=?", new String[]{String.valueOf(userId)});

            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }
    // Get all orders for a specific user
    public java.util.List<Order> getOrdersByUserId(int userId) {
        java.util.List<Order> orderList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDER, null, COLUMN_ORDER_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, COLUMN_ORDER_DATE + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)));
                order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE)));
                order.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)));
                order.setShippingAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_SHIPPING_ADDRESS)));
                order.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PHONE_NUMBER)));
                orderList.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return orderList;
    }

    // Get order items for a specific order
    public java.util.List<OrderItem> getOrderItemsByOrderId(int orderId) {
        java.util.List<OrderItem> orderItemList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDER_ITEM, null, COLUMN_ORDER_ITEM_ORDER_ID + "=?", new String[]{String.valueOf(orderId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_ID)));
                orderItem.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_ORDER_ID)));
                orderItem.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_ID)));
                orderItem.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_QUANTITY)));
                orderItem.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRICE)));
                orderItem.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_NAME)));
                orderItem.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_IMAGE)));
                orderItemList.add(orderItem);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return orderItemList;
    }

    // Update order status (e.g., for admin)
    public boolean updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, newStatus);
        int rows = db.update(TABLE_ORDER, values, COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
        return rows > 0;
    }
    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Order order = null;
        try (Cursor cursor = db.query(TABLE_ORDER, null, COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(orderId)}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)));
                order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE)));
                order.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)));
                order.setShippingAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_SHIPPING_ADDRESS)));
                order.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PHONE_NUMBER)));
            }
        }
        return order;
    }
    // Add a new rating
    public boolean addRating(Rating rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING_PRODUCT_ID, rating.getProductId());
        values.put(COLUMN_RATING_USER_ID, rating.getUserId());
        values.put(COLUMN_RATING_VALUE, rating.getRatingValue());
        values.put(COLUMN_RATING_COMMENT, rating.getReviewComment());
        values.put(COLUMN_RATING_TIMESTAMP, rating.getTimestamp());
        long result = db.insert(TABLE_RATING, null, values);
        if (result != -1) {
            updateProductAverageRating(rating.getProductId());
            return true;
        }
        return false;
    }
    public java.util.List<Rating> getRatingsByProductId(int productId) {
        java.util.List<Rating> ratingList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RATING, null, COLUMN_RATING_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)}, null, null, COLUMN_RATING_TIMESTAMP + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Rating rating = new Rating();
                rating.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_ID)));
                rating.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_PRODUCT_ID)));
                rating.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_USER_ID)));
                rating.setRatingValue(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING_VALUE)));
                rating.setReviewComment(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING_COMMENT)));
                rating.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_RATING_TIMESTAMP)));
                ratingList.add(rating);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ratingList;
    }
    public void updateProductAverageRating(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COLUMN_RATING_VALUE + ") FROM " + TABLE_RATING + " WHERE " + COLUMN_RATING_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
        float averageRating = 0.0f;
        if (cursor != null && cursor.moveToFirst()) {
            averageRating = cursor.getFloat(0);
            cursor.close();
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_RATE, averageRating);
        db.update(TABLE_PRODUCT, values, COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
    }
    public Rating getUserRatingForProduct(int userId, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Rating rating = null;
        try (Cursor cursor = db.query(TABLE_RATING, null, COLUMN_RATING_USER_ID + "=? AND " + COLUMN_RATING_PRODUCT_ID + "=?", new String[]{String.valueOf(userId), String.valueOf(productId)}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                rating = new Rating();
                rating.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_ID)));
                rating.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_PRODUCT_ID)));
                rating.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_USER_ID)));
                rating.setRatingValue(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING_VALUE)));
                rating.setReviewComment(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING_COMMENT)));
                rating.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_RATING_TIMESTAMP)));
            }
        }
        return rating;
    }
    // Update an existing rating
    public boolean updateRating(Rating rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING_VALUE, rating.getRatingValue());
        values.put(COLUMN_RATING_COMMENT, rating.getReviewComment());
        values.put(COLUMN_RATING_TIMESTAMP, System.currentTimeMillis()); // Update timestamp
        int rows = db.update(TABLE_RATING, values, COLUMN_RATING_ID + "=?", new String[]{String.valueOf(rating.getId())});
        if (rows > 0) {
            updateProductAverageRating(rating.getProductId());
            return true;
        }
        return false;
    }
    public long addOrder(int userId, long orderDate, double totalAmount, String status, String shippingAddress, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_ID, userId);
        values.put(COLUMN_ORDER_DATE, orderDate);
        values.put(COLUMN_ORDER_TOTAL_AMOUNT, totalAmount);
        values.put(COLUMN_ORDER_STATUS, status);
        values.put(COLUMN_ORDER_SHIPPING_ADDRESS, shippingAddress);
        values.put(COLUMN_ORDER_PHONE_NUMBER, phoneNumber);

        long orderId = db.insert(TABLE_ORDER, null, values);
        return orderId;
    }
    public long addOrderItem(long orderId, int productId, int quantity, double price, String productName, String productImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ITEM_ORDER_ID, orderId);
        values.put(COLUMN_ORDER_ITEM_PRODUCT_ID, productId);
        values.put(COLUMN_ORDER_ITEM_QUANTITY, quantity);
        values.put(COLUMN_ORDER_ITEM_PRICE, price);
        values.put(COLUMN_ORDER_ITEM_PRODUCT_NAME, productName);
        values.put(COLUMN_ORDER_ITEM_PRODUCT_IMAGE, productImage);

        long orderItemId = db.insert(TABLE_ORDER_ITEM, null, values);
        return orderItemId;
    }
    public List<Order> getAllOrdersByUserId(int userId) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_ORDER + " WHERE " + COLUMN_ORDER_USER_ID + " = ?";
            cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                    long orderDate = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
                    double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS));
                    String shippingAddress = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_SHIPPING_ADDRESS));
                    String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PHONE_NUMBER));

                    Order order = new Order(id, userId, orderDate, totalAmount, status, shippingAddress, phoneNumber);
                    orderList.add(order);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return orderList;
    }
    public List<OrderItem> getOrderItemsByOrderId(long orderId) {
        List<OrderItem> orderItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_ORDER_ITEM + " WHERE " + COLUMN_ORDER_ITEM_ORDER_ID + " = ?";
            cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(orderId)});

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_ID));
                    int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_ID));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_QUANTITY));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRICE));
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_NAME));
                    String productImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEM_PRODUCT_IMAGE));

                    OrderItem orderItem = new OrderItem(id, (int) orderId, productId, quantity, price, productName, productImage);
                    orderItemList.add(orderItem);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return orderItemList;
    }
    public boolean addSupportMessage(SupportMessage message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUPPORT_MESSAGE_USER_ID, message.getUserId());
        values.put(COLUMN_SUPPORT_MESSAGE_ORDER_ID, message.getOrderId());
        values.put(COLUMN_SUPPORT_MESSAGE_SUBJECT, message.getSubject());
        values.put(COLUMN_SUPPORT_MESSAGE_MESSAGE, message.getMessage());
        values.put(COLUMN_SUPPORT_MESSAGE_TIMESTAMP, message.getTimestamp());
        values.put(COLUMN_SUPPORT_MESSAGE_STATUS, message.getStatus()); // e.g., "New"

        long result = db.insert(TABLE_SUPPORT_MESSAGE, null, values);
        return result != -1;
    }

    // (Tùy chọn) Thêm phương thức để lấy tất cả tin nhắn hỗ trợ (cho Admin)
    public List<SupportMessage> getAllSupportMessages() {
        List<SupportMessage> messageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_SUPPORT_MESSAGE, null, null, null, null, null, COLUMN_SUPPORT_MESSAGE_TIMESTAMP + " DESC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SupportMessage message = new SupportMessage();
                    message.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_ID)));
                    message.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_USER_ID)));
                    message.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_ORDER_ID)));
                    message.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_SUBJECT)));
                    message.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_MESSAGE)));
                    message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_TIMESTAMP)));
                    message.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_STATUS)));
                    messageList.add(message);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return messageList;
    }
    public boolean addSupportResponse(SupportResponse response) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUPPORT_RESPONSE_MESSAGE_ID, response.getMessageId());
        values.put(COLUMN_SUPPORT_RESPONSE_ADMIN_ID, response.getAdminId());
        values.put(COLUMN_SUPPORT_RESPONSE_CONTENT, response.getResponseContent());
        values.put(COLUMN_SUPPORT_RESPONSE_TIMESTAMP, response.getTimestamp());

        long result = db.insert(TABLE_SUPPORT_RESPONSE, null, values);
        if (result != -1) {
            // Cập nhật trạng thái của tin nhắn hỗ trợ thành "Replied"
            updateSupportMessageStatus(response.getMessageId(), "Replied");
            return true;
        }
        return false;
    }

    // Lấy tất cả câu trả lời cho một tin nhắn hỗ trợ cụ thể
    public List<SupportResponse> getSupportResponsesByMessageId(int messageId) {
        List<SupportResponse> responseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_SUPPORT_RESPONSE, null,
                    COLUMN_SUPPORT_RESPONSE_MESSAGE_ID + "=?",
                    new String[]{String.valueOf(messageId)},
                    null, null, COLUMN_SUPPORT_RESPONSE_TIMESTAMP + " ASC"); // Sắp xếp theo thời gian tăng dần

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SupportResponse response = new SupportResponse();
                    response.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_RESPONSE_ID)));
                    response.setMessageId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_RESPONSE_MESSAGE_ID)));
                    response.setAdminId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_RESPONSE_ADMIN_ID)));
                    response.setResponseContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_RESPONSE_CONTENT)));
                    response.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_RESPONSE_TIMESTAMP)));
                    responseList.add(response);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return responseList;
    }

    // Phương thức để cập nhật trạng thái của một tin nhắn hỗ trợ
    public boolean updateSupportMessageStatus(int messageId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUPPORT_MESSAGE_STATUS, newStatus);
        int rowsAffected = db.update(TABLE_SUPPORT_MESSAGE, values,
                COLUMN_SUPPORT_MESSAGE_ID + "=?", new String[]{String.valueOf(messageId)});
        return rowsAffected > 0;
    }

    // Phương thức mới để lấy SupportMessage theo ID (cần cho SupportMessageDetailActivity)
    public SupportMessage getSupportMessageById(int messageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        SupportMessage message = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_SUPPORT_MESSAGE, null, COLUMN_SUPPORT_MESSAGE_ID + "=?", new String[]{String.valueOf(messageId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                message = new SupportMessage();
                message.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_ID)));
                message.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_USER_ID)));
                message.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_ORDER_ID)));
                message.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_SUBJECT)));
                message.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_MESSAGE)));
                message.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_TIMESTAMP)));
                message.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORT_MESSAGE_STATUS)));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return message;
    }
}