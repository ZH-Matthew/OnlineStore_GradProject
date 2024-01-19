package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Распространенный вариант использования — Spring Security,
//где функции аутентификации и контроля доступа обычно реализуются в виде фильтров,
// которые располагаются перед сервлетами основного приложения.
// Когда запрос отправляется с помощью диспетчера запросов, он должен снова пройти через цепочку фильтров
// (или, возможно, через другую), прежде чем он попадет к сервлету, который будет его обрабатывать.
// Проблема в том, что некоторые действия фильтра безопасности следует выполнять только один раз для запроса.
// Отсюда и необходимость в этом фильтре.
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
