Onix là một hệ thống web và ứng dụng được xây dựng để phục vụ cho việc kinh doanh giày, bao gồm:
- Giao diện người dùng cuối: Giúp người dùng duyệt, mua giày.
- Dashboard quản trị: Cho phép admin quản lý sản phẩm, danh mục và thống kê doanh thu.
## 📦 Công nghệ sử dụng

🌐 Backend & Admin Web:
- Ngôn ngữ: Java
- Framework: Spring Boot
- ORM: JPA
- Giao diện: Thymeleaf, HTML/CSS
- Firebase
- Build tool: Maven
  
📱 Ứng dụng người dùng Android:
- Ngôn ngữ: Kotlin
- Giao diện: XML + Jetpack Compose
- Kiến trúc: MVVM
- Database: Firebase Realtime Database + Cloudinary
- Xác thực: Firebase Auth (Email, Google)
- Thanh toán: ZaloPay (sandbox), COD
- Thư viện chính:
+ Jetpack Navigation, ViewModel, LiveData
+ Glide, Coil (hiển thị ảnh)
+ Retrofit (API)
+ Gson, OkHttp
+ DotsIndicator, Accompanist Pager
+ Firebase Auth, Google Services
- ConstraintLayout + Compose UI
- ZaloPay SDK (.aar, .jar)

----------------------

## 🚀 Hướng dẫn cài đặt

### 0. Clone project

```bash
git clone https://github.com/lequochuy05/Onix.git
```
🛠️ Phần quản trị (ADMIN)

### 1. Chạy Visual Studio Code 
### 2. Mở thư mục Onix/admin-web
### 3. Trỏ tới lớp Main.java và run main
### 4. lên trang website (google chrome,..) chạy 
```bash
http://localhost:8080/admin/dashboard
```
### 5. Tại đây ta có thể thực hiện thao tác với các chức năng như thêm, cập nhật và xóa các thư mục, sản phẩm,.. và xem thống kê doanh thu qua từng tháng
![image](https://github.com/user-attachments/assets/6f8b14b4-0974-4916-94b4-62079c2c9e8c)

👤 Phần người dùng (Ứng dụng di động)

🧩 Chức năng chính

🔐 Xác thực
- Đăng ký / Đăng nhập bằng Email / Google
- Thay đổi thông tin cá nhân, mật khẩu

🛍️ Mua sắm
- Hiển thị danh sách giày theo danh mục
- Danh mục bán chạy
- Tìm kiếm theo tên, giá, kích cỡ
- Xem chi tiết sản phẩm (kích cỡ, mô tả, đánh giá)
- Giỏ hàng (thêm, xóa, sửa số lượng)
- Danh sách yêu thích
- Áp dụng mã giảm giá
  
💳 Thanh toán & Đơn hàng
- Thanh toán bằng ZaloPay, COD
- Theo dõi đơn hàng đã đặt
- Lịch sử mua hàng

💬 Hỗ trợ người dùng
- Trung tâm trợ giúp (quy trình đặt hàng,...)
- Báo lỗi & góp ý
- Hỗ trợ trực tuyến (ChatBot)
--------------------------------------

