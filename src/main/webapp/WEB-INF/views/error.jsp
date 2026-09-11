<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="vi"><head><meta charset="UTF-8"><title>Không thể xử lý yêu cầu</title></head><body><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap.min.css"><main class="container py-5"><h1>Không thể xử lý yêu cầu</h1><p>Mã lỗi: <c:out value="${status}"/>. Trang không tồn tại hoặc dữ liệu gửi lên không hợp lệ.</p><a class="btn btn-primary" href="${pageContext.request.contextPath}/admin">Về trang quản trị</a></main></body></html>
