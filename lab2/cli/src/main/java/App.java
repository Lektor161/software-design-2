import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Scanner;

public class App {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String cmd = scanner.next();
            switch (cmd) {
                case "list-clients" -> listClients();
                case "get-client" -> {
                    String clientName = scanner.next();
                    getClient(clientName);
                }
                case "add-client" -> {
                    String clientName = scanner.next();
                    addClient(clientName);
                }
                case "add-sub" -> {
                    int clientId = scanner.nextInt();
                    int days = scanner.nextInt();
                    addSubscription(clientId, days);
                }
                case "in" -> {
                    String userName = scanner.next();
                    in(userName);
                }
                case "out" -> {
                    String userName = scanner.next();
                    out(userName);
                }
                case "stats" -> {
                    int lastDays = scanner.nextInt();
                    stats(lastDays);
                }
            }
        }
    }

    private static void listClients() throws IOException, InterruptedException {
        request(9000, "/manager-admin/clients/all", "GET");
    }

    private static void getClient(String clientName) throws IOException, InterruptedException {
        request(9000, "/manager-admin/clients/get?name=" + clientName, "GET");
    }

    private static void addClient(String clientName) throws IOException, InterruptedException {
        request(9000, "/manager-admin/clients/add?name=" + clientName, "POST");
    }

    private static void addSubscription(int clientName, int days) throws IOException, InterruptedException {
        request(9000, "/manager-admin/subscriptions/add?clientName=" + clientName + "&days=" + days, "POST");
    }

    private static void in(String userName) throws IOException, InterruptedException {
        request(9001, "/turnstile/in?userName=" + userName, "POST");
    }

    private static void out(String userName) throws IOException, InterruptedException {
        request(9001, "/turnstile/out?userName=" + userName, "POST");
    }

    private static void stats(int lastDays) throws IOException, InterruptedException {
        request(9002, "/report-service/stat-by-last-days?lastDays=" + lastDays, "GET");
    }

    private static void request(int port, String path, String method) throws IOException, InterruptedException {
        HttpResponse<String> response = HTTP_CLIENT.send(
                HttpRequest.newBuilder()
                        .method(method, HttpRequest.BodyPublishers.noBody())
                        .uri(URI.create("http://localhost:8080/api/v1" + path))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
        String body = response.body();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(body);
        if (!Objects.isNull(je)) {
            String prettyJsonString = gson.toJson(je);
            System.out.println(prettyJsonString);
        }
    }
}
