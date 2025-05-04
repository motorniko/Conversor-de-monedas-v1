import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConversorMoneda {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("-------------------");
        System.out.println("     == MOTORNIKO ==");
        System.out.println("--Conversor de monedas--");
        System.out.println("-------------------");

        String monedaOrigen;
        while (true) {
            System.out.println("Ingrese el tipo de moneda que desea convertir ejemplo: 'CLP'");
            System.out.print("Convertir de:");
            monedaOrigen = scanner.nextLine().trim().toUpperCase();

            // Validar que tenga 3 letras
            if (monedaOrigen.matches("^[A-Z]{3}$")) {
                break;
            } else {
                System.out.println("Indique una moneda valida. (ej: USD, CLP).");
            }
        }

        double cantidad;
        while (true) {
            System.out.print("Ingrese la cantidad que desea convertir: ");
            String entrada = scanner.nextLine().trim();

            try {
                cantidad = Double.parseDouble(entrada);
                if (cantidad > 0) {
                    break;
                } else {
                    System.out.println("La cantidad a convertir debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido (ej: 100.50).");
            }
        }

        String monedaDestino;
        while (true) {
            System.out.println("Ingrese el tipo de moneda a la que desea convertir ejemplo: 'USD'");
            System.out.print("Convertir a :");
            monedaDestino = scanner.nextLine().trim().toUpperCase();

            if (monedaDestino.matches("^[A-Z]{3}$")) {
                break;
            } else {
                System.out.println("Código de moneda incorrecto. Ingrese un codigo valido (ej: USD, CLP).");
            }
        }

        System.out.println("Convirtiendo Espere un momento ..... LOADING....");

        try {
            // 2. Construir URL
            String apiKey = "66665556655545554544";
            String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + monedaOrigen;

            // 3. Conexión HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 4. Leer la respuesta
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                respuesta.append(linea);
            }
            reader.close();

            // 6. Buscar la tasa de cambio dentro de "conversion_rates"
            String json = respuesta.toString();
            String claveBusqueda = "\"" + monedaDestino + "\":";
            int index = json.indexOf(claveBusqueda);
            if (index != -1) {
                int start = json.indexOf(":", index) + 1;
                int end = json.indexOf(",", start);
                if (end == -1) {
                    end = json.indexOf("}", start);
                }
                String tasaStr = json.substring(start, end).trim();
                double tasa = Double.parseDouble(tasaStr);

                // 7. Calcular y mostrar el resultado
                double resultado = cantidad * tasa;
                System.out.println("\n" + cantidad + " " + monedaOrigen + " = " + resultado + " " + monedaDestino);
            } else {
                System.out.println("No se encontraron resultados");
            }

        } catch (Exception e) {
            System.out.println("Parece que tienes problemas de coneccion : " + e.getMessage());
        }
    }
}
