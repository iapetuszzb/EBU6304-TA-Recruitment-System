<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="app.model.TaProfile" %>
<%
  TaProfile p = (TaProfile) request.getAttribute("profile");
  String fullName = p == null ? "" : p.getFullName();
  String email = p == null ? "" : p.getEmail();
  String skills = p == null ? "" : p.getSkills();
  String resumePath = p == null ? "" : p.getResumePath();
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>TA 申请档案</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:900px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;margin:12px 0;}
    input{width:100%;padding:10px;border:1px solid #d1d5db;border-radius:8px;}
    button,a.btn{display:inline-block;padding:8px 12px;border:1px solid #d1d5db;border-radius:8px;background:#fff;cursor:pointer;text-decoration:none;color:#111827}
    .muted{color:#6b7280}
  </style>
</head>
<body>
  <h2>TA 申请档案</h2>
  <div class="card">
    <form method="post" action="<%=request.getContextPath()%>/ta/profile" enctype="multipart/form-data">
      <div style="margin:10px 0;">
        <div>姓名</div>
        <input name="fullName" value="<%=fullName%>" placeholder="你的姓名" />
      </div>
      <div style="margin:10px 0;">
        <div>Email</div>
        <input name="email" value="<%=email%>" placeholder="name@example.com" />
      </div>
      <div style="margin:10px 0;">
        <div>技能（用英文逗号分隔）</div>
        <input name="skills" value="<%=skills%>" placeholder="java, servlet, 数据结构" />
        <div class="muted" style="margin-top:6px;">这些技能会用于岗位匹配度与缺失技能提示（可解释）。</div>
      </div>
      <div style="margin:10px 0;">
        <div>上传简历（可选，支持任意文件）</div>
        <input name="resume" type="file" />
        <% if (resumePath != null && !resumePath.isEmpty()) { %>
          <div class="muted" style="margin-top:6px;">已上传：<%=resumePath%></div>
        <% } %>
      </div>
      <button type="submit">保存</button>
      <a class="btn" style="margin-left:10px" href="<%=request.getContextPath()%>/home">返回首页</a>
    </form>
  </div>
</body>
</html>

