<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String error = (String) request.getAttribute("error");
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>登录</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:720px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:12px;padding:16px 18px;margin:12px 0;background:#fff;}
    .row{display:flex;gap:12px;flex-wrap:wrap}
    .col{flex:1;min-width:240px}
    input{width:100%;padding:10px;border:1px solid #d1d5db;border-radius:10px;}
    button,a.btn{display:inline-block;padding:10px 12px;border:1px solid #d1d5db;border-radius:10px;background:#111827;color:#fff;cursor:pointer;text-decoration:none}
    a.link{color:#2563eb;text-decoration:none}
    .err{color:#b91c1c}
    .muted{color:#6b7280}
  </style>
</head>
<body>
  <h2>登录</h2>
  <div class="card">
    <% if (error != null) { %>
      <div class="err"><%=error%></div>
    <% } %>
    <form method="post" action="<%=request.getContextPath()%>/login">
      <div class="row">
        <div class="col">
          <div>用户名</div>
          <input name="username" placeholder="例如：你的用户名" />
        </div>
        <div class="col">
          <div>密码</div>
          <input name="password" type="password" />
        </div>
      </div>
      <div style="margin-top:12px;">
        <button type="submit">登录</button>
        <a class="btn" style="margin-left:10px;background:#fff;color:#111827" href="<%=request.getContextPath()%>/register">去注册</a>
      </div>
      <div class="muted" style="margin-top:10px;">
        管理员账号为系统预置：admin / admin
      </div>
    </form>
  </div>
  <div class="card">
    <a class="link" href="<%=request.getContextPath()%>/home">返回首页</a>
  </div>
</body>
</html>

