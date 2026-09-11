<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title><sitemesh:write property="title"/> · Quản trị</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin.css">
<sitemesh:write property="head"/></head><body>
<div class="admin-shell" data-layout="admin">
<aside class="sidebar"><a class="brand" href="${pageContext.request.contextPath}/admin"><span class="brand-icon">S</span> Spring Admin</a>
<div class="nav-caption">KHÔNG GIAN QUẢN TRỊ</div>
<nav aria-label="Điều hướng chính"><a href="${pageContext.request.contextPath}/admin">Tổng quan</a><a href="${pageContext.request.contextPath}/admin/categories">Danh mục</a><a href="${pageContext.request.contextPath}/admin/users">Người dùng</a></nav>
<div class="sidebar-bottom">Bài thực hành Spring Boot 4<br><span>Category &amp; User Management</span></div></aside>
<div class="workspace"><header class="topbar"><span class="text-muted">Hệ thống quản lý</span><div class="d-flex align-items-center gap-3"><span class="badge text-bg-success">ADMIN</span><strong><c:out value="${pageContext.request.userPrincipal.name}"/></strong><form method="post" action="${pageContext.request.contextPath}/logout"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"><button class="btn btn-sm btn-outline-secondary">Đăng xuất</button></form></div></header>
<main class="content"><c:if test="${not empty success}"><div class="alert alert-success" role="status"><c:out value="${success}"/></div></c:if>
<c:if test="${not empty failure}"><div class="alert alert-danger" role="alert"><c:out value="${failure}"/></div></c:if>
<sitemesh:write property="body"/></main><footer class="footer">Spring Admin <span>Quản lý dữ liệu tập trung</span></footer></div></div>
<script src="${pageContext.request.contextPath}/assets/admin.js" defer></script></body></html>
