<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="vi"><head><meta charset="UTF-8"><title>Tổng quan</title></head><body>
<div class="page-head"><div><p class="eyebrow">Dashboard</p><h1>Tổng quan quản trị</h1><p>Theo dõi dữ liệu và truy cập nhanh các chức năng quản lý.</p></div></div>
<div class="row g-4"><div class="col-md-6"><section class="metric"><div class="metric-label">TỔNG DANH MỤC</div><div class="number">${categoryCount}</div><a href="${pageContext.request.contextPath}/admin/categories">Quản lý danh mục →</a></section></div>
<div class="col-md-6"><section class="metric"><div class="metric-label">TỔNG NGƯỜI DÙNG</div><div class="number">${userCount}</div><a href="${pageContext.request.contextPath}/admin/users">Quản lý người dùng →</a></section></div></div>
<section class="panel mt-4"><h2>Thao tác nhanh</h2><div class="row g-3"><div class="col-md-6"><a class="quick-link" href="${pageContext.request.contextPath}/admin/categories/new"><strong>+ Thêm danh mục</strong><span>Tạo danh mục mới và thiết lập trạng thái hiển thị.</span></a></div><div class="col-md-6"><a class="quick-link" href="${pageContext.request.contextPath}/admin/users/new"><strong>+ Thêm người dùng</strong><span>Tạo tài khoản và phân quyền truy cập hệ thống.</span></a></div></div></section>
</body></html>
