<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- <title>AI에게 질문해보세요!</title> --%>
    <title><%= request.getAttribute("title")%></title>
</head>
<body>
    <form method="post">
        <input name="text" placeholder="질문하고 싶은 내용..">
        <button>질문하기</button>
    </form>
    <p>
        🤖 : <%= request.getAttribute("answer") %>
    </p>
</body>
</html>