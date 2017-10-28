<%--
  Created by IntelliJ IDEA.
  User: deshani
  Date: 10/10/17
  Time: 12:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../fragments/header.html" %>
    <%@include file="../fragments/navBar.jsp" %>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="page-header">
            <h1>My Scans History</h1>
        </div>
        <h3>Dynamic Scanners</h3>
        <c:forEach items="${dynamicScanners}" var="dynamicScanner">
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="thumbnail">
                    <table class="table table-bordered table-striped table-hover">
                        <tbody>
                        <tr>
                            <th>Created Time</th>
                            <td>${dynamicScanner.getCreatedTime()}</td>
                        </tr>
                            <%--<tr>--%>
                            <%--<th>Product Extracted Status</th>--%>
                            <%--<td>${dynamicScanner.isFindSecBugs()}</td>--%>
                            <%--</tr>--%>
                            <%--<tr>--%>
                            <%--<th>Product Extracted Time</th>--%>
                            <%--<td>${dynamicScanner.getFileExtractedTime()}</td>--%>
                            <%--</tr>--%>
                        <tr>
                            <th>Scan Progress</th>
                            <td>
                                <div class="progress">
                                    <div class="progress-bar progress-bar-success" role="progressbar"
                                         aria-valuenow="${dynamicScanner.getZapScanProgress()}" aria-valuemax="100"
                                         aria-valuemin="0" style="width: ${dynamicScanner.getZapScanProgress()}%;">

                                    </div>
                                </div>
                            </td>
                        </tr>

                        </tbody>
                    </table>
                </div>
            </div>
        </c:forEach>
        <h3>Static Scanners</h3>
        <c:choose>
            <c:when test="${staticScanners!=null}">
                <c:forEach begin="0" end="${staticScanners.length()-1}" var="index">
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="thumbnail">
                            <table class="table table-bordered table-striped table-hover">
                                <tbody>
                                <tr>
                                    <th>Name</th>
                                    <td>${staticScanners.getJSONObject(index).getString("name")}</td>
                                </tr>
                                <tr>
                                    <th>Created Time</th>
                                    <td>${staticScanners.getJSONObject(index).getString("createdTime")}</td>
                                </tr>
                                <tr>
                                    <th>Product Extracted Status</th>
                                    <td>${staticScanners.getJSONObject(index).getBoolean("fileExtracted")}</td>
                                </tr>
                                <tr>
                                    <th>Product Extracted Time</th>
                                    <td>${staticScanners.getJSONObject(index).getString("fileExtractedTime")}</td>
                                </tr>
                                <tr>
                                    <th>Product Cloned Status</th>
                                    <td>${staticScanners.getJSONObject(index).getBoolean("productCloned")}</td>
                                </tr>
                                    <%--<tr>--%>
                                    <%--<th>Product Cloned Time</th>--%>
                                    <%--<td>${staticScanners.getJSONObject(index).getString("productClonedTime")}</td>--%>
                                    <%--</tr>--%>
                                <tr>
                                    <th>FindSecBugs Status</th>
                                    <td>${staticScanners.getJSONObject(index).getString("findSecBugsStatus")}</td>
                                </tr>

                                <tr>
                                    <th>Dependency Check Status</th>
                                    <td>${staticScanners.getJSONObject(index).getString("dependencyCheckStatus")}</td>
                                </tr>
                                <tr>
                                    <th>Report Sent Status</th>
                                    <td>${staticScanners.getJSONObject(index).getBoolean("reportSent")}</td>
                                </tr>

                                <c:if test="${!staticScanners.getJSONObject(index).isNull('reportSentTime')}">
                                    <tr>
                                        <th>Report Sent Time</th>
                                        <td>${staticScanners.getJSONObject(index).getString("reportSentTime")}</td>
                                    </tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>No static scans are found</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@include file="../fragments/footer.jsp" %>

</body>
</html>