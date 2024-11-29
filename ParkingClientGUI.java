import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ParkingClientGUI extends JFrame {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private JTextArea messageArea;
    private JTextField cedulaField, nombreField, placasField, tipoVehiculoField, spaceField, horaField;
    private JButton submitButton, reserveButton;

    public ParkingClientGUI() {
        // Configuración de la ventana
        setTitle("Gestión de Parqueadero - Cliente");
        setSize(400, 500);  // Ajustamos para incluir el campo de hora
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear componentes
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        // Campos para ingresar los datos personales
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));  // Ajustamos el layout
        inputPanel.add(new JLabel("Cédula:"));
        cedulaField = new JTextField();
        inputPanel.add(cedulaField);
        inputPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        inputPanel.add(nombreField);
        inputPanel.add(new JLabel("Placas del vehículo:"));
        placasField = new JTextField();
        inputPanel.add(placasField);
        inputPanel.add(new JLabel("Tipo de vehículo:"));
        tipoVehiculoField = new JTextField();
        inputPanel.add(tipoVehiculoField);

        // Campo para la hora de reserva en formato 24 horas
        inputPanel.add(new JLabel("Hora de reserva (24 horas):"));
        horaField = new JTextField();
        inputPanel.add(horaField);

        // Campo para ingresar el espacio a reservar
        inputPanel.add(new JLabel("Número de espacio:"));
        spaceField = new JTextField();
        inputPanel.add(spaceField);

        // Botones
        submitButton = new JButton("Enviar Datos");
        reserveButton = new JButton("Reservar Espacio");
        reserveButton.setEnabled(false); // Inicialmente deshabilitado

        // Acción del botón Enviar Datos
        submitButton.addActionListener((ActionEvent e) -> {
            String cedula = cedulaField.getText();
            String nombre = nombreField.getText();
            String placas = placasField.getText();
            String tipoVehiculo = tipoVehiculoField.getText();
            String horaReserva = horaField.getText();
            
            if (!cedula.isEmpty() && !nombre.isEmpty() && !placas.isEmpty() && !tipoVehiculo.isEmpty() && !horaReserva.isEmpty()) {
                // Conectar al servidor y enviar los datos
                output.println(cedula);
                output.println(nombre);
                output.println(placas);
                output.println(tipoVehiculo);
                output.println(horaReserva);  // Enviar la hora de reserva
                
                // Habilitar el botón para reservar espacio
                reserveButton.setEnabled(true);
                submitButton.setEnabled(false); // Deshabilitar después de enviar los datos
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            }
        });

        // Acción del botón Reservar Espacio
        reserveButton.addActionListener((ActionEvent e) -> {
            String espacio = spaceField.getText();
            if (!espacio.isEmpty()) {
                output.println(espacio);  // Enviar el espacio seleccionado al servidor
            }
        });

        // Agregar componentes al layout
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(reserveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Conectar al servidor
        try {
            socket = new Socket("localhost", 8080);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            // Hilo para leer mensajes del servidor
            new Thread(() -> {
                try {
                    String messageFromServer;
                    while ((messageFromServer = input.readLine()) != null) {
                        messageArea.append(messageFromServer + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ParkingClientGUI().setVisible(true);
        });
    }
}
