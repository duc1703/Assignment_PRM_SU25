// ==================== BIẾN TOÀN CỤC ====================
let cart = [];
let wishlist = [];

// Dữ liệu sản phẩm mẫu (sau này sẽ lấy từ API)
const products = [
    {
        id: 1,
        name: 'Giày Thể Thao Nam Đế Phẳng',
        price: 1200000,
        oldPrice: 1500000,
        image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80',
        rating: 4.5,
        reviews: 24,
        isNew: true,
        discount: 20,
        colors: ['#ff6b6b', '#4ecdc4', '#2d3436'],
        sizes: ['38', '39', '40', '41', '42']
    },
    {
        id: 2,
        name: 'Giày Cao Gót Quai Ngang',
        price: 900000,
        oldPrice: 1200000,
        image: 'https://via.placeholder.com/300x300/ff9e80/ffffff?text=Giày+Cao+Gót',
        rating: 4.2,
        reviews: 18,
        isNew: true,
        discount: 25
    },
    {
        id: 3,
        name: 'Giày Tây Công Sở',
        price: 1500000,
        oldPrice: 1800000,
        image: 'https://via.placeholder.com/300x300/81c784/ffffff?text=Giày+Tây',
        rating: 4.8,
        reviews: 32,
        isNew: false,
        discount: 15
    },
    {
        id: 4,
        name: 'Dép Quai Ngang Nữ',
        price: 350000,
        oldPrice: 500000,
        image: 'https://via.placeholder.com/300x300/64b5f6/ffffff?text=Dép+Quai+Ngang',
        rating: 4.0,
        reviews: 15,
        isNew: false,
        discount: 30
    },
    {
        id: 5,
        name: 'Giày Thể Thao Nữ Đế Cao',
        price: 1100000,
        oldPrice: 1300000,
        image: 'https://via.placeholder.com/300x300/ffd54f/ffffff?text=Giày+Thể+Thao+Nữ',
        rating: 4.7,
        reviews: 28,
        isNew: true,
        discount: 15
    },
    {
        id: 6,
        name: 'Giày Lười Da Bóng',
        price: 850000,
        oldPrice: 1000000,
        image: 'https://via.placeholder.com/300x300/a5d6a7/ffffff?text=Giày+Lười',
        rating: 4.3,
        reviews: 21,
        isNew: false,
        discount: 15
    },
    {
        id: 7,
        name: 'Sneaker Thời Trang',
        price: 950000,
        oldPrice: 1200000,
        image: 'https://via.placeholder.com/300x300/90caf9/ffffff?text=Sneaker',
        rating: 4.6,
        reviews: 35,
        isNew: true,
        discount: 20
    },
    {
        id: 8,
        name: 'Dép Xỏ Ngón Nữ',
        price: 250000,
        oldPrice: 350000,
        image: 'https://via.placeholder.com/300x300/ce93d8/ffffff?text=Dép+Xỏ+Ngón',
        rating: 4.1,
        reviews: 12,
        isNew: false,
        discount: 28
    }
];

// ==================== HÀM HIỂN THỊ SẢN PHẨM ====================
function displayProducts() {
    const productGrid = document.querySelector('.product-grid');
    if (!productGrid) return;

    productGrid.innerHTML = products.map(product => `
        <div class="product-card" data-id="${product.id}">
            <div class="product-image">
                <img src="${product.image}" alt="${product.name}" loading="lazy">
                <div class="product-actions">
                    <button class="add-to-wishlist ${isInWishlist(product.id) ? 'active' : ''}" 
                            data-id="${product.id}" 
                            data-bs-toggle="tooltip" 
                            title="${isInWishlist(product.id) ? 'Xóa khỏi yêu thích' : 'Thêm vào yêu thích'}">
                        <i class="${isInWishlist(product.id) ? 'fas' : 'far'} fa-heart"></i>
                    </button>
                    <button class="quick-view" 
                            data-id="${product.id}" 
                            data-bs-toggle="tooltip" 
                            title="Xem nhanh">
                        <i class="far fa-eye"></i>
                    </button>
                    <button class="add-to-cart" 
                            data-id="${product.id}" 
                            data-bs-toggle="tooltip" 
                            title="Thêm vào giỏ hàng">
                        <i class="fas fa-shopping-cart"></i>
                    </button>
                </div>
                ${product.isNew ? '<span class="product-badge new">Mới</span>' : ''}
                ${product.discount ? `<span class="product-badge discount">-${product.discount}%</span>` : ''}
                ${product.rating >= 4.5 ? '<span class="product-badge hot">Hot</span>' : ''}
            </div>
            <div class="product-info">
                <h3 class="product-title">
                    <a href="product-detail.html?id=${product.id}" class="text-decoration-none">${product.name}</a>
                </h3>
                <div class="product-price">
                    <span class="current-price">${formatPrice(product.price)}đ</span>
                    ${product.oldPrice ? `<span class="old-price">${formatPrice(product.oldPrice)}đ</span>` : ''}
                </div>
                <div class="product-rating">
                    ${renderRating(product.rating)}
                    <span class="review-count">(${product.reviews})</span>
                </div>
                <div class="product-colors mt-2">
                    ${product.colors.map(color => 
                        `<span class="color-option" style="background-color: ${color}" 
                              data-bs-toggle="tooltip" 
                              title="Màu sản phẩm"></span>`
                    ).join('')}
                </div>
                <button class="btn btn-primary w-100 mt-3 add-to-cart" data-id="${product.id}">
                    <i class="fas fa-shopping-cart me-2"></i>Thêm vào giỏ
                </button>
            </div>
        </div>
    `).join('');

    // Khởi tạo tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// ==================== HÀM TIỆN ÍCH ====================

/**
 * Định dạng giá tiền
 * @param {number} price - Giá tiền cần định dạng
 * @returns {string} Giá đã được định dạng
 */
function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

/**
 * Kiểm tra sản phẩm có trong danh sách yêu thích không
 * @param {number} productId - ID sản phẩm cần kiểm tra
 * @returns {boolean} Trả về true nếu sản phẩm có trong danh sách yêu thích
 */
function isInWishlist(productId) {
    return wishlist.some(item => item.id === productId);
}

/**
 * Hiển thị loading
 */
function showLoading() {
    let loading = document.getElementById('loading');
    if (!loading) {
        loading = document.createElement('div');
        loading.id = 'loading';
        loading.className = 'loading';
        loading.innerHTML = `
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-2">Đang tải...</p>
        `;
        document.body.appendChild(loading);
    }
    loading.style.display = 'flex';
}

/**
 * Ẩn loading
 */
function hideLoading() {
    const loading = document.getElementById('loading');
    if (loading) {
        loading.style.display = 'none';
    }
}

/**
 * Khởi tạo giỏ hàng từ localStorage
 */
function initCart() {
    cart = JSON.parse(localStorage.getItem('cart')) || [];
    wishlist = JSON.parse(localStorage.getItem('wishlist')) || [];
    updateCartCount();
}

/**
 * Hiển thị đánh giá sao
 * @param {number} rating - Điểm đánh giá từ 0-5
 * @returns {string} HTML hiển thị các ngôi sao đánh giá
 */
function renderRating(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    let stars = '';
    
    for (let i = 1; i <= 5; i++) {
        if (i <= fullStars) {
            stars += '<i class="fas fa-star"></i>';
        } else if (i === fullStars + 1 && hasHalfStar) {
            stars += '<i class="fas fa-star-half-alt"></i>';
        } else {
            stars += '<i class="far fa-star"></i>';
        }
    }
    
    return stars;
}

/**
 * Thêm sản phẩm vào giỏ hàng
 * @param {Event} e - Sự kiện click
 */
function addToCart(e) {
    e.preventDefault();
    e.stopPropagation();
    
    // Lấy ID sản phẩm từ thuộc tính data-id
    const productId = parseInt(e.currentTarget.getAttribute('data-id'));
    const product = products.find(p => p.id === productId);
    
    if (!product) return;
    
    // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
    const existingItem = cart.find(item => item.id === productId);
    
    if (existingItem) {
        // Nếu đã có, tăng số lượng lên 1
        existingItem.quantity += 1;
        showNotification(`Đã cập nhật số lượng ${product.name} trong giỏ hàng`);
    } else {
        // Nếu chưa có, thêm mới vào giỏ hàng
        cart.push({
            id: product.id,
            name: product.name,
            price: product.price,
            image: product.image,
            quantity: 1,
            maxQuantity: 10 // Số lượng tối đa có thể mua
        });
        showNotification(`Đã thêm ${product.name} vào giỏ hàng`);
    }
    
    // Lưu giỏ hàng vào localStorage
    localStorage.setItem('cart', JSON.stringify(cart));
    
    // Cập nhật số lượng sản phẩm trong giỏ hàng
    updateCartCount();
    
    // Thêm hiệu ứng vào nút giỏ hàng
    const cartIcon = document.querySelector('.cart-icon');
    if (cartIcon) {
        cartIcon.classList.add('animate__animated', 'animate__rubberBand');
        setTimeout(() => {
            cartIcon.classList.remove('animate__animated', 'animate__rubberBand');
        }, 1000);
    }
}

// Cập nhật số lượng sản phẩm trong giỏ hàng
function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const totalItems = cart.reduce((total, item) => total + item.quantity, 0);
    
    const cartCount = document.querySelector('.cart-count');
    if (cartCount) {
        cartCount.textContent = totalItems;
        cartCount.style.display = totalItems > 0 ? 'flex' : 'none';
    }
}

// Thêm/xóa sản phẩm yêu thích
function toggleWishlist(e) {
    e.preventDefault();
    const button = e.target.closest('button');
    button.classList.toggle('active');
    
    if (button.classList.contains('active')) {
        button.innerHTML = '<i class="fas fa-heart"></i>';
        showNotification('Đã thêm vào yêu thích!');
    } else {
        button.innerHTML = '<i class="far fa-heart"></i>';
    }
}

// Hiển thị thông báo
function showNotification(message) {
    // Tạo thông báo nếu chưa có
    let notification = document.querySelector('.notification');
    
    if (!notification) {
        notification = document.createElement('div');
        notification.className = 'notification';
        document.body.appendChild(notification);
    }
    
    notification.textContent = message;
    notification.classList.add('show');
    
    // Ẩn thông báo sau 3 giây
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

// Xử lý đăng nhập
function handleLogin() {
    // Lấy dữ liệu từ form đăng nhập
    const loginForm = document.getElementById('login-form');
    if (!loginForm) return;
    
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const email = this.querySelector('input[type="email"]').value;
        const password = this.querySelector('input[type="password"]').value;
        const rememberMe = this.querySelector('input[type="checkbox"]').checked;
        
        // Gửi dữ liệu đăng nhập đến server
        // Đoạn code này sẽ được thay thế bằng AJAX call thực tế
        console.log('Đăng nhập với:', { email, password, rememberMe });
        
        // Giả lập đăng nhập thành công
        const user = {
            id: 1,
            name: 'Người Dùng Mẫu',
            email: email,
            role: 'user'
        };
        
        // Lưu thông tin người dùng vào localStorage
        localStorage.setItem('currentUser', JSON.stringify(user));
        
        // Chuyển hướng về trang chủ
        window.location.href = 'index.html';
    });
}

// Kiểm tra trạng thái đăng nhập
function checkAuth() {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    const userAccount = document.getElementById('user-account');
    
    if (currentUser) {
        // Người dùng đã đăng nhập
        if (userAccount) {
            userAccount.innerHTML = `<i class="fas fa-user"></i> ${currentUser.name.split(' ').pop()}`;
            userAccount.href = 'profile.html';
        }
    } else {
        // Chưa đăng nhập, chuyển hướng đến trang đăng nhập
        if (window.location.pathname.includes('profile.html') || 
            window.location.pathname.includes('checkout.html')) {
            window.location.href = 'login.html';
        }
    }
}

/**
 * Xử lý đăng xuất
 */
function handleLogout() {
    const logoutBtn = document.getElementById('logout-btn');
    if (!logoutBtn) return;
    
    logoutBtn.addEventListener('click', function(e) {
        e.preventDefault();
        
        // Xóa thông tin người dùng khỏi localStorage
        localStorage.removeItem('currentUser');
        
        // Hiển thị thông báo
        showNotification('Đã đăng xuất thành công', 'info');
        
        // Chuyển hướng về trang chủ sau 1 giây
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1000);
    });
}

/**
 * Xử lý sự kiện khi nhấn nút quay lại đầu trang
 */
function handleBackToTop() {
    const backToTopBtn = document.querySelector('.back-to-top');
    if (!backToTopBtn) return;
    
    // Hiển thị/ẩn nút khi cuộn trang
    window.addEventListener('scroll', function() {
        if (window.pageYOffset > 300) {
            backToTopBtn.classList.add('show');
        } else {
            backToTopBtn.classList.remove('show');
        }
    });
    
    // Xử lý sự kiện click
    backToTopBtn.addEventListener('click', function(e) {
        e.preventDefault();
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
}

/**
 * Xử lý sự kiện khi nhấn nút tìm kiếm
 */
function handleSearch() {
    const searchForm = document.querySelector('.search-form');
    if (!searchForm) return;
    
    searchForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const searchInput = this.querySelector('input[type="search"]');
        const searchTerm = searchInput.value.trim();
        
        if (searchTerm) {
            // Lưu từ khóa tìm kiếm vào sessionStorage
            sessionStorage.setItem('searchQuery', searchTerm);
            
            // Chuyển hướng đến trang kết quả tìm kiếm
            window.location.href = `search.html?q=${encodeURIComponent(searchTerm)}`;
        }
    });
}

/**
 * Khởi tạo tất cả các sự kiện
 */
function initEventListeners() {
    // Sự kiện cho nút thêm vào giỏ hàng
    document.addEventListener('click', function(e) {
        if (e.target.closest('.add-to-cart')) {
            addToCart(e);
        } else if (e.target.closest('.add-to-wishlist')) {
            toggleWishlist(e);
        } else if (e.target.closest('.quick-view')) {
            // Xử lý xem nhanh sản phẩm
            const productId = parseInt(e.target.closest('.quick-view').getAttribute('data-id'));
            // showQuickViewModal(productId);
            showNotification('Tính năng đang phát triển', 'info');
        }
    });
    
    // Sự kiện cho menu mobile
    const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
    const mainNav = document.querySelector('.main-nav');
    
    if (mobileMenuBtn && mainNav) {
        mobileMenuBtn.addEventListener('click', function() {
            mainNav.classList.toggle('active');
            this.classList.toggle('active');
        });
    }
}

/**
 * Khởi tạo ứng dụng
 */
function init() {
    // Khởi tạo giỏ hàng và danh sách yêu thích
    initCart();
    
    // Kiểm tra trạng thái đăng nhập
    checkAuth();
    
    // Xử lý đăng nhập
    handleLogin();
    
    // Xử lý đăng xuất
    handleLogout();
    
    // Khởi tạo các sự kiện
    initEventListeners();
    
    // Xử lý nút quay lại đầu trang
    handleBackToTop();
    
    // Xử lý tìm kiếm
    handleSearch();
    
    // Hiển thị sản phẩm nếu có phần tử product-grid
    if (document.querySelector('.product-grid')) {
        displayProducts();
    }
    
    // Cập nhật số lượng sản phẩm trong giỏ hàng
    updateCartCount();
    
    // Khởi tạo tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Ẩn loading khi tất cả đã tải xong
    window.addEventListener('load', function() {
        setTimeout(hideLoading, 500);
    });
    
    // Hiển thị thông báo chào mừng nếu vừa đăng nhập
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('login') && urlParams.get('login') === 'success') {
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser) {
            showNotification(`Xin chào, ${currentUser.name}!`, 'success');
            
            // Xóa tham số login khỏi URL
            const url = new URL(window.location);
            url.searchParams.delete('login');
            window.history.replaceState({}, '', url);
        }
    }
}

// Chạy ứng dụng khi DOM đã tải xong
document.addEventListener('DOMContentLoaded', init);
