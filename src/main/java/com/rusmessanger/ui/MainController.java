package com.rusmessanger.ui;

import com.rusmessanger.services.AIService;
import com.rusmessanger.services.EmailService;
import com.rusmessanger.model.Contact;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainController {
    @FXML public TextField searchField;
    @FXML public ListView<Contact> contactsList;
    @FXML public ListView<String> chatsList;
    @FXML public Label currentChatName;
    @FXML public VBox messagesBox;
    @FXML public TextField messageField;
    @FXML public Label statusLabel;

    private List<Contact> contacts = new ArrayList<>();
    private EmailService emailService = new EmailService();
    private AIService aiService = new AIService();

    @FXML
    public void initialize() {
        // contactsList initially empty (user asked: не добавляй контакты на картинке)
        contactsList.getItems().clear();

        searchField.textProperty().addListener((obs, oldV, newV) -> onSearch(newV));
        chatsList.getItems().addAll(); // empty for now

        // simple animation on load
        FadeTransition ft = new FadeTransition(Duration.millis(700), messagesBox);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();
    }

    public void onSearch(String query) {
        // Простейший поиск по нику или email среди contacts (разработай добавление контактов)
        contactsList.getItems().clear();
        for (Contact c : contacts) {
            if (query == null || query.isEmpty() ||
                c.getName().toLowerCase().contains(query.toLowerCase()) ||
                c.getEmail().toLowerCase().contains(query.toLowerCase())) {
                contactsList.getItems().add(c);
            }
        }
    }

    public void onAddContact() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавить контакт");
        dialog.setHeaderText("Введите email или ник контакта:");
        dialog.showAndWait().ifPresent(input -> {
            // Можно проверить формат email, или поиск по нику на сервере
            Contact c = new Contact(input, input);
            contacts.add(c);
            contactsList.getItems().add(c);
            statusLabel.setText("Контакт добавлен: " + input);
        });
    }

    public void onCreateGroup() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Создать группа/канал");
        dialog.setHeaderText("Название группы:");
        dialog.showAndWait().ifPresent(name -> {
            chatsList.getItems().add(name);
            statusLabel.setText("Группа создана: " + name);
        });
    }

    public void onFavorites() { statusLabel.setText("Открыто: Избранное (заготовка)"); }
    public void onCalls() { statusLabel.setText("Открыто: Звонки (заготовка)"); }
    public void onSettings() { statusLabel.setText("Открыто: Настройки (заготовка)"); }

    public void onSendMessage() {
        String text = messageField.getText();
        if (text == null || text.isBlank()) return;
        Label msg = new Label("Вы: " + text);
        msg.getStyleClass().add("message-out");
        messagesBox.getChildren().add(msg);
        messageField.clear();

        // Плавная анимация появления
        FadeTransition ft = new FadeTransition(Duration.millis(300), msg);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();
    }

    public void onOpenAssistant() {
        statusLabel.setText("ИИ Помощник: думает...");
        // показать индикатор в UI
        ProgressIndicator pi = new ProgressIndicator();
        messagesBox.getChildren().add(pi);

        // Вызов ИИ асинхронно
        CompletableFuture<String> fut = aiService.ask("Привет! Подсказка от UI");
        fut.whenComplete((answer, ex) -> Platform.runLater(() -> {
            messagesBox.getChildren().remove(pi);
            if (ex != null) {
                statusLabel.setText("Ошибка ИИ: " + ex.getMessage());
            } else {
                Label ans = new Label("ИИ: " + answer);
                ans.getStyleClass().add("message-in");
                messagesBox.getChildren().add(ans);
                statusLabel.setText("ИИ ответил");
                FadeTransition ft = new FadeTransition(Duration.millis(400), ans);
                ft.setFromValue(0);
                ft.setToValue(1.0);
                ft.play();
            }
        }));
    }

    // Пример: отправить код подтверждения на email
    public void sendVerificationTo(String email) {
        String code = emailService.generateCode();
        try {
            emailService.sendVerificationCode(email, code);
            statusLabel.setText("Код отправлен на " + email);
        } catch (Exception e) {
            statusLabel.setText("Ошибка отправки: " + e.getMessage());
        }
    }
}
