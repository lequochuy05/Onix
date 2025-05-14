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
- 
📱 Ứng dụng người dùng:
- Ngôn ngữ: Kotlin
- Kiến trúc: MVVM
- UI Layout: XML
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
Ứng dụng dành cho người mua được xây dựng bằng:
- Kotlin + XML
- Áp dụng mô hình kiến trúc MVVM
- Tính năng chính:
+ Duyệt danh mục, sản phẩm
+ Tìm kiếm và lọc giày
+ Thêm vào giỏ hàng
+ Thanh toán (nếu có tích hợp)
+ Hiển thị đánh giá và mô tả chi tiết sản phẩm
--------------------------------------

