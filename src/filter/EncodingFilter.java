package filter;

import OtherNet.FirstTest;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    public EncodingFilter(){
        System.out.println("过滤器构造");
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化-1");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8"); //将编码改为utf-8
        servletResponse.setContentType("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;charset=utf-8");
        filterChain.doFilter(servletRequest, servletResponse);
        new FirstTest().test();
    }

    @Override
    public void destroy() {
        System.out.println("过滤器销毁");
    }
}