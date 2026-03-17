<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.model.Job" %>
<%
  String error = (String) request.getAttribute("error");
  List<Job> myJobs = (List<Job>) request.getAttribute("myJobs");
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>发布岗位</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:900px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;margin:12px 0;}
    input,textarea{width:100%;padding:10px;border:1px solid #d1d5db;border-radius:8px;}
    textarea{min-height:110px;resize:vertical}
    button,a.btn{display:inline-block;padding:8px 12px;border:1px solid #d1d5db;border-radius:8px;background:#fff;cursor:pointer;text-decoration:none;color:#111827}
    .muted{color:#6b7280}
    .err{color:#b91c1c}
    table{width:100%;border-collapse:collapse}
    th,td{padding:10px;border-bottom:1px solid #eee;text-align:left;vertical-align:top}
  </style>
</head>
<body>
  <h2>MO：发布招聘岗位</h2>
  <div class="card">
    <% if (error != null) { %>
      <div class="err"><%=error%></div>
    <% } %>
    <form method="post" action="<%=request.getContextPath()%>/mo/post-job">
      <div style="margin:10px 0;">
        <div>岗位标题</div>
        <input name="title" placeholder="例如：数据结构 TA" />
      </div>
      <div style="margin:10px 0;">
        <div>岗位描述</div>
        <textarea name="description" placeholder="职责/时间/地点/要求等"></textarea>
      </div>
      <div style="margin:10px 0;">
        <div>要求技能（逗号分隔，可选）</div>
        <input name="requiredSkills" placeholder="java, 算法, 沟通" />
        <div class="muted" style="margin-top:6px;">这些技能会用于匹配度与缺失技能提示（可解释）。</div>
      </div>
      <button type="submit">发布</button>
      <a class="btn" style="margin-left:10px" href="<%=request.getContextPath()%>/home">返回首页</a>
      <a class="btn" style="margin-left:10px" href="<%=request.getContextPath()%>/mo/applicants">去筛选申请者</a>
    </form>
  </div>

  <div class="card">
    <h3>我发布的岗位</h3>
    <% if (myJobs == null || myJobs.isEmpty()) { %>
      <div class="muted">你还没有发布任何岗位。</div>
    <% } else { %>
      <table>
        <thead>
          <tr><th>标题</th><th>JobID</th><th>状态</th><th>操作</th></tr>
        </thead>
        <tbody>
        <% for (Job j : myJobs) { %>
          <tr>
            <td><b><%=j.getTitle()%></b></td>
            <td class="muted"><%=j.getJobId()%></td>
            <td><%= j.isOpen() ? "OPEN" : "CLOSED" %></td>
            <td><a class="btn" href="<%=request.getContextPath()%>/mo/applicants?jobId=<%=j.getJobId()%>">查看申请者</a></td>
          </tr>
        <% } %>
        </tbody>
      </table>
    <% } %>
  </div>
</body>
</html>

