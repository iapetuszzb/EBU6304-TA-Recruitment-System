<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String error = (String) request.getAttribute("error");
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>注册</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:720px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:12px;padding:16px 18px;margin:12px 0;background:#fff;}
    .row{display:flex;gap:12px;flex-wrap:wrap}
    .col{flex:1;min-width:220px}
    input,select{width:100%;padding:10px;border:1px solid #d1d5db;border-radius:10px;}
    button,a.btn{display:inline-block;padding:10px 12px;border:1px solid #d1d5db;border-radius:10px;background:#111827;color:#fff;cursor:pointer;text-decoration:none}
    a.link{color:#2563eb;text-decoration:none}
    .muted{color:#6b7280}
    .err{color:#b91c1c}
  </style>
</head>
<body>
  <h2>注册（TA / MO）</h2>
  <div class="card">
    <% if (error != null) { %>
      <div class="err"><%=error%></div>
    <% } %>
    <form method="post" action="<%=request.getContextPath()%>/register">
      <div class="row">
        <div class="col">
          <div>用户名</div>
          <input name="username" placeholder="3-20 位：字母/数字/下划线" />
        </div>
        <div class="col">
          <div>注册角色</div>
          <select name="role">
            <option value="TA">TA（申请者）</option>
            <option value="MO">MO（模块组织者）</option>
          </select>
          <div class="muted" style="margin-top:6px;">管理员账号由系统预置，不能注册。</div>
        </div>
      </div>
      <div class="row" style="margin-top:10px;">
        <div class="col">
          <div>密码</div>
          <input name="password" type="password" />
        </div>
        <div class="col">
          <div>确认密码</div>
          <input name="password2" type="password" />
        </div>
      </div>
      <div style="margin-top:12px;">
        <button type="submit">创建账号</button>
        <a class="btn" style="margin-left:10px;background:#fff;color:#111827" href="<%=request.getContextPath()%>/login">去登录</a>
      </div>
      <div class="muted" style="margin-top:10px;">说明：密码会以哈希形式保存到纯文本 `users.csv`，不会明文落盘。</div>
    </form>
  </div>
  <div class="card">
    <a class="link" href="<%=request.getContextPath()%>/home">返回首页</a>
  </div>
</body>
</html>

