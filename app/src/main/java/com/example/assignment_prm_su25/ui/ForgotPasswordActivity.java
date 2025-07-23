package com.example.assignment_prm_su25.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.assignment_prm_su25.R;
import com.example.assignment_prm_su25.data.UserDatabaseHelper;
import com.example.assignment_prm_su25.model.User;
import com.example.assignment_prm_su25.LoginActivity;
import es.dmoral.toasty.Toasty;
import java.security.SecureRandom;
import java.util.Properties;
import android.os.AsyncTask;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnSend;
    private TextView tvStatus;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edtEmail);
        btnSend = findViewById(R.id.btnSend);
        tvStatus = findViewById(R.id.tvStatus);
        dbHelper = UserDatabaseHelper.getInstance(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    showStatus(" Vui lòng nhập email của bạn");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showStatus(" Email không hợp lệ. Vui lòng kiểm tra lại!");
                } else {
                    User user = dbHelper.getUserByEmail(email);
                    if (user == null) {
                        showStatus(" Email không tồn tại trong hệ thống!");
                    } else {
                        // Hiển thị trạng thái đang xử lý
                        showStatus(" Đang xử lý yêu cầu...");
                        btnSend.setEnabled(false);
                        
                        String newPassword = generateRandomPassword();
                        boolean updated = dbHelper.updatePassword(email, newPassword);
                        
                        if (updated) {
                            // Gửi email với mật khẩu mới
                            new SendEmailTask(email, newPassword).execute();
                            // Hiển thị thông báo mật khẩu đã được cập nhật
                            showStatus(" Đã tạo mật khẩu mới và gửi đến email của bạn. Vui lòng kiểm tra email!");
                            Toasty.success(ForgotPasswordActivity.this, "Đã tạo mật khẩu mới và gửi đến email của bạn!", Toasty.LENGTH_LONG, true).show();
                            
                            // Tự động chuyển về màn hình đăng nhập sau 2 giây
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        } else {
                            showStatus(" Lỗi cập nhật mật khẩu. Vui lòng thử lại!");
                            Toasty.error(ForgotPasswordActivity.this, "Lỗi cập nhật mật khẩu!", Toasty.LENGTH_SHORT, true).show();
                        }
                        
                        // Kích hoạt lại nút sau khi xử lý xong
                        btnSend.setEnabled(true);
                    }
                }
            }
        });
    }

    // Hàm sinh mật khẩu ngẫu nhiên mạnh
    // Hiển thị trạng thái xử lý
    private void showStatus(String message) {
        tvStatus.setText(message);
        tvStatus.setVisibility(View.VISIBLE);
    }

    // Xóa trạng thái hiện tại
    private void clearStatus() {
        tvStatus.setText("");
        tvStatus.setVisibility(View.GONE);
    }

    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {
        private final String recipientEmail;
        private final String newPassword;
        private boolean success = false;

        public SendEmailTask(String recipientEmail, String newPassword) {
            this.recipientEmail = recipientEmail;
            this.newPassword = newPassword;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            final String senderEmail = "duynkhe170075@fpt.edu.vn"; // Email FPT của bạn
            final String senderPassword = "xgpy rqrg zbar qscw"; // Mật khẩu ứng dụng của bạn
            
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Khôi phục mật khẩu - PRM Assignment");
                
                String emailContent = "<h2>Chào bạn,</h2>"
                    + "<p>Chúng tôi đã nhận được yêu cầu khôi phục mật khẩu tài khoản của bạn.</p>"
                    + "<p>Mật khẩu mới của bạn là: <strong>" + newPassword + "</strong></p>"
                    + "<p>Vui lòng đăng nhập và thay đổi mật khẩu sau khi đăng nhập thành công.</p>"
                    + "<p>Trân trọng,</p>"
                    + "<p>Đội ngũ PRM Assignment</p>";
                
                message.setContent(emailContent, "text/html; charset=utf-8");
                
                Transport.send(message);
                success = true;
            } catch (MessagingException e) {
                e.printStackTrace();
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                showStatus(" Đã tạo mật khẩu mới và gửi đến email của bạn!");
                Toasty.success(ForgotPasswordActivity.this, "Đã tạo mật khẩu mới và gửi đến email của bạn!", Toasty.LENGTH_LONG, true).show();
                
                // Tự động chuyển về màn hình đăng nhập sau 2 giây
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            } else {
                showStatus(" Lỗi gửi email. Vui lòng thử lại!");
                Toasty.error(ForgotPasswordActivity.this, "Lỗi gửi email!", Toasty.LENGTH_SHORT, true).show();
            }
            
            // Kích hoạt lại nút sau khi xử lý xong
            btnSend.setEnabled(true);
        }
    }

    private void sendPasswordResetEmail(String recipientEmail, String newPassword) {
        new SendEmailTask(recipientEmail, newPassword).execute();
    }

    private String generateRandomPassword() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}