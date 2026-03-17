<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="app.model.Role" %>
<%
  String username = (String) request.getAttribute("username");
  Role role = (Role) request.getAttribute("role");
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>TA 招聘系统</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:900px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;margin:12px 0;}
    a{color:#2563eb;text-decoration:none}
    a:hover{text-decoration:underline}
    .muted{color:#6b7280}
    .btn{display:inline-block;padding:8px 12px;border:1px solid #d1d5db;border-radius:8px;background:#fff}
  </style>
</head>
<body>
  <h2>TA 招聘全流程管理系统</h2>

  <div class="card">
    <% if (username == null) { %>
      <div>你尚未登录。</div>
      <div style="margin-top:10px;">
        <a class="btn" href="<%=request.getContextPath()%>/login">登录</a>
        <a class="btn" style="margin-left:10px" href="<%=request.getContextPath()%>/register">注册（TA/MO）</a>
      </div>
    <% } else { %>
      <div>已登录：<b><%=username%></b>（角色：<b><%=role%></b>）</div>
      <div style="margin-top:10px;">
        <a class="btn" href="<%=request.getContextPath()%>/logout">退出登录</a>
      </div>
    <% } %>
    <div class="muted" style="margin-top:8px;">提示：管理员账号为系统预置：admin / admin；TA/MO 通过注册创建。</div>
  </div>

  <div class="card">
    <h3>公共入口</h3>
    <ul>
      <li><a href="<%=request.getContextPath()%>/jobs">岗位列表 / 搜索</a></li>
    </ul>
  </div>

  <div class="card">
    <h3>TA 功能</h3>
    <ul>
      <% if (username == null) { %>
        <li class="muted">请先登录 TA 账号后使用。</li>
      <% } else if (role != Role.TA) { %>
        <li class="muted">当前非 TA 角色。</li>
      <% } %>
      <li><a href="<%=request.getContextPath()%>/ta/profile">创建/维护申请档案 + 上传简历</a></li>
      <li><a href="<%=request.getContextPath()%>/ta/applications">我的申请状态</a></li>
    </ul>
  </div>

  <div class="card">
    <h3>MO 功能</h3>
    <ul>
      <% if (username == null) { %>
        <li class="muted">请先登录 MO 账号后使用。</li>
      <% } else if (role != Role.MO) { %>
        <li class="muted">当前非 MO 角色。</li>
      <% } %>
      <li><a href="<%=request.getContextPath()%>/mo/post-job">发布招聘岗位</a></li>
      <li><a href="<%=request.getContextPath()%>/mo/applicants">筛选申请者</a></li>
    </ul>
  </div>

  <div class="card">
    <h3>管理员功能</h3>
    <ul>
      <% if (username == null) { %>
        <li class="muted">请先登录管理员账号后使用。</li>
      <% } else if (role != Role.ADMIN) { %>
        <li class="muted">当前非管理员角色。</li>
      <% } %>
      <li><a href="<%=request.getContextPath()%>/admin/workload">查看 TA 整体工作量</a></li>
    </ul>
  </div>
</body>
</html>

