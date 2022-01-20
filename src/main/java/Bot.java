import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    public static final String TOKEN = "1740595855:AAHAYAWFmlmUhSwlsBOiUn3Rx8G1bM7Wjt0";
    public static final String USER_NAME = "MyFlashTelegramBot";



    @Override
    public String getBotUsername() {
        return USER_NAME;
    }


    @Override
    public String getBotToken() {
        return TOKEN;
    }

    //method for receiving messages
    @Override
    public void onUpdateReceived(Update update) {
        //Create object model
        Model model = new Model();
        Message message = update.getMessage();


        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Чем я могу тебе помочь?");

                    break;
                case "/setting":
                    sendMsg(message, "Что будем настраивать?");
                    break;

//Проверяем является ли сообщение названием города
                default:
                    try {
                        //Помещаем сообщение которое отправил пользователь,
                        // результат обработки сообщения(с текстом сообщения и моделью)
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Отправляем сообщение о том что город не найден
                       sendMsg(message,"Такой город не найден,попробуй еще раз!");
                    }

            }

        }

    }

    //Метод который определяет что будет отвечать бот
    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        //включить возможность разметки
        sendMessage.enableMarkdown(true);
        //Определяем chat_id, чтобы бот знал в какой чат он должен ответить
        sendMessage.setChatId(message.getChatId().toString());
        //Получаем Message_id,т.е определяем на какое сообщение должен ответить наш бот
        sendMessage.setReplyToMessageId(message.getMessageId());
        //Передаем боту текст,который он будет писать
        sendMessage.setText(text);

        try {

            setButtons(sendMessage); //Linking the keyboard to the message
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }


    //Method that defines the keyboard under the text bar
    public void setButtons(SendMessage sendMessage) {
        //Initialize the keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //Set reply markup
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        //and we will also determine whether the keyboard
        // will be displayed to all users or only to individual users
        replyKeyboardMarkup.setSelective(true);
        // set resize keyboard for number of buttons
        replyKeyboardMarkup.setResizeKeyboard(true);
        //Specify whether to hide the keyboard after use or not(set false)
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        //Create List of buttons
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        //Create first row with buttons, keyBoardRow extends Arraylist
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));
        //Add to List
        keyboardRowList.add(keyboardFirstRow);
        //Add to keyboard
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        //To make the keyboard work you need to put it in the sending of messages


    }
}
