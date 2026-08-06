package com.rusmessanger.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIService {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Простейшая симуляция: "думает" 1.5 сек и отвечает шаблонно.
    // Вместо симуляции можно подключить внешний API (OpenAI, локальный LLM и т.д.)
    public CompletableFuture<String> ask(String prompt) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500); // имитация "думает"
            } catch (InterruptedException ignored) {}
            return "Я — ИИ помощник. Могу помочь с: создание чата, поиск, отправка кода на почту.";
        }, executor);
    }

    /*
    // Пример интеграции с OpenAI (закомментирован):
    public CompletableFuture<String> ask(String prompt) {
        return CompletableFuture.supplyAsync(() -> {
            // Сделать HTTP POST к /v1/chat/completions, передав API_KEY из env.
            // Используйте OkHttp + Gson. Не забудьте обработать ошибки и таймауты.
            return apiResponse;
        }, executor);
    }
    */
}
