import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    //CREATED TOKEN(API) - b3bd86eec3cabd5bf5f5124aabc557f6
    //К которому мы будем обращаться
    // Создаем метод для извлечения содержимого, который будет отправлять запрос к API по URL и токену, и возвращать

    public static String getWeather(String message, Model model) throws IOException {
        //Формируем запрос. Пользователь должен указать город, погоду которго он хочет узнать
        //Также указываем токен API
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" +
                message + "&units=metric&appid=b3bd86eec3cabd5bf5f5124aabc557f6");
        //Чтобы прочитать содержимое используем Scanner
        //Вызываем у объекта URL метод getContent,который возвращает объект Object
        Scanner scanner = new Scanner((InputStream) url.getContent());
        //Создаем переменную в которую поместим результат
        String result = "";
        //Пока мы можем что-то считывать добавляем в переменную result строку
        while (scanner.hasNext()) {
            result += scanner.nextLine();
        }
        //Весь объект json находится в строке result, мы кастим json объект нашу строку
        JSONObject object = new JSONObject(result);

        //Получаем город из объекта json, name это название города,вытаскиваем из object
        model.setName(object.getString("name"));
        //Получаем маленький вложенный объект json из большого под названием main
        JSONObject main = object.getJSONObject("main");
        //Достаем из main остальные поля, температуру и влажность
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        //Извлекаем json объект weather, и пробегаемся по нему, в нашем случае
        // у нас только один маленький объект
        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            //Инициализируем первый объект и единственный
            JSONObject obj = getArray.getJSONObject(i);
            //И забираем наши значения
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }


        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + "C" + "\n" +
                "Humidity: " + model.getHumidity() + "%" + "\n" +
                "On the street: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + "@2x.png";
        //http://openweathermap.org/img/wn/10d@2x.png


    }
}
