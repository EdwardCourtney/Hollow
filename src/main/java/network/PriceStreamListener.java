package network;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class PriceStreamListener {
    private Thread thread;
    private volatile boolean running;
    private HttpURLConnection connection;

    public void start(Long itemId, Consumer<Double> onPrice) {
        stop();

        running = true;
        thread = new Thread(() -> listen(itemId, onPrice));
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;

        if (connection != null) {
            connection.disconnect();
            connection = null;
        }

        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    private void listen(Long itemId, Consumer<Double> onPrice) {
        try {
            URL url = new URL("http://localhost:8080/items/stream/" + itemId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/event-stream");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(0);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            )) {
                String line;
                while (running && (line = reader.readLine()) != null) {
                    handleLine(line, onPrice);
                }
            }
        } catch (IOException ignored) {
        } finally {
            stop();
        }
    }

    private void handleLine(String line, Consumer<Double> onPrice) {
        if (!line.startsWith("data:")) {
            return;
        }

        String value = line.substring("data:".length()).trim();
        try {
            double price = Double.parseDouble(value);
            Platform.runLater(() -> onPrice.accept(price));
        } catch (NumberFormatException ignored) {
        }
    }
}
