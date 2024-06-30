package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezoneParam = req.getParameter("timezone");
        ZoneId zoneId;

        if (timezoneParam != null) {
            zoneId = ZoneId.of(timezoneParam);
        } else {
            zoneId = ZoneId.of("UTC");
        }

        LocalDateTime now = LocalDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter) + " " + zoneId.getId();

        resp.setContentType("text/html; charset=utf-8");

        resp.getWriter().write("<html>");
        resp.getWriter().write("<head><title>Current Time</title></head>");
        resp.getWriter().write("<body>");
        resp.getWriter().write("<h1>Current Time</h1>");
        resp.getWriter().write("<p>" + formattedTime + "</p>");
        resp.getWriter().write("</body>");
        resp.getWriter().write("</html>");
        resp.getWriter().close();
    }
}
