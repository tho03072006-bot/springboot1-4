<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="vi"><head><meta charset="UTF-8"><title>Tài khoản người dùng</title></head><body><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap.min.css"><main class="container py-5"><h1>Tài khoản người dùng</h1><p>Bạn đã đăng nhập với quyền User. Khu vực quản trị chỉ dành cho Admin.</p><form method="post" action="${pageContext.request.contextPath}/logout"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"><button class="btn btn-outline-secondary">Đăng xuất</button></form></main></body></html>
