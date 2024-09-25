package ua.goit.hw9;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    private static final String TEMPLATES = "/templates/";
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix(TEMPLATES);
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String timezone = req.getParameter("timezone");
        timezone = (timezone == null) ? getTimezoneFromCookie(req) : timezone.replaceAll(" ", "+");
        setCookie(resp, "lastTimezone", timezone);

        String currentTime = ZonedDateTime.now(ZoneId.of(timezone))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'X"));

        resp.setContentType("text/html; charset=utf-8");
        Context context = new Context();
        context.setVariable("time", currentTime);
        engine.process("time", context, resp.getWriter());
        resp.getWriter().close();
    }

    public String getTimezoneFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("lastTimezone"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("UTC");
        }
        return "UTC";
    }

    public void setCookie(HttpServletResponse resp, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(-1);
        resp.addCookie(cookie);
    }
}
