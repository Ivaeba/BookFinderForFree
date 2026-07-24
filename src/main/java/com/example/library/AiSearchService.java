package com.example.library;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

@Service
public class AiSearchService {

    private final ChatLanguageModel chatModel;

    public AiSearchService(ChatLanguageModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 1. ПОИСК ПРЯМОЙ ССЫЛКИ НА КНИГУ (С ДОБАВЛЕНИЕМ WORDPRESS)
     */
    public String findFreeBookUrl(String title, String author) {
        // Добавлен WordPress, образовательные блоги и открытые архивы
        String prompt = "Search for legal, open-access, or public domain versions of the book '" + title + "' by " + author + ". " +
                "Check authoritative digital libraries such as Project Gutenberg, Internet Archive, Open Library, Google Books preview, " +
                "as well as educational blogs, school sites, and open publications hosted on WordPress (.wordpress.com or WordPress-based library sites). " +
                "Return ONLY ONE raw direct HTTP/HTTPS link to read or view the book. " +
                "If no legal free link exists, reply with ONLY the exact word 'NOT_FOUND'. Do not add any extra text or polite phrases.";

        String aiResponseUrl = chatModel.generate(prompt);
        return aiResponseUrl.trim();
    }

    /**
     * 2. РЕЗЕРВНЫЙ ПЛАН: Генерация подробного конспекта (когда книги нет бесплатно)
     */
    public String generateBookSummary(String title, String author) {
        String prompt = "Так как полный бесплатный текст книги '" + title + "' автора " + author + " недоступен из-за авторских прав, " +
                "сделай подробный и структурированный конспект по этой книге на русском языке. " +
                "Включи следующие разделы: " +
                "1. Главная идея и суть книги. " +
                "2. Подробный разбор ключевых глав и концепций. " +
                "3. Главные выводы и чему учит эта книга. " +
                "Оформи ответ в виде аккуратного HTML (используй теги <h3>, <p>, <ul>, <li>, <strong>), без лишних предлогов.";

        return chatModel.generate(prompt);
    }
}