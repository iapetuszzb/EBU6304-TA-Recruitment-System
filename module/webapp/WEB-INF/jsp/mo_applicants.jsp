<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.model.*" %>
<%@ page import="app.service.SkillMatch" %>
<%
  String error = (String) request.getAttribute("error");
  List<Job> myJobs = (List<Job>) request.getAttribute("myJobs");
  Job job = (Job) request.getAttribute("job");
  List<Application> applications = (List<Application>) request.getAttribute("applications");
  Map<String, TaProfile> profileMap = (Map<String, TaProfile>) request.getAttribute("profileMap");
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>筛选申请者</title>
  <style>
    body{font-family:Arial,Helvetica,"Microsoft YaHei",sans-serif;max-width:1100px;margin:30px auto;padding:0 16px;}
    .card{border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;margin:12px 0;}
    a.btn,button{display:inline-block;padding:8px 12px;border:1px solid #d1d5db;border-radius:8px;background:#fff;cursor:pointer;text-decoration:none;color:#111827}
    select{padding:8px 10px;border:1px solid #d1d5db;border-radius:8px;}
    table{width:100%;border-collapse:collapse}
    th,td{padding:10px;border-bottom:1px solid #eee;text-align:left;vertical-align:top}
    .muted{color:#6b7280}
    .err{color:#b91c1c}
    .pill{display:inline-block;padding:2px 8px;border-radius:999px;background:#f3f4f6;margin-left:8px;font-size:12px}
  </style>
</head>
<body>
  <h2>MO：筛选申请者</h2>

  <div class="card">
    <a class="btn" href="<%=request.getContextPath()%>/mo/post-job">发布岗位</a>
    <a class="btn" style="margin-left:10px" href="<%=request.getContextPath()%>/home">返回首页</a>
  </div>

  <% if (error != null) { %>
    <div class="card err"><%=error%></div>
  <% } %>

  <div class="card">
    <form method="get" action="<%=request.getContextPath()%>/mo/applicants">
      <div class="muted">选择你发布的岗位查看申请者</div>
      <div style="margin-top:8px;">
        <select name="jobId">
          <option value="">请选择岗位</option>
          <% if (myJobs != null) { 
               for (Job j : myJobs) { 
                 String sel = (job != null && j.getJobId().equals(job.getJobId())) ? "selected" : "";
          %>
            <option value="<%=j.getJobId()%>" <%=sel%>><%=j.getTitle()%>（<%=j.getJobId()%>）</option>
          <%   } 
             } %>
        </select>
        <button type="submit" style="margin-left:8px;">查看</button>
      </div>
    </form>
  </div>

  <% if (job != null) { %>
    <div class="card">
      <div style="font-size:18px;"><b><%=job.getTitle()%></b></div>
      <div class="muted">JobID：<%=job.getJobId()%> ｜ 要求技能：<%=job.getRequiredSkills()%></div>
    </div>

    <div class="card">
      <% if (applications == null || applications.isEmpty()) { %>
        <div class="muted">该岗位暂无申请。</div>
      <% } else { %>
        <table>
          <thead>
            <tr>
              <th>申请者(TA)</th>
              <th>技能匹配（可解释）</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
          <% for (Application a : applications) {
               TaProfile p = profileMap == null ? null : profileMap.get(a.getTaUsername().toLowerCase());
               String skills = p == null ? "" : p.getSkills();
               int score = SkillMatch.matchScore(job.getRequiredSkills(), skills);
               List<String> miss = SkillMatch.missingSkills(job.getRequiredSkills(), skills);
          %>
            <tr>
              <td>
                <div><b><%=a.getTaUsername()%></b></div>
                <% if (p != null) { %>
                  <div class="muted"><%=p.getFullName()%> ｜ <%=p.getEmail()%></div>
                <% } else { %>
                  <div class="muted">（TA 未完善档案或尚未保存）</div>
                <% } %>
              </td>
              <td>
                <div>技能：<%= skills.isEmpty() ? "（空）" : skills %></div>
                <div style="margin-top:6px;">
                  <span class="pill">匹配度：<%=score%>%</span>
                </div>
                <% if (!miss.isEmpty()) { %>
                  <div class="muted" style="margin-top:6px;">缺失：<%=String.join(", ", miss)%></div>
                <% } %>
              </td>
              <td><b><%=a.getStatus()%></b></td>
              <td>
                <form method="post" action="<%=request.getContextPath()%>/mo/applicants" style="display:inline;">
                  <input type="hidden" name="jobId" value="<%=job.getJobId()%>" />
                  <input type="hidden" name="applicationId" value="<%=a.getApplicationId()%>" />
                  <input type="hidden" name="action" value="shortlist" />
                  <button type="submit">入围</button>
                </form>
                <form method="post" action="<%=request.getContextPath()%>/mo/applicants" style="display:inline;margin-left:6px;">
                  <input type="hidden" name="jobId" value="<%=job.getJobId()%>" />
                  <input type="hidden" name="applicationId" value="<%=a.getApplicationId()%>" />
                  <input type="hidden" name="action" value="reject" />
                  <button type="submit">拒绝</button>
                </form>
              </td>
            </tr>
          <% } %>
          </tbody>
        </table>
      <% } %>
    </div>
  <% } %>
</body>
</html>

