import java.io.*;
import java.net.*;

public class ParkingClient {

    public static void main(String[] args) {
        try {
            try ( // Conectar al servidor en localhost y puerto 8080
                    Socket socket = new Socket("localhost", 8080)) {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                // Leer mensajes del servidor
                String serverMessage;
                while ((serverMessage = input.readLine()) != null) {
                    System.out.println("Servidor: " + serverMessage);
                    
                    // Si el servidor solicita una respuesta del cliente
                    if (serverMessage.contains("Seleccione un espacio")) {
                        System.out.print("Cliente: ");
                        String userChoice = consoleInput.readLine();
                        output.println(userChoice);
                    }
                }
                // Cerrar conexión
            }
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}
