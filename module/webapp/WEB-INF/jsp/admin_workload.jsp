<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
  Map<String, int[]> stats = (Map<String, int[]>) request.getAttribute("stats");
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>TA 整体工作量</title>
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
  <h2>管理员：TA 整体工作量（简化统计）</h2>
  <div class="card">
    <a class="btn" href="<%=request.getContextPath()%>/home">返回首页</a>
  </div>

  <div class="card">
    <div class="muted">口径：工作量 = 投递申请数（总） + 入围数（用于衡量优先分配/负载）。</div>
  </div>

  <div class="card">
    <% if (stats == null || stats.isEmpty()) { %>
      <div class="muted">暂无 TA 数据。</div>
    <% } else { %>
      <table>
        <thead>
          <tr>
            <th>TA 用户名</th>
            <th>申请数（总）</th>
            <th>入围数</th>
          </tr>
        </thead>
        <tbody>
        <% for (Map.Entry<String, int[]> e : stats.entrySet()) { %>
          <tr>
            <td><b><%=e.getKey()%></b></td>
            <td><%=e.getValue()[0]%></td>
            <td><%=e.getValue()[1]%></td>
          </tr>
        <% } %>
        </tbody>
      </table>
    <% } %>
  </div>
</body>
</html>

