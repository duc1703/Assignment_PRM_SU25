-- Tạo cơ sở dữ liệu và các bảng
PRAGMA foreign_keys = ON;

-- Bảng người dùng
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    role VARCHAR(20) DEFAULT 'customer',
    is_verified BOOLEAN DEFAULT 0,
    verification_token VARCHAR(255),
    verification_token_expires TIMESTAMP,
    failed_login_attempts INTEGER DEFAULT 0,
    account_locked_until TIMESTAMP,
    reset_password_token VARCHAR(255),
    reset_token_expires TIMESTAMP,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng phiên đăng nhập
CREATE TABLE IF NOT EXISTS user_sessions (
    session_id TEXT PRIMARY KEY,
    user_id INTEGER NOT NULL,
    ip_address TEXT,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Bảng lịch sử đăng nhập
CREATE TABLE IF NOT EXISTS login_history (
    login_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address TEXT,
    user_agent TEXT,
    status TEXT NOT NULL, -- 'success' hoặc 'failed'
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Bảng danh mục
CREATE TABLE IF NOT EXISTS categories (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories(category_id)
);

-- Bảng sản phẩm
CREATE TABLE IF NOT EXISTS products (
    product_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category_id INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    brand VARCHAR(100),
    gender VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- Bảng biến thể sản phẩm
CREATE TABLE IF NOT EXISTS product_variants (
    variant_id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id INTEGER NOT NULL,
    color VARCHAR(50) NOT NULL,
    size DECIMAL(5, 1) NOT NULL,
    sku VARCHAR(100) UNIQUE,
    additional_price DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Bảng kho hàng
CREATE TABLE IF NOT EXISTS inventory (
    inventory_id INTEGER PRIMARY KEY AUTOINCREMENT,
    variant_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    last_restocked TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id) ON DELETE CASCADE
);

-- Bảng đơn hàng
CREATE TABLE IF NOT EXISTS orders (
    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    shipping_address TEXT NOT NULL,
    payment_method VARCHAR(50),
    payment_status VARCHAR(20) DEFAULT 'unpaid',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Bảng chi tiết đơn hàng
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    variant_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);

-- Bảng đánh giá sản phẩm
CREATE TABLE IF NOT EXISTS reviews (
    review_id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Tạo chỉ mục
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_variants_product ON product_variants(product_id);
CREATE INDEX IF NOT EXISTS idx_inventory_variant ON inventory(variant_id);
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_reviews_product ON reviews(product_id);

-- Thêm dữ liệu mẫu
-- 1. Thêm người dùng (với các trường mới)
-- Mật khẩu mặc định: password123
INSERT INTO users (
    username, 
    password_hash, 
    email, 
    full_name, 
    phone, 
    address, 
    role, 
    is_verified,
    last_login
) VALUES 
('admin', '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@example.com', 'Admin', '0123456789', '123 Admin Street', 'admin', 1, '2025-07-20 23:00:00'),
('customer1', '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'customer1@example.com', 'Nguyễn Văn A', '0987654321', '456 Customer Street', 'customer', 1, '2025-07-20 22:30:00'),
('customer2', '$2b$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'customer2@example.com', 'Trần Thị B', '0905123456', '789 Customer Street', 'customer', 0, NULL);

-- Thêm dữ liệu mẫu cho lịch sử đăng nhập
INSERT INTO login_history (user_id, login_time, ip_address, user_agent, status) VALUES
(1, '2025-07-20 23:00:00', '192.168.1.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'success'),
(2, '2025-07-20 22:30:00', '192.168.1.2', 'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X)', 'success'),
(2, '2025-07-20 22:25:00', '192.168.1.2', 'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X)', 'failed');

-- 2. Thêm danh mục
INSERT INTO categories (name, description, parent_id) VALUES 
('Giày nam', 'Giày dành cho nam giới', NULL),
('Giày nữ', 'Giày dành cho nữ giới', NULL),
('Giày thể thao', 'Giày thể thao đa năng', 1),
('Giày công sở', 'Giày công sở lịch sự', 1),
('Giày thể thao', 'Giày thể thao nữ thời trang', 2),
('Giày cao gót', 'Giày cao gót thời trang', 2);

-- 3. Thêm sản phẩm
INSERT INTO products (name, description, category_id, price, brand, gender) VALUES 
('Giày thể thao nam đế phẳng', 'Giày thể thao nam thoải mái, phù hợp tập luyện', 3, 1200000, 'Nike', 'male'),
('Giày công sở da bóng', 'Giày công sở nam lịch lãm', 4, 1500000, 'Bitis', 'male'),
('Giày thể thao nữ đế cao', 'Giày thể thao nữ thời trang', 5, 1100000, 'Adidas', 'female'),
('Giày cao gót quai ngang', 'Giày cao gót nữ thanh lịch', 6, 900000, 'Vascara', 'female');

-- 4. Thêm biến thể sản phẩm
INSERT INTO product_variants (product_id, color, size, sku, additional_price) VALUES 
(1, 'Đen', 40, 'SP1-DEN-40', 0),
(1, 'Trắng', 41, 'SP1-TRANG-41', 100000),
(2, 'Đen', 39, 'SP2-DEN-39', 0),
(3, 'Hồng', 36, 'SP3-HONG-36', 0),
(3, 'Xanh', 37, 'SP3-XANH-37', 50000),
(4, 'Đỏ', 35, 'SP4-DO-35', 0),
(4, 'Đen', 36, 'SP4-DEN-36', 0);

-- 5. Thêm vào kho hàng
INSERT INTO inventory (variant_id, quantity, last_restocked) VALUES 
(1, 50, '2025-07-15'),
(2, 30, '2025-07-15'),
(3, 25, '2025-07-10'),
(4, 40, '2025-07-12'),
(5, 35, '2025-07-12'),
(6, 20, '2025-07-05'),
(7, 25, '2025-07-05');

-- 6. Thêm đơn hàng mẫu
INSERT INTO orders (user_id, order_date, total_amount, status, shipping_address, payment_method, payment_status) VALUES 
(2, '2025-07-18 10:30:00', 2300000, 'completed', '456 Customer Street', 'COD', 'paid'),
(3, '2025-07-19 14:15:00', 1150000, 'processing', '789 Customer Street', 'Momo', 'paid');

-- 7. Thêm chi tiết đơn hàng
INSERT INTO order_items (order_id, variant_id, quantity, unit_price, subtotal) VALUES 
(1, 1, 1, 1200000, 1200000),
(1, 3, 1, 1500000, 1500000),
(2, 4, 1, 1100000, 1100000);

-- 8. Thêm đánh giá
INSERT INTO reviews (product_id, user_id, rating, comment, review_date) VALUES 
(1, 2, 5, 'Sản phẩm tốt, đi rất êm', '2025-07-18 16:20:00'),
(2, 2, 4, 'Giày đẹp nhưng hơi chật', '2025-07-18 16:25:00'),
(3, 3, 5, 'Màu sắc đẹp, chất lượng tốt', '2025-07-19 15:30:00');

-- Tạo trigger để tự động cập nhật updated_at
CREATE TRIGGER IF NOT EXISTS update_users_updated_at
AFTER UPDATE ON users
BEGIN
    UPDATE users SET updated_at = CURRENT_TIMESTAMP WHERE user_id = NEW.user_id;
    
    -- Cập nhật last_login khi đăng nhập thành công
    IF NEW.last_login IS NOT OLD.last_login THEN
        UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = NEW.user_id;
    END IF;
    
    -- Reset failed_login_attempts khi đăng nhập thành công
    IF NEW.failed_login_attempts = 0 AND OLD.failed_login_attempts > 0 THEN
        UPDATE users SET account_locked_until = NULL WHERE user_id = NEW.user_id;
    END IF;
END;

CREATE TRIGGER IF NOT EXISTS update_products_updated_at
AFTER UPDATE ON products
BEGIN
    UPDATE products SET updated_at = CURRENT_TIMESTAMP WHERE product_id = NEW.product_id;
END;

CREATE TRIGGER IF NOT EXISTS update_inventory_updated_at
AFTER UPDATE ON inventory
BEGIN
    UPDATE inventory SET updated_at = CURRENT_TIMESTAMP WHERE inventory_id = NEW.inventory_id;
END;

CREATE TRIGGER IF NOT EXISTS update_orders_updated_at
AFTER UPDATE ON orders
BEGIN
    UPDATE orders SET updated_at = CURRENT_TIMESTAMP WHERE order_id = NEW.order_id;
END;

-- Trigger để ghi lại lịch sử đăng nhập
CREATE TRIGGER IF NOT EXISTS log_user_login_attempt
AFTER INSERT ON login_history
BEGIN
    -- Nếu đăng nhập thất bại, tăng số lần thử đăng nhập sai
    IF NEW.status = 'failed' THEN
        UPDATE users 
        SET failed_login_attempts = failed_login_attempts + 1,
            account_locked_until = CASE 
                WHEN failed_login_attempts + 1 >= 5 THEN datetime('now', '+30 minutes')
                ELSE account_locked_until 
            END
        WHERE user_id = NEW.user_id;
    ELSE
        -- Nếu đăng nhập thành công, reset số lần thử đăng nhập sai
        UPDATE users 
        SET failed_login_attempts = 0,
            account_locked_until = NULL,
            last_login = CURRENT_TIMESTAMP
        WHERE user_id = NEW.user_id;
    END IF;
END;
