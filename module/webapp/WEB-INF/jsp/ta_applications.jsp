<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.model.*" %>
<%
  List<Application> applications = (List<Application>) request.getAttribute("applications");
  Map<String, Job> jobMap = (Map<String, Job>) request.getAttribute("jobMap");
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>我的申请</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:900px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;margin:12px 0;}
    a.btn{display:inline-block;padding:8px 12px;border:1px solid #d1d5db;border-radius:8px;background:#fff;cursor:pointer;text-decoration:none;color:#111827}
    table{width:100%;border-collapse:collapse}
    th,td{padding:10px;border-bottom:1px solid #eee;text-align:left;vertical-align:top}
    .muted{color:#6b7280}
  </style>
</head>
<body>
  <h2>我的申请状态</h2>
  <div class="card">
    <a class="btn" href="<%=request.getContextPath()%>/jobs">继续找岗位</a>
    <a class="btn" style="margin-left:10px" href="<%=request.getContextPath()%>/home">返回首页</a>
  </div>

  <div class="card">
    <% if (applications == null || applications.isEmpty()) { %>
      <div>你还没有投递任何岗位。</div>
    <% } else { %>
      <table>
        <thead>
          <tr>
            <th>岗位</th>
            <th>状态</th>
            <th>投递时间</th>
          </tr>
        </thead>
        <tbody>
        <% for (Application a : applications) {
             Job j = jobMap == null ? null : jobMap.get(a.getJobId());
        %>
          <tr>
            <td>
              <div><b><%= j == null ? a.getJobId() : j.getTitle() %></b></div>
              <div class="muted">JobID：<%=a.getJobId()%></div>
            </td>
            <td><b><%=a.getStatus()%></b></td>
            <td class="muted"><%=a.getCreatedAtIso()%></td>
          </tr>
        <% } %>
        </tbody>
      </table>
    <% } %>
  </div>
</body>
</html>

