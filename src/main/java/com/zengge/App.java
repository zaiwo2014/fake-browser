package com.zengge;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;

import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        //测试推送
        try (Playwright playwright = Playwright.create()) {
            // 使用 webkit 浏览器引擎启动浏览器
            Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setTimeout(60000));

            // 创建新的浏览器上下文
            BrowserContext context = browser.newContext();

            // 打开新页面
            Page page = context.newPage();
            page.setDefaultTimeout(30000);

            // 在 page.navigate() 之前添加请求监听器
            context.onRequest(request -> {
                // 只处理目标URL的请求
                if (request.url().contains("https://www.coupang.com/np/categories/317256")) {
                    System.out.println("目标请求URL: " + request.url());
                    System.out.println("请求头:");
                    Map<String, String> headers = request.headers();
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        System.out.println("  " + entry.getKey() + ": " + entry.getValue());
                    }
                    System.out.println("---");
                }
            });

            // 访问 Coupang 网站
            page.navigate("https://www.coupang.com/np/categories/317256?listSize=120&filterType=&rating=0&isPriceRange=false&minPrice=&maxPrice=&component=&sorter=bestAsc&brand=&offerCondition=&filter=&fromComponent=N&channel=user&selectedPlpKeepFilter=");

//            String htmlContent = page.content();
//            System.out.println(htmlContent);

            // 输出页面标题
            System.out.println("页面标题: " + page.title());

            // 获取并输出 cookies
            List<Cookie> cookies = context.cookies();
            for (Cookie cookie : cookies) {
                System.out.println(cookie.name + ":" + cookie.value);
            }



            // 关闭浏览器
            browser.close();

        } catch (Exception e) {
            System.err.println("执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
