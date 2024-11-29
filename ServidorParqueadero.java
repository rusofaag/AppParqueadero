import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServidorParqueadero {
    // Lista de espacios disponibles usando nomenclatura alfanumérica
    private static final List<String> availableSpaces = new ArrayList<>(Arrays.asList("P1-1", "P1-5", "P1-9", "P1-13", "P1-18", "P1-23", "P1-34"));

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor iniciado. Esperando conexión...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

                    // Mostrar los espacios disponibles antes de solicitar los datos
                    output.println("Espacios disponibles: " + String.join(", ", availableSpaces));

                    // Leer los datos enviados por el cliente
                    String cedula = input.readLine();  // Leer cédula
                    String nombre = input.readLine();  // Leer nombre
                    String placas = input.readLine();  // Leer placas del vehículo
                    String tipoVehiculo = input.readLine();  // Leer tipo de vehículo
                    String horaReserva = input.readLine();  // Leer la hora de reserva

                    // Obtener la fecha actual
                    String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    // Confirmación de datos ingresados
                    output.println("Bienvenido, " + nombre + ". Cédula: " + cedula);
                    output.println("Datos del vehículo - Placas: " + placas + ", Tipo: " + tipoVehiculo);
                    output.println("Fecha de reserva: " + fechaActual + ", Hora de reserva: " + horaReserva);

                    // Solicitar espacio a reservar
                    output.println("Seleccione el número del espacio que desea reservar:");
                    String requestedSpace = input.readLine();
                    
                    // Validar espacio y proceder con la reserva
                    if (availableSpaces.contains(requestedSpace)) {
                        availableSpaces.remove(requestedSpace);
                        output.println("Confirmado: Espacio " + requestedSpace + " reservado exitosamente para su vehículo tipo " + tipoVehiculo + " con placas " + placas);
                        output.println("Fecha y hora de reserva: " + fechaActual + " " + horaReserva);
                    } else {
                        output.println("El espacio " + requestedSpace + " no está disponible. Intente con otro.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
