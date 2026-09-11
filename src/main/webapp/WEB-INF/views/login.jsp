<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="vi"><head><meta charset="UTF-8"><title>Đăng nhập</title></head><body>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap.min.css"><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin.css"><meta name="viewport" content="width=device-width,initial-scale=1">
<div class="login-page"><main class="login-card"><div class="brand"><span class="brand-icon">S</span> Spring Admin</div><p class="eyebrow">Chào mừng trở lại</p><h1>Đăng nhập quản trị</h1><p class="help">Đăng nhập để quản lý danh mục và người dùng.</p>
<c:if test="${param.error != null}"><div class="alert alert-danger mt-3">Tên đăng nhập hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa.</div></c:if>
<c:if test="${param.logout != null}"><div class="alert alert-success mt-3">Bạn đã đăng xuất.</div></c:if>
<form method="post" action="${pageContext.request.contextPath}/login"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"><label class="form-label" for="username">Tên đăng nhập</label><input id="username" name="username" class="form-control" required maxlength="50" autocomplete="username" autofocus>
<label class="form-label" for="password">Mật khẩu</label><input id="password" type="password" name="password" class="form-control" required autocomplete="current-password"><button class="btn btn-primary">Đăng nhập</button></form></main></div>
</body></html>
