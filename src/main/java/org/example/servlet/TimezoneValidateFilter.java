package org.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.TimeZone;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String timezoneParam = httpRequest.getParameter("timezone");

        if (timezoneParam != null && !isValidTimeZone(timezoneParam)) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("text/html; charset=utf-8");
            httpResponse.getWriter().write("<html>");
            httpResponse.getWriter().write("<head><title>Invalid Timezone</title></head>");
            httpResponse.getWriter().write("<body>");
            httpResponse.getWriter().write("<h1>Invalid timezone</h1>");
            httpResponse.getWriter().write("</body>");
            httpResponse.getWriter().write("</html>");
            httpResponse.getWriter().close();
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isValidTimeZone(String timeZoneId) {
        String[] validIDs = TimeZone.getAvailableIDs();
        for (String id : validIDs) {
            if (id.equals(timeZoneId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
