package com.example.library;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class BookController {

    private final BookService bookService;
    private final AiSearchService aiSearchService;

    public BookController(BookService bookService, AiSearchService aiSearchService) {
        this.bookService = bookService;
        this.aiSearchService = aiSearchService;
    }

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    // 🔍 УМНЫЙ ИИ-ПОИСК С АВТОМАТИЧЕСКИМ ПЕРЕХОДОМ НА КОНСПЕКТ
    @GetMapping("/book/ai-find")
    public Object aiFindAndRead(
            @RequestParam String title,
            @RequestParam String author) {

        String cleanTitle = title.replace("+", " ").replace("%20", " ").trim();
        String cleanAuthor = author.replace("+", " ").replace("%20", " ").trim();

        // 1. Спрашиваем ИИ про прямую ссылку
        String urlFoundByAi = aiSearchService.findFreeBookUrl(cleanTitle, cleanAuthor);

        // Извлекаем чистую ссылку из ответа
        if (urlFoundByAi.contains("http")) {
            int startIndex = urlFoundByAi.indexOf("http");
            int endIndex = urlFoundByAi.indexOf(" ", startIndex);
            if (endIndex == -1) endIndex = urlFoundByAi.length();
            urlFoundByAi = urlFoundByAi.substring(startIndex, endIndex).replaceAll("[\\]\\)\"']", "").trim();
        }

        String cleanUrl = urlFoundByAi.toLowerCase().trim();

        // 2. ПРОВЕРКА: Если ссылка НЕ найдена (книга платная) -> отправляем на генерацию конспекта
        if (cleanUrl.contains("sorry") ||
                cleanUrl.contains("not_found") ||
                cleanUrl.contains("can't") ||
                !cleanUrl.startsWith("http")) {

            // Перенаправляем на страницу с ИИ-конспектом!
            return new RedirectView("/book/summary?title=" + cleanTitle + "&author=" + cleanAuthor);
        }

        // 3. Если бесплатная ссылка найдена -> летим прямо на книгу (Gutenberg/Archive.org)
        return new RedirectView(urlFoundByAi);
    }

    // 📖 СТРАНИЦА КОНСПЕКТА (Резервный вариант)
    @GetMapping(value = "/book/summary", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getBookSummary(@RequestParam String title, @RequestParam String author) {
        // Генерируем конспект через ИИ
        String summaryHtml = aiSearchService.generateBookSummary(title, author);

        // Возвращаем красивую веб-страницу с конспектом
        return "<!DOCTYPE html><html lang='ru'><head>" +
                "<meta charset='UTF-8'>" +
                "<title>Конспект книги: " + title + "</title>" +
                "<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>" +
                "<style>body{background-color:#f8f9fa; padding: 40px 0;} .container{max-width:800px; background:white; padding:30px; border-radius:15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1);}</style>" +
                "</head><body>" +
                "<div class='container'>" +
                "<div class='alert alert-warning mb-4'>⚠️ <strong>Бесплатный полный текст недоступен из-за авторских прав.</strong><br>ИИ подготовил для вас подробный конспект этой книги:</div>" +
                "<h1 class='text-primary mb-3'>" + title + "</h1>" +
                "<h5 class='text-muted mb-4'>Автор: " + author + "</h5><hr>" +
                "<div class='content'>" + summaryHtml + "</div>" +
                "<br><a href='/' class='btn btn-outline-primary mt-3'>← Вернуться в поиск</a>" +
                "</div></body></html>";
    }

    @GetMapping("/book/buyBook")
    @ResponseBody
    public String buyBook(@RequestParam Long id) {
        return "<html><body style='font-family: Arial; text-align: center; margin-top: 50px;'>" +
                "<h2>" + bookService.buyBook(id) + "</h2>" +
                "<br><a href='/'>← Вернуться на главную</a></body></html>";
    }
}