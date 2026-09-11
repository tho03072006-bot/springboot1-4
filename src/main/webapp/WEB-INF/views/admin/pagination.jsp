<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="pager"><span>Tổng ${result.totalElements} bản ghi · Trang ${result.totalPages == 0 ? 1 : result.number + 1}/${result.totalPages == 0 ? 1 : result.totalPages}</span>
<c:if test="${result.totalPages > 1}"><nav aria-label="Phân trang"><ul class="pagination pagination-sm">
<c:if test="${result.hasPrevious()}"><c:url var="previous" value="${listPath}"><c:param name="q" value="${q}"/><c:param name="size" value="${result.size}"/><c:param name="page" value="${result.number-1}"/></c:url><li class="page-item"><a class="page-link" href="${fn:escapeXml(previous)}">Trước</a></li></c:if>
<c:forEach begin="${result.number > 2 ? result.number-2 : 0}" end="${result.number+2 < result.totalPages ? result.number+2 : result.totalPages-1}" var="p"><c:url var="pageUrl" value="${listPath}"><c:param name="q" value="${q}"/><c:param name="size" value="${result.size}"/><c:param name="page" value="${p}"/></c:url><li class="page-item ${p == result.number ? 'active' : ''}"><a class="page-link" href="${fn:escapeXml(pageUrl)}" aria-label="Trang ${p+1}">${p+1}</a></li></c:forEach>
<c:if test="${result.hasNext()}"><c:url var="next" value="${listPath}"><c:param name="q" value="${q}"/><c:param name="size" value="${result.size}"/><c:param name="page" value="${result.number+1}"/></c:url><li class="page-item"><a class="page-link" href="${fn:escapeXml(next)}">Sau</a></li></c:if>
</ul></nav></c:if></div>
