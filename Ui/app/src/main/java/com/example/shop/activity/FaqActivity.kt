package com.example.shop.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.FaqAdapter
import com.example.shop.databinding.ActivityFaqBinding
import com.example.shop.model.FaqModel

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val faqTitle = intent.getStringExtra("title") ?: "Câu hỏi thường gặp"
        binding.tvFaqTitle.text = faqTitle

        val faqList = when (faqTitle) {
            "Vấn đề về đơn hàng" -> listOf(
                FaqModel("Làm sao để xem lịch sử đơn hàng?", "Vào mục 'Theo dõi đơn hàng' trong Cài đặt để kiểm tra các đơn đã đặt."),
                FaqModel("Tại sao đơn hàng của tôi bị huỷ?", "Có thể do thanh toán không thành công hoặc thông tin giao hàng không hợp lệ."),
                FaqModel("Làm thế nào để huỷ đơn hàng?", "Bạn có thể huỷ đơn hàng trong vòng 1 giờ sau khi đặt tại mục Theo dõi đơn hàng."),
                FaqModel("Tôi có thể chỉnh sửa đơn hàng sau khi đặt không?", "Rất tiếc, bạn không thể chỉnh sửa đơn hàng sau khi đã đặt."),
                FaqModel("Có thể đặt nhiều sản phẩm trong một đơn không?", "Dĩ nhiên, bạn có thể thêm nhiều sản phẩm vào giỏ hàng và thanh toán một lần."),
                FaqModel("Tôi không nhận được email xác nhận đơn hàng?", "Hãy kiểm tra hộp thư rác hoặc liên hệ CSKH nếu không thấy trong vài phút.")
            )
            "Giao hàng & vận chuyển" -> listOf(
                FaqModel("Bao lâu tôi nhận được hàng?", "Thời gian giao hàng từ 3–5 ngày làm việc tùy khu vực."),
                FaqModel("Tôi có thể chọn giờ giao hàng không?", "Hiện tại chưa hỗ trợ chọn giờ, nhưng bạn sẽ được thông báo trước khi giao."),
                FaqModel("Làm sao để theo dõi đơn hàng?", "Vào mục 'Theo dõi đơn hàng' để xem tiến trình giao hàng."),
                FaqModel("Phí giao hàng là bao nhiêu?", "Miễn phí cho đơn trên 500.000 VND, các đơn khác tính 20.000 VND."),
                FaqModel("Có thể thay đổi địa chỉ giao hàng sau khi đặt không?", "Không, địa chỉ giao hàng không thể thay đổi sau khi xác nhận đơn."),
                FaqModel("Bưu tá giao không được thì sao?", "Bưu tá sẽ liên hệ lại 1-2 lần, sau đó đơn hàng sẽ được hoàn lại kho.")
            )
            "Thanh toán & hoàn tiền" -> listOf(
                FaqModel("Những hình thức thanh toán nào được hỗ trợ?", "Chúng tôi hỗ trợ COD, ZaloPay, VnPay và thẻ ngân hàng."),
                FaqModel("Thanh toán không thành công, tôi cần làm gì?", "Thử lại hoặc sử dụng hình thức khác, kiểm tra số dư tài khoản."),
                FaqModel("Tôi có thể trả góp không?", "Hiện tại chưa hỗ trợ trả góp, nhưng chúng tôi đang phát triển tính năng này."),
                FaqModel("Làm sao để được hoàn tiền?", "Khi đơn bị huỷ hoặc trả hàng thành công, tiền sẽ hoàn trong 3–5 ngày."),
                FaqModel("Tôi có bị mất phí khi huỷ đơn không?", "Không, bạn không bị mất phí nếu huỷ đơn đúng quy định."),
                FaqModel("Thanh toán có an toàn không?", "Mọi thanh toán đều được mã hóa theo tiêu chuẩn PCI DSS.")
            )
            "Đổi trả & bảo hành" -> listOf(
                FaqModel("Tôi có thể đổi sản phẩm không?", "Có, trong vòng 7 ngày nếu sản phẩm chưa sử dụng và còn nguyên bao bì."),
                FaqModel("Chính sách bảo hành như thế nào?", "Bảo hành 6 tháng với lỗi sản xuất. Không áp dụng với hao mòn tự nhiên."),
                FaqModel("Tôi muốn trả hàng thì làm thế nào?", "Liên hệ CSKH để được hướng dẫn trả hàng miễn phí."),
                FaqModel("Bao lâu tôi nhận được tiền hoàn trả?", "3–5 ngày làm việc sau khi đơn được xác nhận hoàn trả."),
                FaqModel("Tôi có cần giữ hóa đơn khi đổi trả không?", "Có, hóa đơn là điều kiện bắt buộc để xử lý đổi/trả."),
                FaqModel("Sản phẩm lỗi do nhà sản xuất thì sao?", "Chúng tôi hỗ trợ đổi mới hoặc hoàn tiền đầy đủ.")
            )
            "Tài khoản & bảo mật" -> listOf(
                FaqModel("Tôi quên mật khẩu, phải làm sao?", "Dùng tính năng 'Quên mật khẩu' trên màn hình đăng nhập để khôi phục."),
                FaqModel("Làm sao để thay đổi email hoặc SĐT?", "Truy cập hồ sơ cá nhân và chỉnh sửa thông tin liên hệ."),
                FaqModel("Tôi nghi ngờ tài khoản bị xâm nhập?", "Đổi mật khẩu ngay và liên hệ bộ phận bảo mật của chúng tôi."),
                FaqModel("Dữ liệu cá nhân của tôi có được bảo mật không?", "Chúng tôi tuân thủ GDPR và cam kết bảo vệ thông tin người dùng."),
                FaqModel("Tôi có thể xóa tài khoản không?", "Bạn có thể yêu cầu xóa tài khoản qua CSKH nếu không muốn sử dụng nữa."),
                FaqModel("Ứng dụng có xác thực hai yếu tố không?", "Hiện tại chưa hỗ trợ nhưng sẽ cập nhật trong bản tới.")
            )
            "Khuyến mãi & voucher" -> listOf(
                FaqModel("Làm sao để sử dụng mã giảm giá?", "Nhập mã tại trang thanh toán, phần 'Áp dụng mã khuyến mãi'."),
                FaqModel("Tôi có thể dùng nhiều mã không?", "Mỗi đơn chỉ áp dụng được 1 mã giảm giá."),
                FaqModel("Mã khuyến mãi không hoạt động?", "Kiểm tra điều kiện áp dụng và thời hạn mã."),
                FaqModel("Tôi có thể chia sẻ mã cho bạn bè không?", "Có, miễn là mã chưa dùng và chưa hết hạn."),
                FaqModel("Voucher hoàn tiền dùng như thế nào?", "Tự động áp dụng vào đơn tiếp theo sau khi hoàn tất đơn hàng hiện tại."),
                FaqModel("Tôi có thể xem lịch sử khuyến mãi không?", "Tính năng này đang phát triển, vui lòng theo dõi cập nhật mới.")
            )
            "Vấn đề về ứng dụng" -> listOf(
                FaqModel("App bị lỗi khi mở?", "Thử xoá bộ nhớ cache hoặc cập nhật phiên bản mới nhất."),
                FaqModel("Tôi không nhận được thông báo?", "Hãy kiểm tra quyền thông báo trong cài đặt điện thoại."),
                FaqModel("Không thêm được sản phẩm vào giỏ?", "Đảm bảo bạn chọn size sản phẩm trước khi thêm vào giỏ."),
                FaqModel("App bị treo giữa chừng?", "Gỡ cài đặt và cài lại ứng dụng từ CH Play hoặc App Store."),
                FaqModel("Giao diện hiển thị sai?", "Kiểm tra cập nhật hệ điều hành và ứng dụng."),
                FaqModel("Lỗi thanh toán trong app?", "Thử lại hoặc chọn phương thức thanh toán khác.")
            )
            "Liên hệ & hỗ trợ" -> listOf(
                FaqModel("Làm sao để liên hệ CSKH?", "Gọi 0706163387 hoặc nhắn tin qua fanpage chính thức."),
                FaqModel("Giờ làm việc của tổng đài?", "Từ 8h–22h tất cả các ngày trong tuần."),
                FaqModel("Tôi muốn góp ý, làm sao?", "Gửi email về lehuy2425@gmail.com để góp ý."),
                FaqModel("Có thể gọi video với nhân viên không?", "Hiện tại chưa hỗ trợ tính năng này."),
                FaqModel("Tôi có thể đến cửa hàng trực tiếp không?", "Chúng tôi là nền tảng online, chưa có cửa hàng vật lý."),
                FaqModel("CSKH phản hồi bao lâu?", "Thường từ 15–30 phút qua kênh chat hoặc 24h qua email.")
            )
            else -> listOf(
                FaqModel("Tính năng đang được cập nhật", "Vui lòng quay lại sau."),
                FaqModel("Liên hệ hỗ trợ để được tư vấn trực tiếp", "Gọi 0706163387 hoặc nhắn tin fanpage.")
            )
        }

        val adapter = FaqAdapter(faqList)
        binding.recyclerViewFaq.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFaq.adapter = adapter

        binding.backBtn.setOnClickListener { finish() }
    }
}
