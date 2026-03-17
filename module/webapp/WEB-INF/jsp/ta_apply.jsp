<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.model.*" %>
<%
  String error = (String) request.getAttribute("error");
  Job job = (Job) request.getAttribute("job");
  TaProfile profile = (TaProfile) request.getAttribute("profile");
  Application existing = (Application) request.getAttribute("existing");
  Integer matchScore = (Integer) request.getAttribute("matchScore");
  List<String> missingSkills = (List<String>) request.getAttribute("missingSkills");
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>投递申请</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:900px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;margin:12px 0;}
    button,a.btn{display:inline-block;padding:8px 12px;border:1px solid #d1d5db;border-radius:8px;background:#fff;cursor:pointer;text-decoration:none;color:#111827}
    .muted{color:#6b7280}
    .err{color:#b91c1c}
    .pill{display:inline-block;padding:2px 8px;border-radius:999px;background:#f3f4f6;margin-left:8px;font-size:12px}
  </style>
</head>
<body>
  <h2>投递申请</h2>

  <% if (error != null) { %>
    <div class="card err"><%=error%></div>
  <% } %>

  <% if (job != null) { %>
    <div class="card">
      <div style="font-size:18px;"><b><%=job.getTitle()%></b></div>
      <div class="muted">岗位ID：<%=job.getJobId()%> ｜ MO：<%=job.getMoUsername()%></div>
      <div style="margin-top:10px;"><%=job.getDescription()%></div>
      <div style="margin-top:10px;"><span class="muted">要求技能：</span><%=job.getRequiredSkills()%></div>
      <% if (matchScore != null) { %>
        <div style="margin-top:8px;">
          <span class="pill">匹配度：<%=matchScore%>%</span>
          <% if (missingSkills != null && !missingSkills.isEmpty()) { %>
            <div class="muted" style="margin-top:6px;">缺失技能（可解释）：<%=String.join(", ", missingSkills)%></div>
          <% } %>
        </div>
      <% } %>
    </div>
  <% } %>

  <div class="card">
    <h3>你的申请档案检查</h3>
    <% if (profile == null) { %>
      <div class="err">你还没有创建申请档案，请先去完善并上传简历。</div>
      <div style="margin-top:10px;">
        <a class="btn" href="<%=request.getContextPath()%>/ta/profile">去完善档案</a>
      </div>
    <% } else { %>
      <div>姓名：<b><%=profile.getFullName()%></b> ｜ Email：<b><%=profile.getEmail()%></b></div>
      <div style="margin-top:6px;">技能：<b><%=profile.getSkills()%></b></div>
      <div style="margin-top:6px;">简历：<b><%= (profile.getResumePath()==null||profile.getResumePath().isEmpty()) ? "未上传" : profile.getResumePath() %></b></div>
      <% if (existing != null) { %>
        <div class="muted" style="margin-top:10px;">你已投递过该岗位，当前状态：<b><%=existing.getStatus()%></b></div>
        <div style="margin-top:10px;">
          <a class="btn" href="<%=request.getContextPath()%>/ta/applications">查看我的申请</a>
        </div>
      <% } else { %>
        <form method="post" action="<%=request.getContextPath()%>/ta/apply" style="margin-top:10px;">
          <input type="hidden" name="jobId" value="<%=job==null ? "" : job.getJobId()%>" />
          <% if (profile.getResumePath()==null || profile.getResumePath().isEmpty()) { %>
            <div class="err">未上传简历，不能投递。请先去上传。</div>
            <div style="margin-top:10px;">
              <a class="btn" href="<%=request.getContextPath()%>/ta/profile">去上传简历</a>
            </div>
          <% } else { %>
            <button type="submit">确认投递</button>
            <a class="btn" style="margin-left:10px" href="<%=request.getContextPath()%>/jobs">返回岗位列表</a>
          <% } %>
        </form>
      <% } %>
    <% } %>
  </div>

  <div class="card">
    <a class="btn" href="<%=request.getContextPath()%>/home">返回首页</a>
  </div>
</body>
</html>

