<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.model.Job" %>
<%@ page import="app.service.SkillMatch" %>
<%
  String q = (String) request.getAttribute("q");
  List<Job> jobs = (List<Job>) request.getAttribute("jobs");
  String taSkills = (String) request.getAttribute("taSkills"); // may be null
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>岗位列表</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:900px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;margin:12px 0;}
    input{width:100%;padding:10px;border:1px solid #d1d5db;border-radius:8px;}
    button,a.btn{display:inline-block;padding:8px 12px;border:1px solid #d1d5db;border-radius:8px;background:#fff;cursor:pointer;text-decoration:none;color:#111827}
    .muted{color:#6b7280}
    .pill{display:inline-block;padding:2px 8px;border-radius:999px;background:#f3f4f6;margin-left:8px;font-size:12px}
  </style>
</head>
<body>
  <h2>岗位列表</h2>
  <div class="card">
    <form method="get" action="<%=request.getContextPath()%>/jobs">
      <div class="muted">按关键字搜索（标题/描述/技能）</div>
      <div style="margin-top:8px;">
        <input name="q" value="<%=q == null ? "" : q%>" placeholder="例如 Java / 数据结构 / Web" />
      </div>
      <div style="margin-top:10px;">
        <button type="submit">搜索</button>
        <a class="btn" href="<%=request.getContextPath()%>/home">返回首页</a>
      </div>
      <% if (taSkills != null && !taSkills.trim().isEmpty()) { %>
        <div class="muted" style="margin-top:10px;">已读取你的技能（用于可解释匹配）：<%=taSkills%></div>
      <% } %>
    </form>
  </div>

  <% if (jobs == null || jobs.isEmpty()) { %>
    <div class="card">暂无岗位（或搜索无结果）。</div>
  <% } else { %>
    <% for (Job j : jobs) { %>
      <div class="card">
        <div style="display:flex;justify-content:space-between;gap:10px;align-items:flex-start;">
          <div>
            <div style="font-size:18px;"><b><%=j.getTitle()%></b></div>
            <div class="muted">岗位ID：<%=j.getJobId()%> ｜ 发布者(MO)：<%=j.getMoUsername()%></div>
          </div>
          <div>
            <a class="btn" href="<%=request.getContextPath()%>/ta/apply?jobId=<%=j.getJobId()%>">投递申请</a>
          </div>
        </div>
        <div style="margin-top:10px;"><%=j.getDescription()%></div>
        <div style="margin-top:10px;">
          <span class="muted">要求技能：</span><%=j.getRequiredSkills()%>
          <% if (taSkills != null) { 
               int score = SkillMatch.matchScore(j.getRequiredSkills(), taSkills);
               List<String> miss = SkillMatch.missingSkills(j.getRequiredSkills(), taSkills);
          %>
            <span class="pill">匹配度：<%=score%>%</span>
            <% if (!miss.isEmpty()) { %>
              <div class="muted" style="margin-top:6px;">缺失技能（可解释）：<%=String.join(", ", miss)%></div>
            <% } %>
          <% } %>
        </div>
      </div>
    <% } %>
  <% } %>
</body>
</html>

