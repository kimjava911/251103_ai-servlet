package org.example.aiservlet.controller;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.aiservlet.service.AIService;

import java.io.IOException;

// 이 서블릿 클래스를 -> 서블렛 컨테이너 등록 -> 사용 가능 하게 만드는 어노테이션
@WebServlet("/") // "/"
public class MainServlet extends HttpServlet {
    private AIService ai = null;

    @Override
    public void init() throws ServletException {
        // io.github.cdimascio.dotenv.DotenvException: Could not find /.env on the classpath
        // => resources 안에 dotenv가 들어가야한다
        // Dotenv dotenv = Dotenv.load();
        // System.out.println(dotenv.get("hello")); // !
        ai = new AIService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String answer = ai.chat("저녁 메뉴 추천, 메뉴만 20자 이내로. 꾸미는 텍스트 없이.");
//        req.setAttribute("answer", answer);
        req.setAttribute("answer", "질문을 입력해보세요!");
        req.getRequestDispatcher("/WEB-INF/chat.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String textParam = req.getParameter("text"); // form = post, input name=text
        String answer = ai.chat( "%s, 꾸미는 텍스트 없이.".formatted(textParam));
        req.setAttribute("answer", answer);
        req.getRequestDispatcher("/WEB-INF/chat.jsp").forward(req, resp);
    }
}
