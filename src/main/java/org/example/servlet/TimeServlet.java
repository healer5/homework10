package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.TimeZone;
@WebServlet("/")
public class TimeServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCacheable(false);
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezoneParam = req.getParameter("timezone");
        ZoneId zoneId = ZoneId.of("UTC"); // Default to UTC

        if (timezoneParam == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                Optional<Cookie> lastTimezoneCookie = Arrays.stream(cookies)
                        .filter(cookie -> "lastTimezone".equals(cookie.getName()))
                        .findFirst();
                if (lastTimezoneCookie.isPresent()) {
                    timezoneParam = lastTimezoneCookie.get().getValue();
                }
            }
        }

        if (timezoneParam != null) {
            zoneId = ZoneId.of(timezoneParam);
        }

        LocalDateTime now = LocalDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter) + " " + zoneId.getId();

        Context context = new Context();
        context.setVariable("time", formattedTime);

        resp.setContentType("text/html; charset=utf-8");
        templateEngine.process("time", context, resp.getWriter());
    }
}